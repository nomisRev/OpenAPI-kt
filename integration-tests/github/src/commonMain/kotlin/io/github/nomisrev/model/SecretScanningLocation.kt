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
data class SecretScanningLocation(val type: Type? = null, val details: Details? = null) {
    @Serializable
    enum class Type {
        @SerialName("commit")
        Commit,
        @SerialName("wiki_commit")
        WikiCommit,
        @SerialName("issue_title")
        IssueTitle,
        @SerialName("issue_body")
        IssueBody,
        @SerialName("issue_comment")
        IssueComment,
        @SerialName("discussion_title")
        DiscussionTitle,
        @SerialName("discussion_body")
        DiscussionBody,
        @SerialName("discussion_comment")
        DiscussionComment,
        @SerialName("pull_request_title")
        PullRequestTitle,
        @SerialName("pull_request_body")
        PullRequestBody,
        @SerialName("pull_request_comment")
        PullRequestComment,
        @SerialName("pull_request_review")
        PullRequestReview,
        @SerialName("pull_request_review_comment")
        PullRequestReviewComment;
    }

    @Serializable(with = Details.Serializer::class)
    sealed interface Details {
        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationCommit(val value: SecretScanningLocationCommit) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationWikiCommit(val value: SecretScanningLocationWikiCommit) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationIssueTitle(val value: SecretScanningLocationIssueTitle) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationIssueBody(val value: SecretScanningLocationIssueBody) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationIssueComment(val value: SecretScanningLocationIssueComment) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationDiscussionTitle(val value: SecretScanningLocationDiscussionTitle) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationDiscussionBody(val value: SecretScanningLocationDiscussionBody) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationDiscussionComment(val value: SecretScanningLocationDiscussionComment) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationPullRequestTitle(val value: SecretScanningLocationPullRequestTitle) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationPullRequestBody(val value: SecretScanningLocationPullRequestBody) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationPullRequestComment(val value: SecretScanningLocationPullRequestComment) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationPullRequestReview(val value: SecretScanningLocationPullRequestReview) : Details

        @Serializable
        @JvmInline
        value class CaseSecretScanningLocationPullRequestReviewComment(val value: SecretScanningLocationPullRequestReviewComment) : Details

        object Serializer : KSerializer<Details> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.SecretScanningLocation.Details", PolymorphicKind.SEALED) {
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

            override fun deserialize(decoder: Decoder): Details {
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

            override fun serialize(encoder: Encoder, value: Details) = when(value) {
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
