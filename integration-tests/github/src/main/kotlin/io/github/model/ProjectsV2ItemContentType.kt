package io.github.model

import kotlinx.serialization.Serializable

@Serializable
public enum class ProjectsV2ItemContentType {
  Issue,
  PullRequest,
  DraftIssue,
}
