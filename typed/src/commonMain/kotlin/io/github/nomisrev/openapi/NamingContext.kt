package io.github.nomisrev.openapi

import kotlinx.serialization.Serializable

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
@Serializable
sealed interface NamingContext {
  /**
   * This tracks nested, which is important for generating the correct class names. For example,
   * /threads/{thread_id}/runs/{run_id}/submit_tool_outputs.
   */
  @Serializable
  data class Nested(val inner: NamingContext, val outer: NamingContext) : NamingContext

  fun nest(other: NamingContext): NamingContext = NamingContext.Nested(other, this)

  @Serializable
  data class ObjectProperty(val name: String) : GenerateName2

  @Serializable
  data object NestedModel : NamingContext

  @Serializable
  data class Reference(val name: String, val context: SchemaContext?) : NamingContext

  sealed interface GenerateName2 : NamingContext

  @Serializable
  data class GenerateUnionName(val name: String) : GenerateName2

  /**
   * These are the classes the API are generated in:
   * For `/threads/{thread_id}/runs/{run_id}/submit_tool_outputs.` it becomes:
   *   - `Nested(Path(SubmitToolOutputs), Nested(Path(Runs), Nested(Path(Threads))))`
   *
   * Which generates classes `Threads.Runs.SubmitToolOutputs`
   */
  @Serializable
  data class Path(val part: String) : NamingContext

  /**
   * An input param of a route. The param name is used to generate type names for inline schemas.
   * $OuterClass$MyOperationId$ParamName
   */
  @Serializable
  data class RouteParam(
    val name: String,
    val operationId: String
  ) : GenerateName2

  // TODO: reuse RouteParam with name == "body" or name == "request"
  @Serializable
  data class RouteBody(
    val name: String,
    val operationId: String
  ) : GenerateName2

  // TODO: Could be RouteParam with name == "response"
  @Serializable
  data class Response(val operationId: String) : GenerateName2
}
