package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Details of a deployment branch or tag policy.
 */
@Serializable
public data class DeploymentBranchPolicy(
  public val id: Long? = null,
  @SerialName("node_id")
  public val nodeId: String? = null,
  public val name: String? = null,
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
