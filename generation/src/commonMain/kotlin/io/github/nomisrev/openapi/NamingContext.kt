package io.github.nomisrev.openapi

/**
 * A type name is decided by the context it belongs to,
 * there are  possible states.
 *   - inline bodies (Request), and inline responses (Response)
 *   - inline operation parameters
 *   - (inline | top-level) Object param `foo` inline schema => Type.Foo (nested)
 *   - (inline | top-level) Object param `foo` with top-level schema => top-level name
 *   - (inline | top-level) Object param `foo` with primitive => Primitive | List | Set | Map | JsonObject
 */
public sealed interface NamingContext {
  public val content: String

  public data class ClassName(override val content: String) : NamingContext

  public data class OperationParam(
    override val content: String,
    val operationId: String?,
    val postfix: String
  ) : NamingContext

  public sealed interface Param : NamingContext {
    public val outer: NamingContext
  }

  public data class Inline(override val content: String, override val outer: NamingContext) : Param
  public data class Ref(override val content: String, override val outer: NamingContext) : Param
}