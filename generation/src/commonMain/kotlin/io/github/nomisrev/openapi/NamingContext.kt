package io.github.nomisrev.openapi

/**
 * [NamingContext] is a critical part of how the models and routes are named.
 * Following the context is important to generate the correct class names for
 * all schemas that are defined inline, rather than named reference.
 */
sealed interface NamingContext {
  /**
   * This tracks nested, which is important for generating the correct class names.
   * For example, /threads/{thread_id}/runs/{run_id}/submit_tool_outputs.
   *
   *
   */
  data class Nested(val inner: NamingContext, val outer: NamingContext) : NamingContext

  data class Named(val name: String) : NamingContext
  data class RouteParam(val name: String, val operationId: String, val postfix: String) :
    NamingContext

  data class RouteBody(val name: String, val postfix: String) : NamingContext
}
