package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReviewCustomGatesCommentRequired(
    @SerialName("environment_name") val environmentName: String,
    val comment: String,
)
