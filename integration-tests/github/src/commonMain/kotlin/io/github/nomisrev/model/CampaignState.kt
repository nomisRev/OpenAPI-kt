package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CampaignState {
    @SerialName("open") Open, @SerialName("closed") Closed;
}
