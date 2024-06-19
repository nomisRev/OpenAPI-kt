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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

context(OpenAPIContext)
fun Iterable<Model>.toFileSpecs(): List<FileSpec> = mapNotNull { it.toFileSpec() }

context(OpenAPIContext)
fun Model.toFileSpec(): FileSpec? =
  when (this) {
    is Collection -> inner.value.toFileSpec()
    is Model.Enum.Closed ->
      FileSpec.builder(`package`, toClassName(context).simpleName).addType(toTypeSpec()).build()
    is Model.Enum.Open ->
      FileSpec.builder(`package`, toClassName(context).simpleName).addType(toTypeSpec()).build()
    is Model.Object ->
      FileSpec.builder(`package`, toClassName(context).simpleName).addType(toTypeSpec()).build()
    is Model.Union ->
      FileSpec.builder(`package`, toClassName(context).simpleName).addType(toTypeSpec()).build()
    is Model.OctetStream,
    is Model.Primitive,
    is Model.FreeFormJson -> null
  }

tailrec fun Model.toTypeSpec(): TypeSpec? =
  when (this) {
    is Model.OctetStream,
    is Model.FreeFormJson,
    is Model.Primitive -> null
    is Collection -> inner.value.toTypeSpec()
    is Model.Enum.Closed -> toTypeSpec()
    is Model.Enum.Open -> toTypeSpec()
    is Model.Object -> toTypeSpec()
    is Model.Union -> toTypeSpec()
  }

