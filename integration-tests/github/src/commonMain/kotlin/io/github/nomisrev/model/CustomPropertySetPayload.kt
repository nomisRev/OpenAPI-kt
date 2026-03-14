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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class CustomPropertySetPayload(
    @SerialName("value_type") val valueType: ValueType,
    val required: Boolean? = null,
    @SerialName("default_value") val defaultValue: DefaultValue? = null,
    val description: String? = null,
    @SerialName("allowed_values") val allowedValues: List<String>? = null,
    @SerialName("values_editable_by") val valuesEditableBy: ValuesEditableBy? = null,
    @SerialName("require_explicit_values") val requireExplicitValues: Boolean? = null,
) {
    @Serializable
    enum class ValueType {
        @SerialName("string")
        String,
        @SerialName("single_select")
        SingleSelect,
        @SerialName("multi_select")
        MultiSelect,
        @SerialName("true_false")
        TrueFalse,
        @SerialName("url")
        Url;
    }

    @Serializable(with = DefaultValue.Serializer::class)
    sealed interface DefaultValue {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : DefaultValue

        @Serializable
        @JvmInline
        value class CaseStrings(val value: List<String>) : DefaultValue

        object Serializer : KSerializer<DefaultValue> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.CustomPropertySetPayload.DefaultValue", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                }

            override fun deserialize(decoder: Decoder): DefaultValue {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: DefaultValue) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
            }
        }
    }

    @Serializable
    enum class ValuesEditableBy {
        @SerialName("org_actors") OrgActors, @SerialName("org_and_repo_actors") OrgAndRepoActors;
    }
}
