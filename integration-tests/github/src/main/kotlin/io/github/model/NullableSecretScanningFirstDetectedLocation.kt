package io.github.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Details on the location where the token was initially detected. This can be a commit, wiki commit, issue, discussion, pull request.
 *
 */
@Serializable(with = NullableSecretScanningFirstDetectedLocation.Serializer::class)
public sealed interface NullableSecretScanningFirstDetectedLocation {
  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationCommit(
    public val `value`: SecretScanningLocationCommit,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationWikiCommit(
    public val `value`: SecretScanningLocationWikiCommit,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationIssueTitle(
    public val `value`: SecretScanningLocationIssueTitle,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationIssueBody(
    public val `value`: SecretScanningLocationIssueBody,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationIssueComment(
    public val `value`: SecretScanningLocationIssueComment,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationDiscussionTitle(
    public val `value`: SecretScanningLocationDiscussionTitle,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationDiscussionBody(
    public val `value`: SecretScanningLocationDiscussionBody,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationDiscussionComment(
    public val `value`: SecretScanningLocationDiscussionComment,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationPullRequestTitle(
    public val `value`: SecretScanningLocationPullRequestTitle,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationPullRequestBody(
    public val `value`: SecretScanningLocationPullRequestBody,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationPullRequestComment(
    public val `value`: SecretScanningLocationPullRequestComment,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationPullRequestReview(
    public val `value`: SecretScanningLocationPullRequestReview,
  ) : NullableSecretScanningFirstDetectedLocation

  @Serializable
  @JvmInline
  public value class CaseSecretScanningLocationPullRequestReviewComment(
    public val `value`: SecretScanningLocationPullRequestReviewComment,
  ) : NullableSecretScanningFirstDetectedLocation

  public object Serializer : KSerializer<NullableSecretScanningFirstDetectedLocation> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.model.NullableSecretScanningFirstDetectedLocation", PolymorphicKind.SEALED) {
      element("CaseSecretScanningLocationCommit", SecretScanningLocationCommit.serializer().descriptor)
      element("CaseSecretScanningLocationWikiCommit", SecretScanningLocationWikiCommit.serializer().descriptor)
      element("CaseSecretScanningLocationIssueTitle", SecretScanningLocationIssueTitle.serializer().descriptor)
      element("CaseSecretScanningLocationIssueBody", SecretScanningLocationIssueBody.serializer().descriptor)
      element("CaseSecretScanningLocationIssueComment", SecretScanningLocationIssueComment.serializer().descriptor)
      element("CaseSecretScanningLocationDiscussionTitle", SecretScanningLocationDiscussionTitle.serializer().descriptor)
      element("CaseSecretScanningLocationDiscussionBody", SecretScanningLocationDiscussionBody.serializer().descriptor)
      element("CaseSecretScanningLocationDiscussionComment", SecretScanningLocationDiscussionComment.serializer().descriptor)
      element("CaseSecretScanningLocationPullRequestTitle", SecretScanningLocationPullRequestTitle.serializer().descriptor)
      element("CaseSecretScanningLocationPullRequestBody", SecretScanningLocationPullRequestBody.serializer().descriptor)
      element("CaseSecretScanningLocationPullRequestComment", SecretScanningLocationPullRequestComment.serializer().descriptor)
      element("CaseSecretScanningLocationPullRequestReview", SecretScanningLocationPullRequestReview.serializer().descriptor)
      element("CaseSecretScanningLocationPullRequestReviewComment", SecretScanningLocationPullRequestReviewComment.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): NullableSecretScanningFirstDetectedLocation {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseSecretScanningLocationCommit::class to { CaseSecretScanningLocationCommit(decodeFromJsonElement(SecretScanningLocationCommit.serializer(), it)) },
        CaseSecretScanningLocationWikiCommit::class to { CaseSecretScanningLocationWikiCommit(decodeFromJsonElement(SecretScanningLocationWikiCommit.serializer(), it)) },
        CaseSecretScanningLocationIssueTitle::class to { CaseSecretScanningLocationIssueTitle(decodeFromJsonElement(SecretScanningLocationIssueTitle.serializer(), it)) },
        CaseSecretScanningLocationIssueBody::class to { CaseSecretScanningLocationIssueBody(decodeFromJsonElement(SecretScanningLocationIssueBody.serializer(), it)) },
        CaseSecretScanningLocationIssueComment::class to { CaseSecretScanningLocationIssueComment(decodeFromJsonElement(SecretScanningLocationIssueComment.serializer(), it)) },
        CaseSecretScanningLocationDiscussionTitle::class to { CaseSecretScanningLocationDiscussionTitle(decodeFromJsonElement(SecretScanningLocationDiscussionTitle.serializer(), it)) },
        CaseSecretScanningLocationDiscussionBody::class to { CaseSecretScanningLocationDiscussionBody(decodeFromJsonElement(SecretScanningLocationDiscussionBody.serializer(), it)) },
        CaseSecretScanningLocationDiscussionComment::class to { CaseSecretScanningLocationDiscussionComment(decodeFromJsonElement(SecretScanningLocationDiscussionComment.serializer(), it)) },
        CaseSecretScanningLocationPullRequestTitle::class to { CaseSecretScanningLocationPullRequestTitle(decodeFromJsonElement(SecretScanningLocationPullRequestTitle.serializer(), it)) },
        CaseSecretScanningLocationPullRequestBody::class to { CaseSecretScanningLocationPullRequestBody(decodeFromJsonElement(SecretScanningLocationPullRequestBody.serializer(), it)) },
        CaseSecretScanningLocationPullRequestComment::class to { CaseSecretScanningLocationPullRequestComment(decodeFromJsonElement(SecretScanningLocationPullRequestComment.serializer(), it)) },
        CaseSecretScanningLocationPullRequestReview::class to { CaseSecretScanningLocationPullRequestReview(decodeFromJsonElement(SecretScanningLocationPullRequestReview.serializer(), it)) },
        CaseSecretScanningLocationPullRequestReviewComment::class to { CaseSecretScanningLocationPullRequestReviewComment(decodeFromJsonElement(SecretScanningLocationPullRequestReviewComment.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: NullableSecretScanningFirstDetectedLocation) {
      when(value) {
        is CaseSecretScanningLocationCommit -> encoder.encodeSerializableValue(SecretScanningLocationCommit.serializer(), value.value)
        is CaseSecretScanningLocationWikiCommit -> encoder.encodeSerializableValue(SecretScanningLocationWikiCommit.serializer(), value.value)
        is CaseSecretScanningLocationIssueTitle -> encoder.encodeSerializableValue(SecretScanningLocationIssueTitle.serializer(), value.value)
        is CaseSecretScanningLocationIssueBody -> encoder.encodeSerializableValue(SecretScanningLocationIssueBody.serializer(), value.value)
        is CaseSecretScanningLocationIssueComment -> encoder.encodeSerializableValue(SecretScanningLocationIssueComment.serializer(), value.value)
        is CaseSecretScanningLocationDiscussionTitle -> encoder.encodeSerializableValue(SecretScanningLocationDiscussionTitle.serializer(), value.value)
        is CaseSecretScanningLocationDiscussionBody -> encoder.encodeSerializableValue(SecretScanningLocationDiscussionBody.serializer(), value.value)
        is CaseSecretScanningLocationDiscussionComment -> encoder.encodeSerializableValue(SecretScanningLocationDiscussionComment.serializer(), value.value)
        is CaseSecretScanningLocationPullRequestTitle -> encoder.encodeSerializableValue(SecretScanningLocationPullRequestTitle.serializer(), value.value)
        is CaseSecretScanningLocationPullRequestBody -> encoder.encodeSerializableValue(SecretScanningLocationPullRequestBody.serializer(), value.value)
        is CaseSecretScanningLocationPullRequestComment -> encoder.encodeSerializableValue(SecretScanningLocationPullRequestComment.serializer(), value.value)
        is CaseSecretScanningLocationPullRequestReview -> encoder.encodeSerializableValue(SecretScanningLocationPullRequestReview.serializer(), value.value)
        is CaseSecretScanningLocationPullRequestReviewComment -> encoder.encodeSerializableValue(SecretScanningLocationPullRequestReviewComment.serializer(), value.value)
      }
    }
  }
}
