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
fun Resolved<Model>.toTypeName(): TypeName = value.toTypeName()

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
): TypeSpec.Builder =
  classBuilder(className)
    .addModifiers(KModifier.DATA)
    .primaryConstructor(FunSpec.constructorBuilder().addParameters(parameters).build())
    .addProperties(
      parameters.map { param ->
        PropertySpec.builder(param.name, param.type).initializer(param.name).build()
      }
    )

val PrimitiveSerialDescriptor =
  MemberName("kotlinx.serialization.descriptors", "PrimitiveSerialDescriptor")
val ContentType = ClassName("io.ktor.http", "ContentType")
val HttpResponse = ClassName("io.ktor.client.statement", "HttpResponse")
val ListSerializer = MemberName("kotlinx.serialization.builtins", "ListSerializer")
val SetSerializer = MemberName("kotlinx.serialization.builtins", "SetSerializer")
val MapSerializer = MemberName("kotlinx.serialization.builtins", "MapSerializer")
val SerialDescriptor = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
