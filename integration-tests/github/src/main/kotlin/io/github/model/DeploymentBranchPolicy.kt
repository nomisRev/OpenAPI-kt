package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Details of a deployment branch or tag policy.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DeploymentBranchPolicy(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: Type? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("branch")
    Branch("branch"),
    @SerialName("tag")
    Tag("tag"),
    ;
  }
}
