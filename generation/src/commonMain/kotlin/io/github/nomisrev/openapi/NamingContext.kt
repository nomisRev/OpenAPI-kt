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
sealed interface NamingContext {
  val name: String

  data class Named(override val name: String) : NamingContext

  data class Nested(override val name: String, val outer: NamingContext) : NamingContext

  data class RouteParam(override val name: String, val operationId: String?, val postfix: String) :
    NamingContext
}
