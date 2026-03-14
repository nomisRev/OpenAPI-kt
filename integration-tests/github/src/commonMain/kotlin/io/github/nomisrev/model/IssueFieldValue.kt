package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class IssueFieldValue(
    @SerialName("issue_field_id") val issueFieldId: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("data_type") val dataType: DataType,
    val value: Value?,
    @SerialName("single_select_option") val singleSelectOption: SingleSelectOption? = null,
) {
    @Serializable
    enum class DataType {
        @SerialName("text")
        Text,
        @SerialName("single_select")
        SingleSelect,
        @SerialName("number")
        Number,
        @SerialName("date")
        Date;
    }

    @Serializable(with = Value.Serializer::class)
    sealed interface Value {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Value

        @Serializable
        @JvmInline
        value class CaseDouble(val value: Double) : Value

        @Serializable
        @JvmInline
        value class CaseLong(val value: Long) : Value

        object Serializer : KSerializer<Value> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.IssueFieldValue.Value", PolymorphicKind.SEALED) {
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

            override fun serialize(encoder: Encoder, value: Value) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
            }
        }
    }

    @Serializable
    data class SingleSelectOption(val id: Long, val name: String, val color: String)
}
