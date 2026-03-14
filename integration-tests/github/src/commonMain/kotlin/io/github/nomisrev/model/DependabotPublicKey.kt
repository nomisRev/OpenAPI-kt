package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DependabotPublicKey(@SerialName("key_id") val keyId: String, val key: String)
