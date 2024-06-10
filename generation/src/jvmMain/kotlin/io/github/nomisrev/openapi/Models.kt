package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FileSpec
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
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import io.github.nomisrev.openapi.generation.NamingStrategy
import io.github.nomisrev.openapi.generation.default
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind

fun Model.toFileSpec(naming: NamingStrategy): FileSpec? =
  when (this) {
    is Collection -> value.toFileSpec(naming)
    is Model.Enum ->
      FileSpec.builder("io.github.nomisrev.openapi", naming.toEnumClassName(context))
        .addType(toTypeSpec(DefaultNamingStrategy))
        .build()
    is Model.Object ->
      FileSpec.builder("io.github.nomisrev.openapi", naming.toObjectClassName(context))
        .addType(toTypeSpec(DefaultNamingStrategy))
        .build()
    is Model.Union ->
      FileSpec.builder("io.github.nomisrev.openapi", naming.toUnionClassName(this))
        .addType(toTypeSpec(DefaultNamingStrategy))
        .build()
    Model.Binary,
    is Model.Primitive,
    Model.FreeFormJson -> null
  }

tailrec fun Model.toTypeSpec(naming: NamingStrategy): TypeSpec? =
  when (this) {
    is Model.Binary,
    is Model.FreeFormJson,
    is Model.Primitive -> null
    is Collection -> value.toTypeSpec(naming)
    is Model.Enum -> toTypeSpec(naming)
    is Model.Object -> toTypeSpec(naming)
    is Model.Union -> toTypeSpec(naming)
  }

