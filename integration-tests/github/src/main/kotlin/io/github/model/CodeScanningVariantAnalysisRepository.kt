package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repository Identifier
 */
@Serializable
public data class CodeScanningVariantAnalysisRepository(
  public val id: Long,
  public val name: String,
  @SerialName("full_name")
  public val fullName: String,
  public val `private`: Boolean,
  @SerialName("stargazers_count")
  public val stargazersCount: Long,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
)
