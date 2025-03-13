package io.github.nomisrev.openapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes a single operation parameter.
 *
 * A unique parameter is defined by a combination of a [name] and [input].
 *
 * Parameter Locations There are four possible parameter locations specified by the in field:
 *
 * path - Used together with Path Templating, where the parameter value is actually part of the
 * operation's URL. This does not include the host or base path of the API. For example, in
 * /items/{itemId}, the path parameter is itemId. query - Parameters that are appended to the URL.
 * For example, in /items?id=###, the query parameter is id. header - Custom headers that are
 * expected as part of the request. Note that RFC7230 states header names are case insensitive.
 * cookie - Used to pass a specific cookie value to the API.
 */
@Serializable
public data class Parameter(
  /**
   * The name of the parameter. Parameter names are case sensitive. If in is "path", the name field
   * MUST correspond to a template expression occurring within the path field in the Paths Object.
   * See [Path Templating](https://swagger.io/specification/#path-templating) for further
   * information. If in is "header" and the name field is "Accept", "Content-Type" or
   * "Authorization", the parameter definition SHALL be ignored. For all other cases, the name
   * corresponds to the parameter name used by the in property.
   */
  public val name: String,
  @SerialName("in")
  /** The input of the parameter. */
  public val input: Input,
  /**
   * A brief description of the parameter. This could contain examples of use. CommonMark syntax MAY
   * be used for rich text representation.
   */
  public val description: String? = null,
  /**
   * Determines whether this parameter is mandatory. If the parameter location is "path", this
   * property is REQUIRED and its value MUST be true. Otherwise, the property MAY be included and
   * its default value is false.
   */
  public val required: Boolean = input == Input.Path,
  /**
   * Specifies that a parameter is deprecated and SHOULD be transitioned out of usage. Default value
   * is false.
   */
  public val deprecated: Boolean = false,
  /**
   * Sets the ability to pass empty-valued parameters. This is valid only for query parameters and
   * allows sending a parameter with an empty value. Default value is false. If style is used, and
   * if behavior is n/a (cannot be serialized), the value of allowEmptyValue SHALL be ignored. Use
   * of this property is NOT RECOMMENDED, as it is likely to be removed in a later revision.
   */
  public val allowEmptyValue: Boolean = false,
  /**
   * Determines whether the parameter value SHOULD allow reserved characters, as defined by RFC3986
   * :/?#[]@!$&'()*+,;= to be included without percent-encoding. This property only applies to
   * parameters with an in value of query. The default value is false.
   */
  public val allowReserved: Boolean = false,
  /** The schema defining the type used for the parameter. */
  public val schema: ReferenceOr<Schema>? = null,
  /**
   * Describes how the parameter value will be serialized depending on the type of the parameter
   * value. Default values (based on value of _paramIn): for ParamQuery - StyleForm; for ParamPath -
   * StyleSimple; for ParamHeader - StyleSimple; for ParamCookie - StyleForm.
   */
  public val style: Style? = null,
  public val explode: Boolean? = null,
  /**
   * Example of the parameter's potential value. The example SHOULD match the specified schema and
   * encoding properties if present. The example field is mutually exclusive of the examples field.
   * Furthermore, if referencing a schema that contains an example, the example value SHALL override
   * the example provided by the schema. To represent examples of media types that cannot naturally
   * be represented in JSON or YAML, a string value can contain the example with escaping where
   * necessary.
   */
  public val example: ExampleValue? = null,
  /**
   * Examples of the parameter's potential value. Each example SHOULD contain a value in the correct
   * format as specified in the parameter encoding. The _paramExamples field is mutually exclusive
   * of the _paramExample field. Furthermore, if referencing a schema that contains an example, the
   * examples value SHALL override the example provided by the schema.
   */
  public val examples: Map<String, ReferenceOr<Example>>? = emptyMap(),
) {
  init {
    if (input == Input.Path)
      require(required) {
        "${required}Determines whether this parameter is mandatory. If the parameter location is \"path\", this property is REQUIRED and its value MUST be true. Otherwise, the property MAY be included and its default value is false."
      }
  }

  @Serializable
  public enum class Input(public val value: String) {
    @SerialName("query") Query("query"),
    @SerialName("header") Header("header"),
    @SerialName("path") Path("path"),
    @SerialName("cookie") Cookie("cookie"),
  }
}
