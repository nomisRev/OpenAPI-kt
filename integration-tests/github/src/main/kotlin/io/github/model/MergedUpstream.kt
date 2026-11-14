package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Results of a successful merge upstream request
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MergedUpstream(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: String? = null,
  @SerialName("merge_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mergeType: MergeType? = null,
  @SerialName("base_branch")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val baseBranch: String? = null,
) {
  @Serializable
  public enum class MergeType(
    public val `value`: String,
  ) {
    @SerialName("merge")
    Merge("merge"),
    @SerialName("fast-forward")
    FastForward("fast-forward"),
    @SerialName("none")
    None("none"),
    ;
  }
}
