package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ProtectedBranchAdminEnforced(val url: String, val enabled: Boolean)
