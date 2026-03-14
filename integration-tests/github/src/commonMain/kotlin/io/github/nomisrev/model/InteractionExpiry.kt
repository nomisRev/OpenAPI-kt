package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class InteractionExpiry {
    @SerialName("one_day")
    OneDay,
    @SerialName("three_days")
    ThreeDays,
    @SerialName("one_week")
    OneWeek,
    @SerialName("one_month")
    OneMonth,
    @SerialName("six_months")
    SixMonths;
}
