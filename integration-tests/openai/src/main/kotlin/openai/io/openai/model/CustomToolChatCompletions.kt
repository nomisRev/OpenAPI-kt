package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * A custom tool that processes input using a specified format.
 *
 */
@Serializable
public data class CustomToolChatCompletions(
  public val type: Type,
  public val custom: Custom,
) {
  /**
   * Properties of the custom tool.
   *
   */
  @Serializable
  public data class Custom(
    public val name: String,
    public val description: String? = null,
    public val format: Format? = null,
  ) {
    /**
     * The input format for the custom tool. Default is unconstrained text.
     *
     */
    @Serializable(with = Format.Serializer::class)
    public sealed interface Format {
      /**
       * Unconstrained free-form text.
       */
      @JvmInline
      @Serializable
      public value class Text(
        public val type: Type,
      ) : Format {
        @Serializable
        public enum class Type(
          public val `value`: String,
        ) {
          @SerialName("text")
          Text("text"),
          ;
        }
      }

      /**
       * A grammar defined by the user.
       */
      @Serializable
      public data class Grammar(
        public val type: Type,
        public val grammar: Grammar,
      ) : Format {
        /**
         * Your chosen grammar.
         */
        @Serializable
        public data class Grammar(
          public val definition: String,
          public val syntax: Syntax,
        ) {
          @Serializable
          public enum class Syntax(
            public val `value`: String,
          ) {
            @SerialName("lark")
            Lark("lark"),
            @SerialName("regex")
            Regex("regex"),
            ;
          }
        }

        @Serializable
        public enum class Type(
          public val `value`: String,
        ) {
          @SerialName("grammar")
          Grammar("grammar"),
          ;
        }
      }

      public object Serializer : KSerializer<Format> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.openai.model.CustomToolChatCompletions.Custom.Format", PolymorphicKind.SEALED) {
          element("Text", Text.serializer().descriptor)
          element("Grammar", Grammar.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Format {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            Grammar::class to { decodeFromJsonElement(Grammar.serializer(), it) },
            Text::class to { decodeFromJsonElement(Text.serializer(), it) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Format) {
          when(value) {
            is Text -> encoder.encodeSerializableValue(Text.serializer(), value)
            is Grammar -> encoder.encodeSerializableValue(Grammar.serializer(), value)
          }
        }
      }
    }
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
