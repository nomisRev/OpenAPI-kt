package io.github.nomisrev.model

import kotlin.uuid.ExperimentalUuidApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.Uuid
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@ExperimentalUuidApi
@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class CaseString(val value: String) : Union

    @Serializable
    @JvmInline
    value class CaseInt(val value: Int) : Union

    @Serializable
    @JvmInline
    value class CaseFloat(val value: Float) : Union

    @Serializable
    @JvmInline
    value class CaseDouble(val value: Double) : Union

    @Serializable
    @JvmInline
    value class CaseDate(val value: LocalDate) : Union

    @Serializable
    @JvmInline
    value class CaseDateTime(val value: LocalDateTime) : Union

    @Serializable
    @JvmInline
    value class CaseBinary(val value: ByteArray) : Union

    @Serializable
    @JvmInline
    value class CaseUuid(val value: Uuid) : Union

    @Serializable
    data object Empty : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.model.Union", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CaseInt", Int.serializer().descriptor)
                element("CaseFloat", Float.serializer().descriptor)
                element("CaseDouble", Double.serializer().descriptor)
                element("CaseDate", LocalDate.serializer().descriptor)
                element("CaseDateTime", LocalDateTime.serializer().descriptor)
                element("CaseBinary", ByteArraySerializer().descriptor)
                element("CaseUuid", Uuid.serializer().descriptor)
                element("Empty", Unit.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
                CaseFloat::class to { CaseFloat(decodeFromJsonElement(Float.serializer(), it)) },
                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
                CaseUuid::class to { CaseUuid(decodeFromJsonElement(Uuid.serializer(), it)) },
                CaseDate::class to { CaseDate(decodeFromJsonElement(LocalDate.serializer(), it)) },
                CaseDateTime::class to { CaseDateTime(decodeFromJsonElement(LocalDateTime.serializer(), it)) },
                CaseBinary::class to { CaseBinary(decodeFromJsonElement(ByteArraySerializer(), it)) },
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
            is CaseFloat -> encoder.encodeSerializableValue(Float.serializer(), value.value)
            is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
            is CaseDate -> encoder.encodeSerializableValue(LocalDate.serializer(), value.value)
            is CaseDateTime -> encoder.encodeSerializableValue(LocalDateTime.serializer(), value.value)
            is CaseBinary -> encoder.encodeSerializableValue(ByteArraySerializer(), value.value)
            is CaseUuid -> encoder.encodeSerializableValue(Uuid.serializer(), value.value)
            is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
        }
    }
}
