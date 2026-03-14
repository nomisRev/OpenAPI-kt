package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberEvent(val action: String, val member: SimpleUser)
