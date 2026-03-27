package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * A custom tool that processes input using a specified format. Learn more about   [custom tools](/docs/guides/function-calling#custom-tools)
 */
@Serializable
public data class CustomToolParam(
  @Required
  public val type: Type = Type.Custom,
  public val name: String,
  public val description: String? = null,
  public val format: Format? = null,
  @SerialName("defer_loading")
  public val deferLoading: Boolean? = null,
) {
  /**
   * The input format for the custom tool. Default is unconstrained text.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Format {
    @Serializable
    @SerialName("text")
    public data object Text : Format

    /**
     * A grammar defined by the user.
     */
    @SerialName("grammar")
    @Serializable
    public data class Grammar(
      public val syntax: GrammarSyntax1,
      public val definition: String,
    ) : Format
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("custom")
    Custom("custom"),
    ;
  }
}
