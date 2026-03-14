package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class PullRequestMinimal(val id: Long, val number: Long, val url: String, val head: Head, val base: Base) {
    @Serializable
    data class Head(val ref: String, val sha: String, val repo: Repo) {
        @Serializable
        data class Repo(val id: Long, val url: String, val name: String)
    }

    @Serializable
    data class Base(val ref: String, val sha: String, val repo: Repo) {
        @Serializable
        data class Repo(val id: Long, val url: String, val name: String)
    }
}
