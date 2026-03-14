package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableCommunityHealthFile(val url: String, @SerialName("html_url") val htmlUrl: String)
