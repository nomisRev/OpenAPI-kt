package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SelectedActions(
    @SerialName("github_owned_allowed") val githubOwnedAllowed: Boolean? = null,
    @SerialName("verified_allowed") val verifiedAllowed: Boolean? = null,
    @SerialName("patterns_allowed") val patternsAllowed: List<String>? = null,
)
