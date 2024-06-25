package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.SET
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asTypeName
import io.github.nomisrev.openapi.Model.Collection
import kotlinx.serialization.json.JsonElement

context(OpenAPIContext)
fun Model.toTypeName(): TypeName =
  when (this) {
    is Model.Primitive.Boolean -> BOOLEAN
    is Model.Primitive.Double -> DOUBLE
    is Model.Primitive.Int -> INT
    is Model.Primitive.String -> STRING
    is Model.Primitive.Unit -> UNIT
    is Collection.List -> LIST.parameterizedBy(inner.toTypeName())
    is Collection.Set -> SET.parameterizedBy(inner.toTypeName())
    is Collection.Map -> MAP.parameterizedBy(STRING, inner.toTypeName())
    is Model.OctetStream -> ClassName(`package`, "UploadFile")
    is Model.FreeFormJson -> JsonElement::class.asTypeName()
    is Model.Enum -> toClassName(context)
    is Model.Object -> toClassName(context)
    is Model.Union -> toClassName(context)
  }

fun TypeName.nullable(): TypeName = copy(nullable = true)

inline fun <reified A : Annotation> annotationSpec(): AnnotationSpec =
  AnnotationSpec.builder(A::class).build()

fun TypeSpec.Builder.description(kdoc: String?): TypeSpec.Builder = apply {
  kdoc?.let { addKdoc("%L", it) }
}

fun ParameterSpec.Builder.description(kdoc: String?): ParameterSpec.Builder = apply {
  kdoc?.let { addKdoc("%L", it) }
}

fun TypeSpec.Companion.dataClassBuilder(
  className: ClassName,
  parameters: List<ParameterSpec>
): TypeSpec.Builder {
  val sorted = parameters.sorted()
  return classBuilder(className)
    .addModifiers(KModifier.DATA)
    .primaryConstructor(
      FunSpec.constructorBuilder()
        .addParameters(sorted)
        .build()
    )
    .addProperties(
      sorted.map { param ->
        PropertySpec.builder(param.name, param.type).initializer(param.name).build()
      }
    )
}

private fun List<ParameterSpec>.sorted(): List<ParameterSpec> {
  val (required, optional) = partition { it.defaultValue == null }
  return required + optional
}

fun ClassName.nested(name: String): ClassName =
  ClassName(packageName, simpleName, name)

val ContentType = ClassName("io.ktor.http", "ContentType")
val HttpResponse = ClassName("io.ktor.client.statement", "HttpResponse")
val SerialDescriptor = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
val ByteReadChannel = ClassName("io.ktor.utils.io", "ByteReadChannel")

val PrimitiveSerialDescriptor = MemberName("kotlinx.serialization.descriptors", "PrimitiveSerialDescriptor")
val ListSerializer = MemberName("kotlinx.serialization.builtins", "ListSerializer")
val SetSerializer = MemberName("kotlinx.serialization.builtins", "SetSerializer")
val MapSerializer = MemberName("kotlinx.serialization.builtins", "MapSerializer")
val contentType = MemberName("io.ktor.http", "contentType")
val setBody = MemberName("io.ktor.client.request", "setBody", isExtension = true)
val formData = MemberName("io.ktor.client.request.forms", "formData", isExtension = true)
val accept = MemberName("io.ktor.client.request", "accept", isExtension = true)
val header = MemberName("io.ktor.client.request", "header", isExtension = true)
val request = MemberName("io.ktor.client.request", "request", isExtension = true)
val HttpHeaders = ClassName("io.ktor.http", "HttpHeaders")
val HttpMethod = ClassName("io.ktor.http", "HttpMethod")
val seconds =
  MemberName("kotlin.time.Duration.Companion", "seconds", isExtension = true)
val DurationUnit =
  ClassName("kotlin.time", "DurationUnit")

val SerializationOptIn =
  AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
    .addMember("%T::class", ClassName("kotlinx.serialization", "ExperimentalSerializationApi"))
    .addMember("%T::class", ClassName("kotlinx.serialization", "InternalSerializationApi"))
    .build()