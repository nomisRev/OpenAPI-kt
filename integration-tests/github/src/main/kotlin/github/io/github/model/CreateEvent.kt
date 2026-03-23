package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateEvent(
  public val ref: String,
  @SerialName("ref_type")
  public val refType: String,
  @SerialName("full_ref")
  public val fullRef: String,
  @SerialName("master_branch")
  public val masterBranch: String,
  public val description: String? = null,
  @SerialName("pusher_type")
  public val pusherType: String,
)
