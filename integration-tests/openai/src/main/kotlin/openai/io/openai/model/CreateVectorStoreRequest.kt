package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateVectorStoreRequest(
  @SerialName("file_ids")
  public val fileIds: List<String>? = null,
  public val name: String? = null,
  public val description: String? = null,
  @SerialName("expires_after")
  public val expiresAfter: VectorStoreExpirationAfter? = null,
  @SerialName("chunking_strategy")
  public val chunkingStrategy: JsonElement? = null,
  public val metadata: Metadata? = null,
)
