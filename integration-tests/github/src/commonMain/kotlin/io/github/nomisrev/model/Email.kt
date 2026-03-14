package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class Email(val email: String, val primary: Boolean, val verified: Boolean, val visibility: String?)
