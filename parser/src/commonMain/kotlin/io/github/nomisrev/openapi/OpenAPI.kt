package io.github.nomisrev.openapi

import kotlin.jvm.JvmStatic
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode.ALWAYS
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.yaml.snakeyaml.Yaml

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

  public fun operationsByTag(): Map<String, List<Operation>> = TODO()

  //    tags.associateBy(Tag::name) { tag ->
  //      operations().filter { it.tags.contains(tag.name) }
  //    }

  public fun withComponents(components: Components): OpenAPI = copy(components = components)

  public fun withPathItem(path: String, pathItem: PathItem): OpenAPI {
    val newPathItem =
      when (val existing = paths[path]) {
        null -> pathItem
        else -> existing + pathItem
      }

    return copy(paths = paths + Pair(path, newPathItem))
  }

  public fun withServers(servers: List<Server>): OpenAPI = copy(servers = this.servers + servers)

  public fun withServers(vararg servers: Server): OpenAPI =
    copy(servers = this.servers + servers.toList())

  public fun withServer(server: Server): OpenAPI = copy(servers = this.servers + listOf(server))

  public fun withTags(tags: Set<Tag>): OpenAPI = copy(tags = this.tags + tags)

  public fun withTag(tag: Tag): OpenAPI = copy(tags = this.tags + setOf(tag))

  public fun withExternalDocs(externalDocs: ExternalDocs): OpenAPI =
    copy(externalDocs = externalDocs)

  public fun withExtensions(extensions: Map<String, JsonElement>): OpenAPI =
    copy(extensions = this.extensions + extensions)

  public fun toJson(): String = Json.encodeToString(this)

  public fun toJsObject(): JsonObject = Json.encodeToJsonElement(this).jsonObject

  public companion object {
    public fun fromJson(json: String): OpenAPI = Json.decodeFromString(serializer(), json)

    public fun fromYaml(yaml: String): OpenAPI {
      val json = Yaml().load<Any?>(yaml).toJsonElement()
      return Json.decodeFromJsonElement(serializer(), json)
    }

    @JvmStatic
    private val Json: Json = Json {
      encodeDefaults = false
      prettyPrint = true
      // TODO: Should this somehow be configurable?
      //   This allows incorrect OpenAPI to be parsed,
      //   such as OpenAPI Generator skips validation.
      ignoreUnknownKeys = true
      isLenient = true
    }
  }
}

private fun Any?.toJsonElement(): JsonElement =
  when (this) {
    is List<*> -> JsonArray(map { it.toJsonElement() })
    is Map<*, *> ->
      @Suppress("UNCHECKED_CAST")
      JsonObject((this as Map<String, Any?>).mapValues { (_, v) -> v.toJsonElement() })
    null -> JsonNull
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    else -> throw IllegalArgumentException("Unsupported type: ${this::class.simpleName}")
  }
