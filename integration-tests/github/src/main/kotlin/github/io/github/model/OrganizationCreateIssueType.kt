package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OrganizationCreateIssueType(
  public val name: String,
  @SerialName("is_enabled")
  public val isEnabled: Boolean,
  public val description: String? = null,
  public val color: Color? = null,
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
