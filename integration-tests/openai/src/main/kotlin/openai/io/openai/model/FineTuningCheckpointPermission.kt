package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `checkpoint.permission` object represents a permission for a fine-tuned model checkpoint.
 *
 */
@Serializable
public data class FineTuningCheckpointPermission(
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("project_id")
  public val projectId: String,
  public val `object`: Object,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("checkpoint.permission")
    CheckpointPermission("checkpoint.permission"),
    ;
  }
}
