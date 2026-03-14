package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
enum class ProjectsV2ItemContentType {
    Issue, PullRequest, DraftIssue;
}