@OptIn(ExperimentalSerializationApi::class)
fun Model.Union.toTypeSpec(naming: NamingStrategy): TypeSpec =
  TypeSpec.interfaceBuilder(naming.toUnionClassName(this))
    .addModifiers(KModifier.SEALED)
    .addAnnotation(annotationSpec<Serializable>())
    .addTypes(
      cases.map { case ->
        val model = case.model
        TypeSpec.classBuilder(naming.toUnionCaseName(model))
          .addModifiers(KModifier.VALUE)
          .addAnnotation(annotationSpec<JvmInline>())
          .primaryConstructor(
            FunSpec.constructorBuilder()
              .addParameter(ParameterSpec.builder("value", model.toTypeName(naming)).build())
              .build()
          )
          .addProperty(
            PropertySpec.builder("value", model.toTypeName(naming)).initializer("value").build()
          )
          .addSuperinterface(toTypeName(naming))
          .build()
      }
    )
    .addTypes(inline.mapNotNull { it.toTypeSpec(naming) })
    .addType(
      TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(
          ClassName("kotlinx.serialization", "KSerializer").parameterizedBy(toTypeName(naming))
        )
        .addProperty(
          PropertySpec.builder(
              "descriptor",
              ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
            )
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
                  context.name,
                  PolymorphicKind::class
                )
                .withIndent {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer(naming)
                    add(
                      "element(%S, $placeholder.descriptor)\n",
                      naming.toUnionCaseName(case.model),
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
            .addParameter("value", toTypeName(naming))
            .addCode(
              CodeBlock.builder()
                .add("when(value) {\n")
                .apply {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer(naming)
                    addStatement(
                      "is %T -> encoder.encodeSerializableValue($placeholder, value.value)",
                      ClassName.bestGuess(naming.toUnionCaseName(case.model)),
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
            .addParameter("decoder", ClassName("kotlinx.serialization.encoding", "Decoder"))
            .returns(toTypeName(naming))
            .addCode(
              CodeBlock.builder()
                .add(
                  "val json = decoder.decodeSerializableValue(%T.serializer())\n",
                  ClassName("kotlinx.serialization.json", "JsonElement")
                )
                .add("return attemptDeserialize(json,\n")
                .apply {
                  cases.forEach { case ->
                    val (placeholder, values) = case.model.serializer(naming)
                    add(
                      "Pair(%T::class) { %T(%T.decodeFromJsonElement($placeholder, json)) },\n",
                      ClassName.bestGuess(naming.toUnionCaseName(case.model)),
                      ClassName.bestGuess(naming.toUnionCaseName(case.model)),
                      ClassName("kotlinx.serialization.json", "Json"),
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

private inline fun <reified A : Annotation> annotationSpec(): AnnotationSpec =
  AnnotationSpec.builder(A::class).build()

private fun serialName(rawName: String): AnnotationSpec =
  annotationSpec<SerialName>().toBuilder().addMember("\"$rawName\"").build()

fun List<ParameterSpec>.sorted(): List<ParameterSpec> {
  val (required, optional) = partition { it.defaultValue == null }
  return required + optional
}

/*
 * Generating data classes with KotlinPoet!?
 * https://stackoverflow.com/questions/44483831/generate-data-class-with-kotlinpoet
 */
fun Model.Object.toTypeSpec(naming: NamingStrategy): TypeSpec =
  TypeSpec.classBuilder(naming.toObjectClassName(context))
    .addModifiers(KModifier.DATA)
    // We cannot serialize files, these are used for multipart requests, but we cannot check at this
    // point...
    // This occurs when request bodies are defined using top-level schemas.
    .apply {
      if (properties.none { it.model is Model.Binary })
        addAnnotation(annotationSpec<Serializable>())
    }
    .primaryConstructor(
      FunSpec.constructorBuilder()
        .addParameters(
          properties
            .map { prop ->
              val default = prop.model.default(naming)
              val isRequired = prop.isRequired && default != null
              ParameterSpec.builder(
                  naming.toParamName(context, prop.name),
                  prop.model.toTypeName(naming).copy(nullable = prop.isNullable)
                )
                .apply {
                  default?.let { defaultValue(it) }
                  if (isRequired) addAnnotation(annotationSpec<Required>())
                }
                .build()
            }
            .sorted()
        )
        .build()
    )
    .addProperties(
      properties.map { prop ->
        PropertySpec.builder(
            naming.toParamName(context, prop.name),
            prop.model.toTypeName(naming).copy(nullable = prop.isNullable)
          )
          .initializer(naming.toParamName(context, prop.name))
          .build()
      }
    )
    .addTypes(inline.mapNotNull { it.toTypeSpec(naming) })
    .build()

fun Model.toTypeName(
  naming: NamingStrategy,
  `package`: String = "io.github.nomisrev.openapi"
): TypeName =
  when (this) {
    is Model.Primitive.Boolean -> BOOLEAN
    is Model.Primitive.Double -> DOUBLE
    is Model.Primitive.Int -> INT
    is Model.Primitive.String -> STRING
    Model.Primitive.Unit -> UNIT
    is Collection.List -> LIST.parameterizedBy(value.toTypeName(naming, `package`))
    is Collection.Set -> SET.parameterizedBy(value.toTypeName(naming, `package`))
    is Collection.Map -> MAP.parameterizedBy(STRING, value.toTypeName(naming, `package`))
    Model.Binary -> ClassName(`package`, "UploadFile")
    Model.FreeFormJson -> ClassName("kotlinx.serialization.json", "JsonElement")
    is Model.Enum ->
      when (context) {
        is NamingContext.Named -> ClassName(`package`, naming.toEnumClassName(context))
        else -> ClassName.bestGuess(naming.toEnumClassName(context))
      }
    is Model.Object ->
      when (context) {
        is NamingContext.Named -> ClassName(`package`, naming.toObjectClassName(context))
        else -> ClassName.bestGuess(naming.toObjectClassName(context))
      }
    is Model.Union ->
      when (context) {
        is NamingContext.Named -> ClassName(`package`, naming.toUnionClassName(this))
        else -> ClassName.bestGuess(naming.toUnionClassName(this))
      }
  }

fun Model.Enum.toTypeSpec(naming: NamingStrategy): TypeSpec =
  when (this) {
    is Model.Enum.Closed -> toTypeSpec(naming)
    is Model.Enum.Open -> toTypeSpec(naming)
  }

fun Model.Enum.Closed.toTypeSpec(naming: NamingStrategy): TypeSpec {
  val rawToName = values.map { rawName -> Pair(rawName, naming.toEnumValueName(rawName)) }
  val isSimple = rawToName.all { (rawName, valueName) -> rawName == valueName }
  val enumName = naming.toEnumClassName(context)
  return TypeSpec.enumBuilder(enumName)
    .apply {
      if (!isSimple)
        primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
      rawToName.forEach { (rawName, valueName) ->
        if (isSimple) addEnumConstant(rawName)
        else
          addEnumConstant(
            valueName,
            TypeSpec.anonymousClassBuilder()
              .addAnnotation(serialName(rawName))
              .addSuperclassConstructorParameter("\"$rawName\"")
              .build()
          )
      }
    }
    .addAnnotation(annotationSpec<Serializable>())
    .build()
}

fun Model.Enum.Open.toTypeSpec(naming: NamingStrategy): TypeSpec {
  val rawToName = values.map { rawName -> Pair(rawName, naming.toEnumValueName(rawName)) }
  val enumName = naming.toEnumClassName(context)
  return TypeSpec.interfaceBuilder(enumName)
    .addModifiers(KModifier.SEALED)
    .addProperty(PropertySpec.builder("value", STRING).addModifiers(KModifier.ABSTRACT).build())
    .addAnnotation(annotationSpec<Serializable>())
    .addType(
      TypeSpec.classBuilder("Custom")
        .addModifiers(KModifier.VALUE)
        .addAnnotation(annotationSpec<JvmInline>())
        .primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
        .addProperty(
          PropertySpec.builder("value", STRING)
            .initializer("value")
            .addModifiers(KModifier.OVERRIDE)
            .build()
        )
        .addSuperinterface(ClassName.bestGuess(enumName))
        .build()
    )
    .addTypes(
      rawToName.map { (rawName, valueName) ->
        TypeSpec.objectBuilder(valueName)
          .addModifiers(KModifier.DATA)
          .addSuperinterface(ClassName.bestGuess(enumName))
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
          PropertySpec.builder("defined", LIST.parameterizedBy(ClassName.bestGuess(enumName)))
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
              ClassName("kotlinx.serialization", "KSerializer")
                .parameterizedBy(ClassName.bestGuess(enumName))
            )
            .addProperty(
              PropertySpec.builder(
                  "descriptor",
                  ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
                )
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
                      MemberName("kotlinx.serialization.descriptors", "PrimitiveSerialDescriptor"),
                      enumName,
                      ClassName("kotlinx.serialization.descriptors", "PrimitiveKind")
                    )
                    .build()
                )
                .build()
            )
            .addFunction(
              FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", ClassName("kotlinx.serialization.encoding", "Encoder"))
                .addParameter("value", ClassName.bestGuess(enumName))
                .addCode(
                  CodeBlock.builder().addStatement("encoder.encodeString(value.value)").build()
                )
                .build()
            )
            .addFunction(
              FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("decoder", ClassName("kotlinx.serialization.encoding", "Decoder"))
                .returns(ClassName.bestGuess(enumName))
                .addCode(
                  CodeBlock.builder()
                    .addStatement("val value = decoder.decodeString()")
                    .addStatement("return attemptDeserialize(value,")
                    .apply {
                      indent()
                      rawToName.forEach { (_, name) ->
                        addStatement(
                          "Pair(%T::class) { defined.find { it.value == value } },",
                          ClassName.bestGuess(name)
                        )
                      }
                      addStatement("Pair(Custom::class) { Custom(value) }")
                      unindent()
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

private val ListSerializer = MemberName("kotlinx.serialization.builtins", "ListSerializer")

private val SetSerializer = MemberName("kotlinx.serialization.builtins", "SetSerializer")

private val MapSerializer = MemberName("kotlinx.serialization.builtins", "MapSerializer")

private fun Model.serializer(naming: NamingStrategy): Pair<String, Array<Any>> {
  val values: MutableList<Any> = mutableListOf()
  fun Model.placeholder(): String =
    when (this) {
      is Collection.List -> {
        values.add(ListSerializer)
        "%M(${value.placeholder()})"
      }
      is Collection.Map -> {
        values.add(MapSerializer)
        "%M(${key.placeholder()}, ${value.placeholder()})"
      }
      is Collection.Set -> {
        values.add(SetSerializer)
        "%M(${value.placeholder()})"
      }
      is Model.Primitive -> {
        values.add(toTypeName(naming))
        values.add(MemberName("kotlinx.serialization.builtins", "serializer", isExtension = true))
        "%T.%M()"
      }
      else -> {
        values.add(toTypeName(naming))
        "%T.serializer()"
      }
    }

  return Pair(placeholder(), values.toTypedArray())
}
