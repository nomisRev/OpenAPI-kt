package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OrganizationUpdateIssueField(
  public val name: String? = null,
  public val description: String? = null,
  public val visibility: Visibility? = null,
  public val options: List<Options>? = null,
) {
  @Serializable
  public data class Options(
    public val name: String,
    public val description: String? = null,
    public val color: Color,
    public val priority: Long,
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

  @Serializable
  public enum class Visibility(
    public val `value`: String,
  ) {
    @SerialName("organization_members_only")
    OrganizationMembersOnly("organization_members_only"),
    @SerialName("all")
    All("all"),
    ;
  }
}
