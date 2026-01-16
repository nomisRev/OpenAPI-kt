package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BaseWorkItemResponse {
    val id: String?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val id: String? = null) : BaseWorkItemResponse

    @SerialName("IssueWorkItem")
    @Serializable
    data class IssueWorkItem(
        override val id: String? = null,
        val author: UserResponse? = null,
        val creator: UserResponse? = null,
        val text: String? = null,
        val textPreview: String? = null,
        val type: WorkItemTypeResponse? = null,
        val created: Long? = null,
        val updated: Long? = null,
        val duration: DurationValueResponse? = null,
        val date: Long? = null,
        val issue: IssueResponse? = null,
        val attributes: List<WorkItemAttributeResponse>? = null,
    ) : BaseWorkItemResponse
}
