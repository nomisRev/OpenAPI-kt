package io.github.model

import kotlin.Double
import kotlin.Long
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
 * A value assigned to an issue field
 */
@Serializable
public data class IssueFieldValue(
  @SerialName("issue_field_id")
  public val issueFieldId: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("data_type")
  public val dataType: DataType,
  public val `value`: Value?,
  @SerialName("single_select_option")
  public val singleSelectOption: SingleSelectOption? = null,
) {
  @Serializable
  public enum class DataType(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    @SerialName("single_select")
    SingleSelect("single_select"),
    @SerialName("number")
    Number("number"),
    @SerialName("date")
    Date("date"),
    ;
  }

  /**
   * Details about the selected option (only present for single_select fields)
   */
  @Serializable
  public data class SingleSelectOption(
    public val id: Long,
    public val name: String,
    public val color: String,
  )

  /**
   * The value of the issue field
   */
  @Serializable(with = Value.Serializer::class)
  public sealed interface Value {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Value

    @Serializable
    @JvmInline
    public value class CaseDouble(
      public val `value`: Double,
    ) : Value

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : Value

    public object Serializer : KSerializer<Value> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.IssueFieldValue.Value", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseDouble", Double.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Value {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Value) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
        }
      }
    }
  }
}
