package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AuthorAssociation(
  public val `value`: String,
) {
  COLLABORATOR("COLLABORATOR"),
  CONTRIBUTOR("CONTRIBUTOR"),
  @SerialName("FIRST_TIMER")
  FIRSTTIMER("FIRST_TIMER"),
  @SerialName("FIRST_TIME_CONTRIBUTOR")
  FIRSTTIMECONTRIBUTOR("FIRST_TIME_CONTRIBUTOR"),
  MANNEQUIN("MANNEQUIN"),
  MEMBER("MEMBER"),
  NONE("NONE"),
  OWNER("OWNER"),
  ;
}
