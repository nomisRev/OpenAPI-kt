package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateVectorStoreFileBatchRequest(
  @SerialName("file_ids")
  public val fileIds: List<String>? = null,
  public val files: List<CreateVectorStoreFileRequest>? = null,
  @SerialName("chunking_strategy")
  public val chunkingStrategy: ChunkingStrategyRequestParam? = null,
  public val attributes: VectorStoreFileAttributes? = null,
)
