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

/**
 * A tool that allows the model to execute shell commands.
 */
@Serializable
public data class FunctionShellToolParam(
  @Required
  public val type: Type = Type.Shell,
  public val environment: Environment? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Environment {
    @SerialName("container_auto")
    @Serializable
    public data class ContainerAuto(
      @SerialName("file_ids")
      public val fileIds: List<String>? = null,
      @SerialName("memory_limit")
      public val memoryLimit: ContainerMemoryLimit? = null,
      @SerialName("network_policy")
      public val networkPolicy: NetworkPolicy? = null,
      public val skills: List<Skills>? = null,
    ) : Environment {
      /**
       * Network access policy for the container.
       */
      @OptIn(ExperimentalSerializationApi::class)
      @JsonClassDiscriminator("type")
      @Serializable
      public sealed interface NetworkPolicy {
        @Serializable
        @SerialName("disabled")
        public data object Disabled : NetworkPolicy

        @SerialName("allowlist")
        @Serializable
        public data class Allowlist(
          @SerialName("allowed_domains")
          public val allowedDomains: List<String>,
          @SerialName("domain_secrets")
          public val domainSecrets: List<ContainerNetworkPolicyDomainSecretParam>? = null,
        ) : NetworkPolicy
      }

      @OptIn(ExperimentalSerializationApi::class)
      @JsonClassDiscriminator("type")
      @Serializable
      public sealed interface Skills {
        @SerialName("skill_reference")
        @Serializable
        public data class SkillReference(
          @SerialName("skill_id")
          public val skillId: String,
          public val version: String? = null,
        ) : Skills

        @SerialName("inline")
        @Serializable
        public data class Inline(
          public val name: String,
          public val description: String,
          public val source: InlineSkillSourceParam,
        ) : Skills
      }
    }

    @JvmInline
    @SerialName("local")
    @Serializable
    public value class Local(
      public val skills: List<LocalSkillParam>? = null,
    ) : Environment

    @JvmInline
    @SerialName("container_reference")
    @Serializable
    public value class ContainerReference(
      @SerialName("container_id")
      public val containerId: String,
    ) : Environment
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("shell")
    Shell("shell"),
    ;
  }
}
