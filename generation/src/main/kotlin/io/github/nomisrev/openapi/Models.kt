package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.NamingContext.Named
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

context(OpenAPIContext)
fun Iterable<Model>.toFileSpecs(): List<FileSpec> = mapNotNull { it.toFileSpec() }

context(OpenAPIContext)
fun Model.toFileSpec(): FileSpec? =
  when (val model = intercept(this)) {
    is Collection -> model.inner.toFileSpec()
    is Model.Enum.Closed ->
      FileSpec.builder(`package`, toClassName(model.context).simpleName)
        .addType(model.toTypeSpec())
        .build()
    is Model.Enum.Open ->
      FileSpec.builder(`package`, toClassName(model.context).simpleName)
        .addType(model.toTypeSpec())
        .build()
    is Model.Object ->
      FileSpec.builder(`package`, toClassName(model.context).simpleName)
        .addType(model.toTypeSpec())
        .build()
    is Model.Union ->
      FileSpec.builder(`package`, toClassName(model.context).simpleName)
        .addType(model.toTypeSpec())
        .build()
    is Model.OctetStream,
    is Model.Primitive,
    is Model.FreeFormJson -> null
  }

context(OpenAPIContext)
fun Model.toTypeSpecOrNull(): TypeSpec? {
  tailrec fun toTypeSpecOrNull(m: Model): TypeSpec? =
    when (val model = intercept(m)) {
      is Model.OctetStream,
      is Model.FreeFormJson,
      is Model.Primitive -> null
      is Collection -> toTypeSpecOrNull(model.inner)
      is Model.Enum.Closed -> model.toTypeSpec()
      is Model.Enum.Open -> model.toTypeSpec()
      is Model.Object -> model.toTypeSpec()
      is Model.Union -> model.toTypeSpec()
    }

  return toTypeSpecOrNull(this)
}

context(OpenAPIContext)
@OptIn(ExperimentalSerializationApi::class)
private fun Model.Union.toTypeSpec(): TypeSpec {
  fun Model.placeholder(buffer: MutableList<Any>): String =
    when (this) {
      is Collection.List -> {
        buffer.add(ListSerializer)
        "%M(${inner.placeholder(buffer)})"
      }
      is Collection.Map -> {
        buffer.add(MapSerializer)
        "%M(${key.placeholder(buffer)}, ${inner.placeholder(buffer)})"
      }
      is Collection.Set -> {
        buffer.add(SetSerializer)
        "%M(${inner.placeholder(buffer)})"
      }
      is Model.Primitive -> {
        buffer.add(toTypeName())
        buffer.add(MemberName("kotlinx.serialization.builtins", "serializer", isExtension = true))
        "%T.%M()"
      }
      else -> {
        buffer.add(toTypeName())
        "%T.serializer()"
      }
    }

  fun Model.serializer(): Pair<String, Array<Any>> {
    val buffer = mutableListOf<Any>()
    val placeholder = placeholder(buffer)
    return Pair(placeholder, buffer.toTypedArray())
  }

  return TypeSpec.interfaceBuilder(toClassName(context))
    .description(description)
    .addModifiers(KModifier.SEALED)
    .addAnnotation(annotationSpec<Serializable>())
    .addTypes(
      cases.map { case ->
        val model = case.model
        TypeSpec.classBuilder(toCaseClassName(case.model).simpleName)
          .description(model.description)
          .addModifiers(KModifier.VALUE)
          .addAnnotation(annotationSpec<JvmInline>())
          .primaryConstructor(
            FunSpec.constructorBuilder()
              .addParameter(ParameterSpec.builder("value", model.toTypeName()).build())
              .build()
          )
          .addProperty(PropertySpec("value", model.toTypeName()) { initializer("value") })
          .addSuperinterface(toClassName(context))
          .build()
      }
    )
    .addTypes(inline.mapNotNull { it.toTypeSpecOrNull() })
    .addType(
      TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(KSerializer::class.asTypeName().parameterizedBy(toClassName(context)))
        .addProperty(
          PropertySpec("descriptor", SerialDescriptor) {
            addModifiers(KModifier.OVERRIDE)
            addAnnotation(SerializationOptIn)
            initializer(
              CodeBlock.builder()
                .add(
                  "%M(%S, %T.SEALED) {\n",
                  MemberName("kotlinx.serialization.descriptors", "buildSerialDescriptor"),
                  toClassName(context).simpleNames.joinToString("."),
                  PolymorphicKind::class
                )
                .withIndent {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer()
                    add(
                      "element(%S, $placeholder.descriptor)\n",
                      toCaseClassName(case.model).simpleNames.joinToString("."),
                      *values
                    )
                  }
                }
                .add("}\n")
                .build()
            )
          }
        )
        .addFunction(
          FunSpec.builder("serialize")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("encoder", ClassName("kotlinx.serialization.encoding", "Encoder"))
            .addParameter("value", toClassName(context))
            .addCode(
              CodeBlock.builder()
                .add("when(value) {\n")
                .apply {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer()
                    addStatement(
                      "is %T -> encoder.encodeSerializableValue($placeholder, value.value)",
                      toCaseClassName(case.model),
                      *values
                    )
                  }
                }
                .add("}\n")
                .build()
            )
            .build()
        )
        .addFunction(
          FunSpec.builder("deserialize")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("decoder", Decoder::class.asTypeName())
            .returns(toClassName(context))
            .addCode(
              CodeBlock.builder()
                .addStatement(
                  "val value = decoder.decodeSerializableValue(%T.serializer())",
                  JsonElement::class.asTypeName()
                )
                .addStatement(
                  "val json = requireNotNull(decoder as? %T) { %S }.json",
                  JsonDecoder::class.asTypeName(),
                  "Currently only supporting Json"
                )
                .addStatement("return attemptDeserialize(value,")
                .withIndent {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer()
                    val caseClassName = toCaseClassName(case.model)
                    add(
                      "Pair(%T::class) { %T(json.decodeFromJsonElement($placeholder, value)) },\n",
                      caseClassName,
                      caseClassName,
                      *values
                    )
                  }
                }
                .add(")\n")
                .build()
            )
            .build()
        )
        .build()
    )
    .build()
}

