package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateEvent(
  public val ref: String,
  @SerialName("ref_type")
  public val refType: String,
  @SerialName("full_ref")
  public val fullRef: String,
  @SerialName("master_branch")
  public val masterBranch: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @SerialName("pusher_type")
  public val pusherType: String,
)
