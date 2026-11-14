package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OrganizationUpdateIssueType(
  public val name: String,
  @SerialName("is_enabled")
  public val isEnabled: Boolean,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val color: Optional<Color> = Optional.Absent,
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
