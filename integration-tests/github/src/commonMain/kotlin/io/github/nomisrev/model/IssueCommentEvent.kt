package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class IssueCommentEvent(val action: String, val issue: Issue, val comment: IssueComment)
