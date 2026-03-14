package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SocialAccount(val provider: String, val url: String)
