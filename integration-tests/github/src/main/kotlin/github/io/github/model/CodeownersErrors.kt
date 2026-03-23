package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A list of errors found in a repo's CODEOWNERS file
 */
@JvmInline
@Serializable
public value class CodeownersErrors(
  public val errors: List<Errors>,
) {
  @Serializable
  public data class Errors(
    public val line: Long,
    public val column: Long,
    public val source: String? = null,
    public val kind: String,
    public val suggestion: String? = null,
    public val message: String,
    public val path: String,
  )
}
