package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ShortBranch(
    val name: String,
    val commit: Commit,
    val protected: Boolean,
    val protection: BranchProtection? = null,
    @SerialName("protection_url") val protectionUrl: String? = null,
) {
    @Serializable
    data class Commit(val sha: String, val url: String)
}
