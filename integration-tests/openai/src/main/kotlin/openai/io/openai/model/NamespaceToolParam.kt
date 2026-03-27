package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
    @SerialName("function")
    @Serializable
    public data class Function(
      public val name: String,
      public val description: String? = null,
      public val parameters: EmptyModelParam? = null,
      public val strict: Boolean? = null,
    ) : Tools

    /**
     * A custom tool that processes input using a specified format. Learn more about   [custom tools](/docs/guides/function-calling#custom-tools)
     */
    @SerialName("custom")
    @Serializable
    public data class Custom(
      public val name: String,
      public val description: String? = null,
      public val format: Format? = null,
      @SerialName("defer_loading")
      public val deferLoading: Boolean? = null,
    ) : Tools {
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
    }
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
