package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class DiscussionEvent(val action: String, val discussion: Discussion)
