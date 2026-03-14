package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class PullRequestMergeResult(val sha: String, val merged: Boolean, val message: String)
