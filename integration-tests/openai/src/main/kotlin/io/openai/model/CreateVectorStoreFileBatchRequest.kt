package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateVectorStoreFileBatchRequest(
  @SerialName("file_ids")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileIds: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val files: List<CreateVectorStoreFileRequest>? = null,
  @SerialName("chunking_strategy")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val chunkingStrategy: ChunkingStrategyRequestParam? = null,
  public val attributes: VectorStoreFileAttributes? = null,
)
