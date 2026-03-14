package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class PullRequestReviewRequest(val users: List<SimpleUser>, val teams: List<Team>)
