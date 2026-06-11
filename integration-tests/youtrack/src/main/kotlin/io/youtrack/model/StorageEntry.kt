package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * SSL key representation.
 */
@Serializable
public data class StorageEntry(
  public val id: String? = null,
  public val name: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
