package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class PublicIp(val enabled: Boolean? = null, val prefix: String? = null, val length: Long? = null)