context(OpenAPIContext)
private fun Model.Object.toTypeSpec(): TypeSpec =
  TypeSpec.dataClass(
    toClassName(context),
    properties.map { prop ->
      ParameterSpec(toPropName(prop), prop.model.toTypeName().copy(nullable = prop.isNullable)) {
        if (toPropName(prop) != prop.baseName)
          addAnnotation(annotationSpec<SerialName> { addMember("%S", prop.baseName) })
        description(prop.description)
        defaultValue(prop.model)
        val hasDefault = prop.model.hasDefault()
        if (prop.isRequired && hasDefault) addAnnotation(annotationSpec<Required>())
        else if (!prop.isRequired && !hasDefault && prop.isNullable) defaultValue("null")
      }
    }
  ) {
    // Cannot serialize binary, these are used for multipart requests.
    // This occurs when request bodies are defined using top-level schemas.
    if (properties.none { it.model is Model.OctetStream })
      addAnnotation(annotationSpec<Serializable>())
    addTypes(inline.mapNotNull { it.toTypeSpecOrNull() })
  }

context(OpenAPIContext)
private fun Model.Enum.Closed.toTypeSpec(): TypeSpec {
  val rawToName = values.map { rawName -> Pair(rawName, toEnumValueName(rawName)) }
  val isSimple = rawToName.all { (rawName, valueName) -> rawName == valueName }
  val enumName = toClassName(context)
  return TypeSpec.enumBuilder(enumName)
    .description(description)
    .apply {
      if (!isSimple) {
        primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
        addProperty(PropertySpec.builder("value", STRING).initializer("value").build())
      }
      rawToName.forEach { (rawName, valueName) ->
        if (isSimple) addEnumConstant(rawName)
        else {
          addEnumConstant(
            valueName,
            TypeSpec.anonymousClassBuilder()
              .addAnnotation(annotationSpec<SerialName> { addMember("\"$rawName\"") })
              .apply {
                // If we're `9...` we need to drop backticks, and prefix with `_`
                if (Regex("`[0-9]").matchesAt(valueName, 0))
                  addAnnotation(
                    AnnotationSpec.builder(ClassName("kotlin.js", "JsName"))
                      .addMember("\"_${valueName.drop(1).dropLast(1)}\"")
                      .build()
                  )
              }
              .addSuperclassConstructorParameter("\"$rawName\"")
              .build()
          )
        }
      }
    }
    .addAnnotation(annotationSpec<Serializable>())
    .build()
}

