package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Porter Author
 */
@Serializable
public data class PorterAuthor(
  public val id: Long,
  @SerialName("remote_id")
  public val remoteId: String,
  @SerialName("remote_name")
  public val remoteName: String,
  public val email: String,
  public val name: String,
  public val url: String,
  @SerialName("import_url")
  public val importUrl: String,
)
