package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OrganizationCreateIssueField(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @SerialName("data_type")
  public val dataType: DataType,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: Visibility? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val options: Optional<List<Options>> = Optional.Absent,
) {
  @Serializable
  public enum class DataType(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    @SerialName("date")
    Date("date"),
    @SerialName("single_select")
    SingleSelect("single_select"),
    @SerialName("number")
    Number("number"),
    ;
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Options(
    public val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: Optional<String> = Optional.Absent,
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
