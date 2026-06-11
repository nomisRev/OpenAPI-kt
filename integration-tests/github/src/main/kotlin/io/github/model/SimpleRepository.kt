package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub repository.
 */
@Serializable
public data class SimpleRepository(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  @SerialName("full_name")
  public val fullName: String,
  public val owner: SimpleUser,
  public val `private`: Boolean,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val description: String?,
  public val fork: Boolean,
  public val url: String,
  @SerialName("archive_url")
  public val archiveUrl: String,
  @SerialName("assignees_url")
  public val assigneesUrl: String,
  @SerialName("blobs_url")
  public val blobsUrl: String,
  @SerialName("branches_url")
  public val branchesUrl: String,
  @SerialName("collaborators_url")
  public val collaboratorsUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  @SerialName("commits_url")
  public val commitsUrl: String,
  @SerialName("compare_url")
  public val compareUrl: String,
  @SerialName("contents_url")
  public val contentsUrl: String,
  @SerialName("contributors_url")
  public val contributorsUrl: String,
  @SerialName("deployments_url")
  public val deploymentsUrl: String,
  @SerialName("downloads_url")
  public val downloadsUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("forks_url")
  public val forksUrl: String,
  @SerialName("git_commits_url")
  public val gitCommitsUrl: String,
  @SerialName("git_refs_url")
  public val gitRefsUrl: String,
  @SerialName("git_tags_url")
  public val gitTagsUrl: String,
  @SerialName("issue_comment_url")
  public val issueCommentUrl: String,
  @SerialName("issue_events_url")
  public val issueEventsUrl: String,
  @SerialName("issues_url")
  public val issuesUrl: String,
  @SerialName("keys_url")
  public val keysUrl: String,
  @SerialName("labels_url")
  public val labelsUrl: String,
  @SerialName("languages_url")
  public val languagesUrl: String,
  @SerialName("merges_url")
  public val mergesUrl: String,
  @SerialName("milestones_url")
  public val milestonesUrl: String,
  @SerialName("notifications_url")
  public val notificationsUrl: String,
  @SerialName("pulls_url")
  public val pullsUrl: String,
  @SerialName("releases_url")
  public val releasesUrl: String,
  @SerialName("stargazers_url")
  public val stargazersUrl: String,
  @SerialName("statuses_url")
  public val statusesUrl: String,
  @SerialName("subscribers_url")
  public val subscribersUrl: String,
  @SerialName("subscription_url")
  public val subscriptionUrl: String,
  @SerialName("tags_url")
  public val tagsUrl: String,
  @SerialName("teams_url")
  public val teamsUrl: String,
  @SerialName("trees_url")
  public val treesUrl: String,
  @SerialName("hooks_url")
  public val hooksUrl: String,
)
