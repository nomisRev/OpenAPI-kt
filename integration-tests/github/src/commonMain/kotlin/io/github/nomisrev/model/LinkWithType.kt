package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class LinkWithType(val href: String, val type: String)
