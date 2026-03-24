package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A batch of files attached to a vector store.
 */
@Serializable
public data class VectorStoreFileBatchObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("vector_store_id")
  public val vectorStoreId: String,
  public val status: Status,
  @SerialName("file_counts")
  public val fileCounts: FileCounts,
) {
  @Serializable
  public data class FileCounts(
    @SerialName("in_progress")
    public val inProgress: Long,
    public val completed: Long,
    public val failed: Long,
    public val cancelled: Long,
    public val total: Long,
  )

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("vector_store.files_batch")
    VectorStoreFilesBatch("vector_store.files_batch"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    @SerialName("failed")
    Failed("failed"),
    ;
  }
}
