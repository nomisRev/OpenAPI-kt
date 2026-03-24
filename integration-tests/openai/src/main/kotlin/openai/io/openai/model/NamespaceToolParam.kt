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
 * Groups function/custom tools under a shared namespace.
 */
@Serializable
public data class NamespaceToolParam(
  @Required
  public val type: Type = Type.Namespace,
  public val name: String,
  public val description: String,
  public val tools: List<Tools>,
) {
  /**
   * A function or custom tool that belongs to a namespace.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Tools {
    @Serializable
    @JvmInline
    @SerialName("FunctionToolParam")
    public value class FunctionToolParam(
      public val `value`: io.openai.model.FunctionToolParam,
    ) : Tools

    @Serializable
    @JvmInline
    @SerialName("CustomToolParam")
    public value class CustomToolParam(
      public val `value`: io.openai.model.CustomToolParam,
    ) : Tools
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("namespace")
    Namespace("namespace"),
    ;
  }
}
