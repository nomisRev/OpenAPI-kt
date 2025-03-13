package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * The object provides metadata about the API. The metadata MAY be used by the clients if needed,
 * and MAY be presented in editing or documentation generation tools for convenience.
 */
@Serializable
public data class Info(
  /** The title of the API. */
  public val title: String,
  /**
   * A short description of the API. [CommonMark syntax](https://spec.commonmark.org/) MAY be used
   * for rich text representation.
   */
  public val description: String? = null,
  /** A URL to the Terms of Service for the API. MUST be in the format of a URL. */
  public val termsOfService: String? = null,
  /** The contact information for the exposed API. */
  public val contact: Contact? = Contact(),
  /** The license information for the exposed API. */
  public val license: License? = null,
  /**
   * The version of the OpenAPI document (which is distinct from the OpenAPI Specification version
   * or the API implementation version).
   */
  public val version: String,
  /**
   * Any additional external documentation for this OpenAPI document. The key is the name of the
   * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
   * [JsonPrimitive], [JsonArray] or [JsonObject].
   */
  public val extensions: Map<String, JsonElement> = emptyMap(),
) {
  /** Contact information for the exposed API. */
  @Serializable
  public data class Contact(
    /** The identifying name of the contact person/organization. */
    public val name: String? = null,
    /** The URL pointing to the contact information. MUST be in the format of a URL. */
    public val url: String? = null,
    /**
     * The email address of the contact person/organization. MUST be in the format of an email
     * address.
     */
    public val email: String? = null,
    /**
     * Any additional external documentation for this OpenAPI document. The key is the name of the
     * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
     * [JsonPrimitive], [JsonArray] or [JsonObject].
     */
    public val extensions: Map<String, JsonElement> = emptyMap(),
  )

  /** License information for the exposed API. */
  @Serializable
  public data class License(
    /** The license name used for the API. */
    public val name: String,
    /** A URL to the license used for the API. MUST be in the format of a URL. */
    public val url: String? = null,
    private val identifier: String? = null,
    /**
     * Any additional external documentation for this OpenAPI document. The key is the name of the
     * extension (beginning with x-), and the value is the data. The value can be a [JsonNull],
     * [JsonPrimitive], [JsonArray] or [JsonObject].
     */
    public val extensions: Map<String, JsonElement> = emptyMap(),
  )
}
