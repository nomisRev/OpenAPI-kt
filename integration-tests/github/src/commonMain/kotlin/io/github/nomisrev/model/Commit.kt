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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class Commit(
    val url: String,
    val sha: String,
    @SerialName("node_id") val nodeId: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("comments_url") val commentsUrl: String,
    val commit: Commit,
    val author: Author?,
    val committer: Committer?,
    val parents: List<Parents>,
    val stats: Stats? = null,
    val files: List<DiffEntry>? = null,
) {
    @Serializable
    data class Commit(
        val url: String,
        val author: NullableGitUser?,
        val committer: NullableGitUser?,
        val message: String,
        @SerialName("comment_count") val commentCount: Long,
        val tree: Tree,
        val verification: Verification? = null,
    ) {
        @Serializable
        data class Tree(val sha: String, val url: String)
    }

    @Serializable(with = Author.Serializer::class)
    sealed interface Author {
        @Serializable
        @JvmInline
        value class CaseSimpleUser(val value: SimpleUser) : Author

        @Serializable
        @JvmInline
        value class CaseEmptyObject(val value: EmptyObject) : Author

        object Serializer : KSerializer<Author> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Commit.Author", PolymorphicKind.SEALED) {
                    element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                    element("CaseEmptyObject", EmptyObject.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Author {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                    CaseEmptyObject::class to { CaseEmptyObject(decodeFromJsonElement(EmptyObject.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Author) = when(value) {
                is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                is CaseEmptyObject -> encoder.encodeSerializableValue(EmptyObject.serializer(), value.value)
            }
        }
    }

    @Serializable(with = Committer.Serializer::class)
    sealed interface Committer {
        @Serializable
        @JvmInline
        value class CaseSimpleUser(val value: SimpleUser) : Committer

        @Serializable
        @JvmInline
        value class CaseEmptyObject(val value: EmptyObject) : Committer

        object Serializer : KSerializer<Committer> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Commit.Committer", PolymorphicKind.SEALED) {
                    element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                    element("CaseEmptyObject", EmptyObject.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Committer {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                    CaseEmptyObject::class to { CaseEmptyObject(decodeFromJsonElement(EmptyObject.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Committer) = when(value) {
                is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                is CaseEmptyObject -> encoder.encodeSerializableValue(EmptyObject.serializer(), value.value)
            }
        }
    }

    @Serializable
    data class Parents(val sha: String, val url: String, @SerialName("html_url") val htmlUrl: String? = null)

    @Serializable
    data class Stats(val additions: Long? = null, val deletions: Long? = null, val total: Long? = null)
}
