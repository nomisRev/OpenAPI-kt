package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UpdateVectorStoreRequest(
  public val name: String? = null,
  @SerialName("expires_after")
  public val expiresAfter: VectorStoreExpirationAfter? = null,
  public val metadata: Metadata? = null,
)
