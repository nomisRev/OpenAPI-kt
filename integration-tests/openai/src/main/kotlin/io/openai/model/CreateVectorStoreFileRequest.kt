package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateVectorStoreFileRequest(
  @SerialName("file_id")
  public val fileId: String,
  @SerialName("chunking_strategy")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val chunkingStrategy: ChunkingStrategyRequestParam? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attributes: Optional<VectorStoreFileAttributes> = Optional.Absent,
)
