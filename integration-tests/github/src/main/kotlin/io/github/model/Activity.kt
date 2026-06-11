package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Activity
 */
@Serializable
public data class Activity(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val before: String,
  public val after: String,
  public val ref: String,
  public val timestamp: Instant,
  @SerialName("activity_type")
  public val activityType: ActivityType,
  public val actor: NullableSimpleUser?,
) {
  @Serializable
  public enum class ActivityType(
    public val `value`: String,
  ) {
    @SerialName("push")
    Push("push"),
    @SerialName("force_push")
    ForcePush("force_push"),
    @SerialName("branch_deletion")
    BranchDeletion("branch_deletion"),
    @SerialName("branch_creation")
    BranchCreation("branch_creation"),
    @SerialName("pr_merge")
    PrMerge("pr_merge"),
    @SerialName("merge_queue_merge")
    MergeQueueMerge("merge_queue_merge"),
    ;
  }
}
