package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class BranchShort(val name: String, val commit: Commit, val protected: Boolean) {
    @Serializable
    data class Commit(val sha: String, val url: String)
}