context(OpenAPIContext)
@OptIn(ExperimentalSerializationApi::class)
private fun Model.Union.toTypeSpec(): TypeSpec =
  TypeSpec.interfaceBuilder(toClassName(context))
    .description(description)
    .addModifiers(KModifier.SEALED)
    .addAnnotation(annotationSpec<Serializable>())
    .addTypes(
      cases.map { case ->
        val model = case.model
        TypeSpec.classBuilder(toCaseClassName(this@toTypeSpec, case.model.value).simpleName)
          .description(model.value.description)
          .addModifiers(KModifier.VALUE)
          .addAnnotation(annotationSpec<JvmInline>())
          .primaryConstructor(
            FunSpec.constructorBuilder()
              .addParameter(ParameterSpec.builder("value", model.toTypeName()).build())
              .build()
          )
          .addProperty(
            PropertySpec.builder("value", model.toTypeName()).initializer("value").build()
          )
          .addSuperinterface(toClassName(context))
          .build()
      }
    )
    .addTypes(inline.mapNotNull { it.toTypeSpec() })
    .addType(
      TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(
          KSerializer::class.asTypeName().parameterizedBy(toClassName(context))
        )
        .addProperty(
          PropertySpec.builder("descriptor", SerialDescriptor)
            .addModifiers(KModifier.OVERRIDE)
            .addAnnotation(
              AnnotationSpec.builder(ClassName("kotlinx.serialization", "InternalSerializationApi"))
                .build()
            )
            .initializer(
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
                      toCaseClassName(this@toTypeSpec, case.model.value)
                        .simpleNames
                        .joinToString("."),
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
                      toCaseClassName(this@toTypeSpec, case.model.value),
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
                .add(
                  "val json = decoder.decodeSerializableValue(%T.serializer())\n",
                  JsonElement::class.asTypeName()
                )
                .add("return attemptDeserialize(json,\n")
                .apply {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer()
                    add(
                      "Pair(%T::class) { %T(%T.decodeFromJsonElement($placeholder, json)) },\n",
                      toCaseClassName(this@toTypeSpec, case.model.value),
                      toCaseClassName(this@toTypeSpec, case.model.value),
                      Json::class.asTypeName(),
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

private fun List<ParameterSpec>.sorted(): List<ParameterSpec> {
  val (required, optional) = partition { it.defaultValue == null }
  return required + optional
}

/*
 * Generating data classes with KotlinPoet!?
 * https://stackoverflow.com/questions/44483831/generate-data-class-with-kotlinpoet
 */
context(OpenAPIContext)
private fun Model.Object.toTypeSpec(): TypeSpec =
  TypeSpec.dataClassBuilder(
      toClassName(context),
      properties
        .map { prop ->
          val propName = toPropName(prop)
          ParameterSpec.builder(
              toPropName(prop),
              prop.model.toTypeName().copy(nullable = prop.isNullable)
            )
            .apply {
              if (propName != prop.baseName)
                addAnnotation(
                  annotationSpec<SerialName>()
                    .toBuilder()
                    .addMember("%S", prop.baseName)
                    .build()
                )
            }
            .description(prop.description)
            .defaultValue(prop.model.value)
            .apply {
              val hasDefault = prop.model.value.hasDefault()
              if (prop.isRequired && hasDefault) addAnnotation(annotationSpec<Required>())
              else if (!prop.isRequired && !hasDefault && prop.isNullable) defaultValue("null")
            }
            .build()
        }
        .sorted()
    )
    .apply {
      // Cannot serialize binary, these are used for multipart requests.
      // This occurs when request bodies are defined using top-level schemas.
      if (properties.none { it.model.value is Model.OctetStream })
        addAnnotation(annotationSpec<Serializable>())
    }
    .addTypes(inline.mapNotNull { it.toTypeSpec() })
    .build()

context(OpenAPIContext)
private fun Model.Enum.Closed.toTypeSpec(): TypeSpec {
  val rawToName = values.map { rawName -> Pair(rawName, toEnumValueName(rawName)) }
  val isSimple = rawToName.all { (rawName, valueName) -> rawName == valueName }
  val enumName = toClassName(context)
  return TypeSpec.enumBuilder(enumName)
    .description(description)
    .apply {
      if (!isSimple)
        primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
      rawToName.forEach { (rawName, valueName) ->
        if (isSimple) addEnumConstant(rawName)
        else
          addEnumConstant(
            valueName,
            TypeSpec.anonymousClassBuilder()
              .addAnnotation(
                annotationSpec<SerialName>().toBuilder().addMember("\"$rawName\"").build()
              )
              .addSuperclassConstructorParameter("\"$rawName\"")
              .build()
          )
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
    .addProperty(PropertySpec.builder("value", STRING).addModifiers(KModifier.ABSTRACT).build())
    .addAnnotation(annotationSpec<Serializable>())
    .addType(
      TypeSpec.classBuilder("OpenCase")
        .addModifiers(KModifier.VALUE)
        .addAnnotation(annotationSpec<JvmInline>())
        .primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
        .addProperty(
          PropertySpec.builder("value", STRING)
            .initializer("value")
            .addModifiers(KModifier.OVERRIDE)
            .build()
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
            PropertySpec.builder("value", STRING)
              .initializer("\"$rawName\"")
              .addModifiers(KModifier.OVERRIDE)
              .build()
          )
          .build()
      }
    )
    .addType(
      TypeSpec.companionObjectBuilder()
        .addProperty(
          PropertySpec.builder("defined", LIST.parameterizedBy(toClassName(context)))
            .initializer(
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
            .build()
        )
        .addType(
          TypeSpec.objectBuilder("Serializer")
            .addSuperinterface(
              KSerializer::class.asTypeName().parameterizedBy(toClassName(context))
            )
            .addProperty(
              PropertySpec.builder("descriptor", SerialDescriptor)
                .addModifiers(KModifier.OVERRIDE)
                .addAnnotation(
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
                .build()
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

context(OpenAPIContext)
private fun Resolved<Model>.serializer(): Pair<String, Array<Any>> =
  with(value) {
    val values: MutableList<Any> = mutableListOf()
    fun Model.placeholder(): String =
      when (this) {
        is Collection.List -> {
          values.add(ListSerializer)
          "%M(${inner.value.placeholder()})"
        }
        is Collection.Map -> {
          values.add(MapSerializer)
          "%M(${key.placeholder()}, ${inner.value.placeholder()})"
        }
        is Collection.Set -> {
          values.add(SetSerializer)
          "%M(${inner.value.placeholder()})"
        }
        is Model.Primitive -> {
          values.add(toTypeName())
          values.add(MemberName("kotlinx.serialization.builtins", "serializer", isExtension = true))
          "%T.%M()"
        }
        else -> {
          values.add(toTypeName())
          "%T.serializer()"
        }
      }

    return Pair(placeholder(), values.toTypedArray())
  }
