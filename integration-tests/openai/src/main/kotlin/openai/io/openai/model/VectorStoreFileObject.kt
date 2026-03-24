package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A list of files attached to a vector store.
 */
@Serializable
public data class VectorStoreFileObject(
  public val id: String,
  public val `object`: Object,
  @SerialName("usage_bytes")
  public val usageBytes: Long,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("vector_store_id")
  public val vectorStoreId: String,
  public val status: Status,
  @SerialName("last_error")
  public val lastError: LastError?,
  @SerialName("chunking_strategy")
  public val chunkingStrategy: JsonElement? = null,
  public val attributes: VectorStoreFileAttributes? = null,
) {
  /**
   * The last error associated with this vector store file. Will be `null` if there are no errors.
   */
  @Serializable
  public data class LastError(
    public val code: Code,
    public val message: String,
  ) {
    @Serializable
    public enum class Code(
      public val `value`: String,
    ) {
      @SerialName("server_error")
      ServerError("server_error"),
      @SerialName("unsupported_file")
      UnsupportedFile("unsupported_file"),
      @SerialName("invalid_file")
      InvalidFile("invalid_file"),
      ;
    }
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("vector_store.file")
    VectorStoreFile("vector_store.file"),
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
