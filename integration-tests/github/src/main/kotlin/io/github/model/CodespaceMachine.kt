package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A description of the machine powering a codespace.
 */
@Serializable
public data class CodespaceMachine(
  public val name: String,
  @SerialName("display_name")
  public val displayName: String,
  @SerialName("operating_system")
  public val operatingSystem: String,
  @SerialName("storage_in_bytes")
  public val storageInBytes: Long,
  @SerialName("memory_in_bytes")
  public val memoryInBytes: Long,
  public val cpus: Long,
  @SerialName("prebuild_availability")
  public val prebuildAvailability: PrebuildAvailability?,
) {
  @Serializable
  public enum class PrebuildAvailability(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("ready")
    Ready("ready"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    ;
  }
}
