package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The type of issue.
 */
@Serializable
public data class IssueType(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  public val description: String?,
  public val color: Color? = null,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
  @SerialName("is_enabled")
  public val isEnabled: Boolean? = null,
) {
  @Serializable
  public enum class Color(
    public val `value`: String,
  ) {
    @SerialName("gray")
    Gray("gray"),
    @SerialName("blue")
    Blue("blue"),
    @SerialName("green")
    Green("green"),
    @SerialName("yellow")
    Yellow("yellow"),
    @SerialName("orange")
    Orange("orange"),
    @SerialName("red")
    Red("red"),
    @SerialName("pink")
    Pink("pink"),
    @SerialName("purple")
    Purple("purple"),
    ;
  }
}
