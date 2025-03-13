package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable
public data class Xml(
  /**
   * Replaces the name of the element/attribute used for the described schema property. When defined
   * within the @'OpenApiItems'@ (items), it will affect the name of the individual XML elements
   * within the list. When defined alongside type being array (outside the items), it will affect
   * the wrapping element and only if wrapped is true. If wrapped is false, it will be ignored.
   */
  val name: String,
  /** The URL of the namespace definition. Value SHOULD be in the form of a URL. */
  val namespace: String? = null,
  /** The prefix to be used for the name. */
  val prefix: String? = null,
  /**
   * Declares whether the property definition translates to an attribute instead of an element.
   * Default value is @False@.
   */
  val attribute: Boolean? = null,
  /**
   * MAY be used only for an array definition. Signifies whether the array is wrapped (for
   * example, @\<books\>\<book/\>\<book/\>\</books\>@) or unwrapped (@\<book/\>\<book/\>@). Default
   * value is
   *
   * @False@. The definition takes effect only when defined alongside type being array (outside the
   *   items).
   */
  val wrapped: Boolean? = null,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  val extensions: Map<String, JsonElement> = emptyMap(),
)
