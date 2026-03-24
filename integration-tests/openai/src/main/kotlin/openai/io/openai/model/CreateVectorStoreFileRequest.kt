package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateVectorStoreFileRequest(
  @SerialName("file_id")
  public val fileId: String,
  @SerialName("chunking_strategy")
  public val chunkingStrategy: ChunkingStrategyRequestParam? = null,
  public val attributes: VectorStoreFileAttributes? = null,
)
