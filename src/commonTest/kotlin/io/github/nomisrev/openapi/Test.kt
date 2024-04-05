package io.github.nomisrev.openapi

import com.goncalossilva.resources.Resource
import io.github.nomisrev.openapi.OpenAPI.Companion.JSON_FLEXIBLE
import kotlin.test.Test

class Test {

  @Test
  fun readOpenAI() {
    val openAPI = JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/openai.json").readText(),
    )
    openAPI.operationsByTag().forEach { (tag, operations) ->
      val code = """
        |interface ${tag.toPascalCase()} {
        |${operations.toSignature(openAPI)}
        |}
      """.trimMargin()
      println(code)
    }
  }
}

private const val refPrefix = "#/components/schemas/"

fun ReferenceOr<Schema>.resolve(openAPI: OpenAPI): Pair<String, Schema> =
  when (this) {
    is ReferenceOr.Value -> Pair(
      requireNotNull(value.type?.toKotlinType()) { "When is this null?? $this" },
      value
    )

    is ReferenceOr.Reference -> {
      val typeName = ref.drop(refPrefix.length)
      val schema = (openAPI.components.schemas.getValue(typeName) as ReferenceOr.Value<Schema>).value
      Pair(typeName, schema)
    }
  }

fun Schema.Companion.Type.toKotlinType(): String =
  when (this) {
    // TODO Multiple types are not supported yet.
    is Schema.Companion.Type.Array -> TODO()
    Schema.Companion.Type.Basic.Boolean -> "Boolean"
    Schema.Companion.Type.Basic.Integer -> "Int"
    Schema.Companion.Type.Basic.Number -> "Double"
    Schema.Companion.Type.Basic.String -> "String"
    Schema.Companion.Type.Basic.Null -> "Unit"
    Schema.Companion.Type.Basic.Object -> TODO()
    Schema.Companion.Type.Basic.Array -> TODO()
  }

fun ReferenceOr<Response>.resolve(openAPI: OpenAPI): Response =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> (openAPI.components.responses.getValue(ref) as ReferenceOr.Value<Response>).value
  }

fun ReferenceOr<Parameter>.resolve(openAPI: OpenAPI): Parameter =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> (openAPI.components.parameters.getValue(ref) as ReferenceOr.Value<Parameter>).value
  }

fun ReferenceOr<RequestBody>.resolve(openAPI: OpenAPI): RequestBody =
  when (this) {
    is ReferenceOr.Value -> value
    is ReferenceOr.Reference -> (openAPI.components.requestBodies.getValue(ref) as ReferenceOr.Value<RequestBody>).value
  }

fun List<Operation>.toSignature(openAPI: OpenAPI): String =
  joinToString(prefix = "  ", separator = "\n  ") { it.toSignature(openAPI) }

fun Operation.toSignature(openAPI: OpenAPI): String {
  return "suspend fun $operationId(${allParameters(openAPI)}): ${responseType(openAPI)}"
}

fun Schema.nullable(): String =
  if (nullable == true) "?" else ""

// TODO support multiple responses, or formats??
fun Operation.responseType(openAPI: OpenAPI): String =
  when (responses.responses.size) {
    0 -> "Unit"
    1 -> {
      val response = responses.responses.values.first().resolve(openAPI)
      when {
        response.content.contains("application/octet-stream") -> "HttpResponse"
        response.content.contains("application/json") -> {
          val mediaType =
            requireNotNull(response.content["application/json"]) { "Only JSON content type is supported atm. ${response.content}" }
          mediaType.schema?.resolve(openAPI)?.let { (typeName, schema) ->
            "$typeName${schema.nullable()}" // default param
          } ?: "JsonElement" // If we don't know the schema for Json, use freeform JsonElement.
        }

        else -> throw IllegalStateException("Response: $response")
      }
    }

    else -> throw IllegalStateException("We don't support multiple formats yet: $responses")
  }

fun Operation.allParameters(openAPI: OpenAPI): String {
  val parameters = parameters.mapNotNull {
    val parameter = it.resolve(openAPI)
    parameter.schema?.resolve(openAPI)?.let { (typeName, schema) ->
      "val ${parameter.name.toCamelCase()}: ${typeName + if (schema.nullable == true) "?" else ""}"
    }
  }
  return (requestBody.toParam(openAPI) + parameters).joinToString()
}

// "multipart/form-data"
fun ReferenceOr<RequestBody>?.toParam(openAPI: OpenAPI): List<String> =
  if (this == null) emptyList()
  else {
    val requestBody: RequestBody = resolve(openAPI)
    when {
      requestBody.content.contains("application/json") -> {
        listOfNotNull(requestBody.content["application/json"]!!.schema?.resolve(openAPI)
          ?.let { (typeName, schema) ->
            "val ${typeName.decapitalize()}: ${typeName + if (schema.nullable == true) "?" else ""}"
          })
      }

      requestBody.content.contains("multipart/form-data") -> {
        val schema = requestBody.content["multipart/form-data"]!!.schema?.resolve(openAPI)!!.second
        schema.properties.entries.map { (key, value) ->
          val (typeName, schema) = value.resolve(openAPI)
          Argument(key, typeName, schema.nullable == true, schema.description, null/*TODO*/)
        }
        requestBody.content["multipart/form-data"]!!.schema?.resolve(openAPI)
          ?.let { (typeName, schema) ->
            "val ${typeName.decapitalize()}: ${typeName + if (schema.nullable == true) "?" else ""}"
          }
        TODO()
      }

      else -> emptyList<String>()
    }
  }

data class Argument(
  val name: String,
  val type: String,
  val required: Boolean,
  val description: String? = null,
  val defaultValue: String? = null
) {
  override fun toString(): String {
    val nullable = if (required) "" else "?"
    val default = defaultValue?.let { " = $it" } ?: ""
    return "val $name: $type$nullable$default"
  }
}

fun OpenAPI.operationsByTag(): Map<String, List<Operation>> =
  tags.associateBy(Tag::name) { tag ->
    allOperations().filter { it.tags.contains(tag.name) }
  }

fun OpenAPI.allOperations(): List<Operation> =
  paths.values.flatMap { pathItem ->
    listOfNotNull(
      pathItem.get,
      pathItem.put,
      pathItem.post,
      pathItem.delete,
      pathItem.options,
      pathItem.head,
      pathItem.patch,
      pathItem.trace
    )
  }

fun String.decapitalize(): String =
  replaceFirstChar { it.lowercase() }

fun String.capitalize(): String =
  replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun String.toPascalCase(): String {
  val words = split("_", "-")
  return when (words.size) {
    1 -> words[0]
    else -> buildString {
      append(words[0].capitalize())
      for (i in 1 until words.size) {
        append(words[i].capitalize())
      }
    }
  }
}

fun String.toCamelCase(): String {
  val words = split("_")
  return when (words.size) {
    1 -> words[0]
    else -> buildString {
      append(words[0].decapitalize())
      for (i in 1 until words.size) {
        append(words[i].capitalize())
      }
    }
  }
}
