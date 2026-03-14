package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsHostedRunnerCustomImageVersion(
    val version: String,
    val state: String,
    @SerialName("size_gb") val sizeGb: Long,
    @SerialName("created_on") val createdOn: String,
    @SerialName("state_details") val stateDetails: String,
)
