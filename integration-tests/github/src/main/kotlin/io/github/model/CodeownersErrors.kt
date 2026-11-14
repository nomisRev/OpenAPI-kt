package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * A list of errors found in a repo's CODEOWNERS file
 */
@Serializable
public data class CodeownersErrors(
  public val errors: List<Errors>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Errors(
    public val line: Long,
    public val column: Long,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val source: String? = null,
    public val kind: String,
    public val suggestion: String? = null,
    public val message: String,
    public val path: String,
  )
}
