package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface CommandVisibilityRequest {


    @SerialName("Default")
    @Serializable
    data object Default : CommandVisibilityRequest

    @SerialName("CommandUnlimitedVisibility")
    @Serializable
    data object CommandUnlimitedVisibility : CommandVisibilityRequest

    @SerialName("CommandLimitedVisibility")
    @Serializable
    data object CommandLimitedVisibility : CommandVisibilityRequest
}
