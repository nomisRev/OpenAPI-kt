package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class InteractionExpiry(
  public val `value`: String,
) {
  @SerialName("one_day")
  OneDay("one_day"),
  @SerialName("three_days")
  ThreeDays("three_days"),
  @SerialName("one_week")
  OneWeek("one_week"),
  @SerialName("one_month")
  OneMonth("one_month"),
  @SerialName("six_months")
  SixMonths("six_months"),
  ;
}
