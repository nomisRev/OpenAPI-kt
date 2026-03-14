package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Activity(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val before: String,
    val after: String,
    val ref: String,
    val timestamp: LocalDateTime,
    @SerialName("activity_type") val activityType: ActivityType,
    val actor: NullableSimpleUser?,
) {
    @Serializable
    enum class ActivityType {
        @SerialName("push")
        Push,
        @SerialName("force_push")
        ForcePush,
        @SerialName("branch_deletion")
        BranchDeletion,
        @SerialName("branch_creation")
        BranchCreation,
        @SerialName("pr_merge")
        PrMerge,
        @SerialName("merge_queue_merge")
        MergeQueueMerge;
    }
}