context(OpenAPIContext)
private fun Model.Enum.Open.toTypeSpec(): TypeSpec {
  val rawToName = values.map { rawName -> Pair(rawName, toEnumValueName(rawName)) }
  val enumName = toClassName(context)
  return TypeSpec.interfaceBuilder(enumName)
    .description(description)
    .addModifiers(KModifier.SEALED)
    .addProperty(PropertySpec("value", STRING) { addModifiers(KModifier.ABSTRACT) })
    .addAnnotation(annotationSpec<Serializable>())
    .addType(
      TypeSpec.classBuilder("OpenCase")
        .addModifiers(KModifier.VALUE)
        .addAnnotation(annotationSpec<JvmInline>())
        .primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
        .addProperty(
          PropertySpec("value", STRING) {
            initializer("value")
            addModifiers(KModifier.OVERRIDE)
          }
        )
        .addSuperinterface(toClassName(context))
        .build()
    )
    .addTypes(
      rawToName.map { (rawName, valueName) ->
        TypeSpec.objectBuilder(valueName)
          .addModifiers(KModifier.DATA)
          .addSuperinterface(toClassName(context))
          .addProperty(
            PropertySpec("value", STRING) {
              initializer("\"$rawName\"")
              addModifiers(KModifier.OVERRIDE)
            }
          )
          .build()
      }
    )
    .addType(
      TypeSpec.companionObjectBuilder()
        .addProperty(
          PropertySpec("defined", LIST.parameterizedBy(toClassName(context))) {
            initializer(
              CodeBlock.builder()
                .add("listOf(")
                .apply {
                  rawToName.forEachIndexed { index, (_, valueName) ->
                    add(valueName)
                    if (index < rawToName.size - 1) add(", ")
                  }
                  add(")")
                }
                .build()
            )
          }
        )
        .addType(
          TypeSpec.objectBuilder("Serializer")
            .addSuperinterface(
              KSerializer::class.asTypeName().parameterizedBy(toClassName(context))
            )
            .addProperty(
              PropertySpec("descriptor", SerialDescriptor) {
                addModifiers(KModifier.OVERRIDE)
                addAnnotation(
                    AnnotationSpec.builder(
                        ClassName("kotlinx.serialization", "InternalSerializationApi")
                      )
                      .build()
                  )
                  .initializer(
                    CodeBlock.builder()
                      .addStatement(
                        "%M(%S, %T.STRING)",
                        PrimitiveSerialDescriptor,
                        enumName,
                        PrimitiveKind::class.asTypeName()
                      )
                      .build()
                  )
              }
            )
            .addFunction(
              FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", Encoder::class.asTypeName())
                .addParameter("value", toClassName(context))
                .addCode("encoder.encodeString(value.value)")
                .build()
            )
            .addFunction(
              FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("decoder", Decoder::class.asTypeName())
                .returns(toClassName(context))
                .addCode(
                  CodeBlock.builder()
                    .addStatement("val value = decoder.decodeString()")
                    .addStatement("return attemptDeserialize(value,")
                    .withIndent {
                      rawToName.forEach { (_, name) ->
                        val nested = NamingContext.Nested(Named(name), context)
                        addStatement(
                          "Pair(%T::class) { defined.find { it.value == value } },",
                          toClassName(nested)
                        )
                      }
                      addStatement("Pair(OpenCase::class) { OpenCase(value) }")
                    }
                    .addStatement(")")
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build()
}
