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
    @Serializable
    @JvmInline
    @SerialName("ContainerAutoParam")
    public value class ContainerAutoParam(
      public val `value`: io.openai.model.ContainerAutoParam,
    ) : Environment

    @Serializable
    @JvmInline
    @SerialName("LocalEnvironmentParam")
    public value class LocalEnvironmentParam(
      public val `value`: io.openai.model.LocalEnvironmentParam,
    ) : Environment

    @Serializable
    @JvmInline
    @SerialName("ContainerReferenceParam")
    public value class ContainerReferenceParam(
      public val `value`: io.openai.model.ContainerReferenceParam,
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
