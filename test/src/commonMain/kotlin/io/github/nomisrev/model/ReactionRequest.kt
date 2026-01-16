package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ReactionRequest(val author: UserRequest? = null, val reaction: String? = null)
