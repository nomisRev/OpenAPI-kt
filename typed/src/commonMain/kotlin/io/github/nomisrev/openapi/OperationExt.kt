package io.github.nomisrev.openapi

import io.ktor.http.HttpMethod

/**
 * Extension function to get the operationId from an Operation, or generate a synthetic one if it's missing.
 * This ensures we have a consistent way to generate names when operationId is not available in the OpenAPI spec.
 *
 * The synthetic operationId is generated from the path and method, following the pattern:
 * - For path "/users/{id}/posts": "getUsersPosts" (for GET method)
 * - For path "/users/{id}/posts": "postUsersPosts" (for POST method)
 * - etc.
 *
 * @param path The path of the operation
 * @param method The HTTP method of the operation
 * @return The operationId if present, or a synthetic one if missing
 */
fun Operation.getOrCreateOperationId(path: String, method: HttpMethod): String =
  operationId ?: generateSyntheticOperationId(path, method)

/**
 * Generates a synthetic operationId from the path and method.
 * This is used when the operationId is missing in the OpenAPI spec.
 *
 * @param path The path of the operation
 * @param method The HTTP method of the operation
 * @return A synthetic operationId
 */
fun generateSyntheticOperationId(path: String, method: HttpMethod): String {
  // Extract path segments, removing empty segments and path parameters
  val segments = path.split("/")
    .filter { it.isNotEmpty() }
    .map { segment -> 
      // Remove curly braces from path parameters
      if (segment.startsWith("{") && segment.endsWith("}")) {
        segment.substring(1, segment.length - 1)
      } else {
        segment
      }
    }
  
  // Build the operationId: method name + capitalized path segments
  val capitalizedSegments = segments.joinToString("") { segment ->
    segment.replaceFirstChar { it.uppercase() }
  }
  
  return "${method.value.lowercase()}$capitalizedSegments"
}