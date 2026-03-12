package union.enum.primitive.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.builtins.serializer

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class CaseString(val value: String) : Union

    @Serializable
    enum class AscOrDesc : Union {
        @SerialName("asc") Asc, @SerialName("desc") Desc;
    }

    object Serializer : KSerializer<Union> {
        override val descriptor: SerialDescriptor = String.serializer().descriptor

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            AscOrDesc.Asc -> encoder.encodeString("asc")
            AscOrDesc.Desc -> encoder.encodeString("desc")
            is CaseString -> encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): Union =
            when(val value = decoder.decodeString()) {
                "asc" -> AscOrDesc.Asc
                "desc" -> AscOrDesc.Desc
                else -> CaseString(value)
            }
    }
}
