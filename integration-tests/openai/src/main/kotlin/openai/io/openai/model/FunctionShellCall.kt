package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A tool call that executes one or more shell commands in a managed environment.
 */
@Serializable
public data class FunctionShellCall(
  @Required
  public val type: Type = Type.ShellCall,
  public val id: String,
  @SerialName("call_id")
  public val callId: String,
  public val action: FunctionShellAction,
  public val status: LocalShellCallStatus,
  public val environment: Environment?,
  @SerialName("created_by")
  public val createdBy: String? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Environment {
    @Serializable
    @JvmInline
    @SerialName("LocalEnvironmentResource")
    public value class LocalEnvironmentResource(
      public val `value`: io.openai.model.LocalEnvironmentResource,
    ) : Environment

    @Serializable
    @JvmInline
    @SerialName("ContainerReferenceResource")
    public value class ContainerReferenceResource(
      public val `value`: io.openai.model.ContainerReferenceResource,
    ) : Environment
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("shell_call")
    ShellCall("shell_call"),
    ;
  }
}
