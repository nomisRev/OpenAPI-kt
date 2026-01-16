package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueWatchersRequest(val hasStar: Boolean? = null, val issueWatchers: List<IssueWatcherRequest>? = null)
