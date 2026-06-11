package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
 * Commit
 */
@Serializable
public data class Commit(
  public val url: String,
  public val sha: String,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  public val commit: Commit,
  public val author: Author?,
  public val committer: Committer?,
  public val parents: List<Parents>,
  public val stats: Stats? = null,
  public val files: List<DiffEntry>? = null,
) {
  @Serializable(with = Author.Serializer::class)
  public sealed interface Author {
    @Serializable
    @JvmInline
    public value class CaseSimpleUser(
      public val `value`: SimpleUser,
    ) : Author

    @Serializable
    @JvmInline
    public value class CaseEmptyObject(
      public val `value`: EmptyObject,
    ) : Author

    public object Serializer : KSerializer<Author> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Commit.Author", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: Author) {
        when(value) {
          is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
          is CaseEmptyObject -> encoder.encodeSerializableValue(EmptyObject.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public data class Commit(
    public val url: String,
    public val author: NullableGitUser?,
    public val committer: NullableGitUser?,
    public val message: String,
    @SerialName("comment_count")
    public val commentCount: Long,
    public val tree: Tree,
    public val verification: Verification? = null,
  ) {
    @Serializable
    public data class Tree(
      public val sha: String,
      public val url: String,
    )
  }

  @Serializable(with = Committer.Serializer::class)
  public sealed interface Committer {
    @Serializable
    @JvmInline
    public value class CaseSimpleUser(
      public val `value`: SimpleUser,
    ) : Committer

    @Serializable
    @JvmInline
    public value class CaseEmptyObject(
      public val `value`: EmptyObject,
    ) : Committer

    public object Serializer : KSerializer<Committer> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Commit.Committer", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: Committer) {
        when(value) {
          is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
          is CaseEmptyObject -> encoder.encodeSerializableValue(EmptyObject.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public data class Parents(
    public val sha: String,
    public val url: String,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
  )

  @Serializable
  public data class Stats(
    public val additions: Long? = null,
    public val deletions: Long? = null,
    public val total: Long? = null,
  )
}
