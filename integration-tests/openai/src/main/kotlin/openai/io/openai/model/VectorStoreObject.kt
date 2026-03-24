package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A vector store is a collection of processed files can be used by the `file_search` tool.
 */
@Serializable
public data class VectorStoreObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("created_at")
  public val createdAt: Long,
  public val name: String,
  @SerialName("usage_bytes")
  public val usageBytes: Long,
  @SerialName("file_counts")
  public val fileCounts: FileCounts,
  public val status: Status,
  @SerialName("expires_after")
  public val expiresAfter: VectorStoreExpirationAfter? = null,
  @SerialName("expires_at")
  public val expiresAt: Long? = null,
  @SerialName("last_active_at")
  public val lastActiveAt: Long?,
  public val metadata: Metadata,
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
    @SerialName("vector_store")
    VectorStore("vector_store"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("expired")
    Expired("expired"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    ;
  }
}
