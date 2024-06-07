package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.NamingContext.TopLevelSchema
import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import io.github.nomisrev.openapi.generation.NamingStrategy
import kotlin.io.path.Path
import okio.FileSystem
import okio.Path.Companion.toPath

suspend fun main() {
  //  FileSystem.SYSTEM.generateModel("openai.json")
  val rawSpec = FileSystem.SYSTEM.read("openai.json".toPath()) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  val root = openAPI.routes()
  root.endpoints.forEach { api ->
    FileSpec.builder("io.github.nomisrev.openapi", api.name)
      .addType(api.toCode(DefaultNamingStrategy))
      .build()
      .writeTo(Path("../example/build/generated/openapi/src/commonMain/kotlin/"))
  }
  FileSpec.builder("io.github.nomisrev.openapi", "OpenAPI")
    .addType(TypeSpec.interfaceBuilder("OpenAPI").build())
    .build()
    .writeTo(Path("../example/build/generated/openapi/src/commonMain/kotlin/"))
}

fun TypeSpec.Builder.addProperty(api: API, naming: NamingStrategy) {
  val className = naming.toObjectClassName(TopLevelSchema(api.name))
  addProperty(api.name, ClassName.bestGuess(className))
}

fun API.toCode(naming: NamingStrategy): TypeSpec =
  TypeSpec.interfaceBuilder(naming.toObjectClassName(TopLevelSchema(name)))
    .addFunctions(routes.map { it.toFun(naming).abstract() })
    .apply {
      nested.forEach { api ->
        addType(api.toCode(naming))
        addProperty(api, naming)
      }
    }
    .build()

fun FunSpec.abstract(): FunSpec = toBuilder().addModifiers(KModifier.ABSTRACT).build()

fun Route.toFun(naming: NamingStrategy): FunSpec =
  FunSpec.builder(naming.toFunctionName(TopLevelSchema(operation.operationId!!)))
    .apply {
      input.forEach { input ->
        addParameter(
          naming.toFunctionName(TopLevelSchema(input.name)),
          ClassName.bestGuess(naming.typeName(input.type))
        )
      }
    }
    .returns(returnType(naming))
    .build()

// TODO generate an ADT to properly support FULL return types
fun Route.returnType(naming: NamingStrategy): ClassName {
  val success =
    returnType.types.toSortedMap { s1, s2 -> s1.code.compareTo(s2.code) }.entries.first()
  val className = naming.typeName(success.value.type)
  return ClassName.bestGuess("io.github.nomisrev.openapi.$className")
}
