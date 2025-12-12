package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.AnchorsAndAliases
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlin.jvm.JvmStatic
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode.ALWAYS
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

/** This is the root document object for the API specification. */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OpenAPI(
  @EncodeDefault(ALWAYS) public val openapi: String = "3.1.0",
  /** Provides metadata about the API. The metadata can be used by the clients if needed. */
  public val info: Info,
  /** An array of Server Objects, which provide connectivity information to a target server. */
  public val servers: List<Server> = listOf(Server(url = "/")),
  /** The available paths and operations for the API. */
  public val paths: Map<String, PathItem> = emptyMap(),
  /**
   * The incoming webhooks that MAY be received as part of this API and that the API consumer MAY
   * choose to implement. Closely related to the callbacks feature, this section describes requests
   * initiated other than by an API call, for example by an out of band registration. The key name
   * is a unique string to refer to each webhook, while the (optionally referenced) Path Item Object
   * describes a request that may be initiated by the API provider and the expected responses.
   */
  public val webhooks: Map<String, ReferenceOr<PathItem>> = emptyMap(),
  /** An element to hold various schemas for the specification. */
  public val components: Components = Components(),
  /**
   * A declaration of which security mechanisms can be used across the API. The list of values
   * includes alternative security requirement objects that can be used. Only one of the security
   * requirement objects need to be satisfied to authorize a request. Individual operations can
   * override this definition. To make security optional, an empty security requirement can be
   * included in the array.
   */
  public val security: List<SecurityRequirement> = emptyList(),
  /**
   * A list of tags used by the specification with additional metadata. The order of the tags can be
   * used to reflect on their order by the parsing tools. Not all tags that are used by the
   * 'Operation' Object must be declared. The tags that are not declared MAY be organized randomly
   * or based on the tools' logic. Each tag name in the list MUST be unique.
   */
  public val tags: Set<Tag> = emptySet(),
  /** Additional external documentation. */
  public val externalDocs: ExternalDocs? = null,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {
  public fun toJson(): String = Json.encodeToString(this)
  public fun toYaml(): String = Yaml.encodeToString(this)

  public fun toJsObject(): JsonObject = Json.encodeToJsonElement(this).jsonObject

  public companion object {
    public fun fromJson(json: String): OpenAPI = Json.decodeFromString(serializer(), json)

    public fun fromYaml(yaml: String): OpenAPI = Yaml.decodeFromString<OpenAPI>(yaml)

    @JvmStatic
    internal val Json: Json = Json {
      encodeDefaults = false
      prettyPrint = true
      ignoreUnknownKeys = true
      isLenient = true
    }

    @JvmStatic
    internal val Yaml: Yaml =
      Yaml(
        configuration =
          YamlConfiguration(
            encodeDefaults = false,
            strictMode = false,
            decodeEnumCaseInsensitive = true,
            anchorsAndAliases = AnchorsAndAliases.Permitted(),
          )
      )
  }
}
