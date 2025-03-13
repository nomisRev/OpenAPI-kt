package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** An object representing a Server. */
@Serializable
public data class Server(
  /**
   * A URL to the target host. This URL supports Server Variables and MAY be relative, to indicate
   * that the host location is relative to the location where the OpenAPI document is being served.
   * Variable substitutions will be made when a variable is named in {brackets}.
   */
  public val url: String,
  /**
   * An optional string describing the host designated by the URL. CommonMark syntax MAY be used for
   * rich text representation.
   */
  public val description: String? = null,
  /**
   * A map between a variable name and its value. The value is used for substitution in the server's
   * URL template.
   */
  public val variables: Map<String, Variable>? = null,
  public val extensions: Map<String, JsonElement>? = emptyMap(),
) {
  /** An object representing a Server Variable for server URL template substitution. */
  @Serializable
  public data class Variable(
    /**
     * An enumeration of string values to be used if the substitution options are from a limited
     * set.
     */
    public val enum: List<String>? = null,
    /**
     * The default value to use for substitution, which SHALL be sent if an alternate value is not
     * supplied. Note this behavior is different than the Schema Object's treatment of default
     * values, because in those cases parameter values are optional. If the enum is defined, the
     * value SHOULD exist in the enum's values.
     */
    public val default: String,
    /**
     * An optional description for the server variable. CommonMark syntax MAY be used for rich text
     * representation.
     */
    public val description: String? = null,
    public val extensions: Map<String, JsonElement>? = emptyMap(),
  )
}
