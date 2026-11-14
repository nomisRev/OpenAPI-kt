package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Provides details of a hosted runner image
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableActionsHostedRunnerPoolImage(
  public val id: String,
  @SerialName("size_gb")
  public val sizeGb: Long,
  @SerialName("display_name")
  public val displayName: String,
  public val source: Source,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val version: String? = null,
) {
  @Serializable
  public enum class Source(
    public val `value`: String,
  ) {
    @SerialName("github")
    Github("github"),
    @SerialName("partner")
    Partner("partner"),
    @SerialName("custom")
    Custom("custom"),
    ;
  }
}
