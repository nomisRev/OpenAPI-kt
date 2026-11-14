package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DeploymentBranchPolicyNamePatternWithType(
  public val name: String,
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
