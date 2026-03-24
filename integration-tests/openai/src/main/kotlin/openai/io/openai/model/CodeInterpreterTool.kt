package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * A tool that runs Python code to help generate a response to a prompt.
 *
 */
@Serializable
public data class CodeInterpreterTool(
  public val type: Type,
  public val container: Container,
) {
  /**
   * The code interpreter container. Can be a container ID or an object that
   * specifies uploaded file IDs to make available to your code, along with an
   * optional `memory_limit` setting.
   *
   */
  @Serializable(with = Container.Serializer::class)
  public sealed interface Container {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Container

    @Serializable
    @JvmInline
    public value class CaseAutoCodeInterpreterToolParam(
      public val `value`: AutoCodeInterpreterToolParam,
    ) : Container

    public object Serializer : KSerializer<Container> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.CodeInterpreterTool.Container", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseAutoCodeInterpreterToolParam", AutoCodeInterpreterToolParam.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Container {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseAutoCodeInterpreterToolParam::class to { CaseAutoCodeInterpreterToolParam(decodeFromJsonElement(AutoCodeInterpreterToolParam.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Container) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseAutoCodeInterpreterToolParam -> encoder.encodeSerializableValue(AutoCodeInterpreterToolParam.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("code_interpreter")
    CodeInterpreter("code_interpreter"),
    ;
  }
}
