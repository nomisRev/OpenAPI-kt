package union.recursive.ref.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Tree.Serializer::class)
sealed interface Tree {
    @Serializable
    @JvmInline
    value class Leaf(val leaf: String? = null) : Tree

    @Serializable
    @JvmInline
    value class Children(val children: List<Tree>? = null) : Tree

    object Serializer : KSerializer<Tree> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.recursive.ref.model.Tree", PolymorphicKind.SEALED) {
                element("Leaf", Leaf.serializer().descriptor)
                element("Children", Children.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Tree {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                Leaf::class to { decodeFromJsonElement(Leaf.serializer(), it) },
                Children::class to { decodeFromJsonElement(Children.serializer(), it) },
            )
        }

        override fun serialize(encoder: Encoder, value: Tree) = when(value) {
            is Leaf -> encoder.encodeSerializableValue(Leaf.serializer(), value)
            is Children -> encoder.encodeSerializableValue(Children.serializer(), value)
        }
    }
}
