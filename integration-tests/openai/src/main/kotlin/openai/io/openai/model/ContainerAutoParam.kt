package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public data class ContainerAutoParam(
  @Required
  public val type: Type = Type.ContainerAuto,
  @SerialName("file_ids")
  public val fileIds: List<String>? = null,
  @SerialName("memory_limit")
  public val memoryLimit: ContainerMemoryLimit? = null,
  @SerialName("network_policy")
  public val networkPolicy: NetworkPolicy? = null,
  public val skills: List<Skills>? = null,
) {
  /**
   * Network access policy for the container.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface NetworkPolicy {
    @Serializable
    @JvmInline
    @SerialName("ContainerNetworkPolicyDisabledParam")
    public value class ContainerNetworkPolicyDisabledParam(
      public val `value`: io.openai.model.ContainerNetworkPolicyDisabledParam,
    ) : NetworkPolicy

    @Serializable
    @JvmInline
    @SerialName("ContainerNetworkPolicyAllowlistParam")
    public value class ContainerNetworkPolicyAllowlistParam(
      public val `value`: io.openai.model.ContainerNetworkPolicyAllowlistParam,
    ) : NetworkPolicy
  }

  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Skills {
    @Serializable
    @JvmInline
    @SerialName("SkillReferenceParam")
    public value class SkillReferenceParam(
      public val `value`: io.openai.model.SkillReferenceParam,
    ) : Skills

    @Serializable
    @JvmInline
    @SerialName("InlineSkillParam")
    public value class InlineSkillParam(
      public val `value`: io.openai.model.InlineSkillParam,
    ) : Skills
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("container_auto")
    ContainerAuto("container_auto"),
    ;
  }
}
