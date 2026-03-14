package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckAutomatedSecurityFixes(val enabled: Boolean, val paused: Boolean)
