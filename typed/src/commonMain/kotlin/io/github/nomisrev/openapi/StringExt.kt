package io.github.nomisrev.openapi

import io.ktor.http.HttpMethod

fun String.segments(): List<String> =
  replace(Regex("\\{.*?\\}"), "").split("/").filter { it.isNotEmpty() }

private fun HttpMethod.simpleName(): String =
  when (value.uppercase()) {
    "GET" -> "Get"
    "POST" -> "Post"
    "PUT" -> "Put"
    "PATCH" -> "Patch"
    "DELETE" -> "Delete"
    "HEAD" -> "Head"
    "OPTIONS" -> "Options"
    else -> value.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
  }

/**
 * Build a stable fallback operation identifier when operationId is missing.
 * Strategy: <lastNonVarSegment>[By<Var1>[And<VarN>]...]<Method>
 * Examples:
 *   - path="/admin/projects", method=POST -> "projectsPost"
 *   - path="/admin/projects/{id}", method=POST -> "projectsByIdPost"
 */
fun fallbackOperationId(path: String, method: HttpMethod): String {
  val parts = path.split('/').filter { it.isNotEmpty() }
  val nonVars = parts.filterNot { it.startsWith("{") && it.endsWith("}") }
  val base = nonVars.lastOrNull() ?: "root"
  val vars = parts.filter { it.startsWith("{") && it.endsWith("}") }.map { it.removePrefix("{").removeSuffix("}") }
  val by = if (vars.isEmpty()) "" else vars.joinToString(separator = "And", prefix = "By") { it.replace(Regex("[^A-Za-z0-9]"), "") }
  return "$base$by${method.simpleName()}"
}
