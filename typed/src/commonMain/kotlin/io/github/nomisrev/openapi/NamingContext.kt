package io.github.nomisrev.openapi

/**
 * [NamingContext] is a critical part of how the models and routes are named. Following the context
 * is important to generate the correct class names for all schemas that are defined inline, rather
 * than named reference.
 *
 * An example, `/assistants/{assistant_id}/files` This would generate interfaces (and impl classes):
 * `Assistants.Files`. Any inline defined schema by an operation is generated as a nested type.
 * Let's say for `listAssistantFiles`, an `enum` for property `order` is generated as
 * `ListAssistantFilesOrder`. However, the FULL class name is
 * `Assistants.Files.ListAssistantFilesOrder`. NamingContext tracks this as:
 * ```kotlin
 * NamingContext.Nested(
 *   NamingContext.Nested("files", RouteParam("order", "ListAssistantFilesOrder")),
 *   Named("assistants")
 * )
 * ```
 *
 * The same technique is applied for all nesting, such that the correct names are generated.
 */
sealed interface NamingContext {
  /**
   * This tracks nested, which is important for generating the correct class names. For example,
   * /threads/{thread_id}/runs/{run_id}/submit_tool_outputs.
   */
  data class Nested(val inner: NamingContext, val outer: NamingContext) : NamingContext

  data class Named(val name: String) : NamingContext

  data class RouteParam(val name: String, val operationId: String, val postfix: String) :
    NamingContext

  data class RouteBody(val name: String, val postfix: String) : NamingContext
}
