package io.github.nomisrev.openapi

/**
 * A type name needs to be generated using the surrounding context.
 * - inline bodies (postfix `Request`)
 * - inline responses (postfix `Response`)
 * - inline operation parameters,
 * - (inline | top-level) Object param `foo` inline schema => Type.Foo (nested)
 * - (inline | top-level) Object param `foo` with top-level schema => top-level name
 * - (inline | top-level) Object param `foo` with primitive => Primitive | List | Set | Map |
 *   JsonObject
 */
public sealed interface NamingContext {
  public val name: String

  public data class Named(override val name: String) : NamingContext

  public data class RouteParam(
    override val name: String,
    val operationId: String?,
    val postfix: String
  ) : NamingContext

  public sealed interface Param : NamingContext {
    public val outer: NamingContext
  }

  public data class Inline(override val name: String, override val outer: NamingContext) : Param

  public data class Ref(override val name: String, override val outer: NamingContext) : Param
}
