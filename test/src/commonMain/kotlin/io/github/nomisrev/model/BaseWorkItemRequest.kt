package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BaseWorkItemRequest {


    @SerialName("Default")
    @Serializable
    data object Default : BaseWorkItemRequest

    @SerialName("IssueWorkItem")
    @Serializable
    data class IssueWorkItem(
        val author: UserRequest? = null,
        val creator: UserRequest? = null,
        val text: String? = null,
        val type: WorkItemTypeRequest? = null,
        val created: Long? = null,
        val updated: Long? = null,
        val duration: DurationValueRequest? = null,
        val date: Long? = null,
        val issue: IssueRequest? = null,
    ) : BaseWorkItemRequest
}
