package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Minimal Repository
 */
@Serializable
public data class MinimalRepository(
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
  @SerialName("git_url")
  public val gitUrl: String? = null,
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
  @SerialName("ssh_url")
  public val sshUrl: String? = null,
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
  @SerialName("clone_url")
  public val cloneUrl: String? = null,
  @SerialName("mirror_url")
  public val mirrorUrl: String? = null,
  @SerialName("hooks_url")
  public val hooksUrl: String,
  @SerialName("svn_url")
  public val svnUrl: String? = null,
  public val homepage: String? = null,
  public val language: String? = null,
  @SerialName("forks_count")
  public val forksCount: Long? = null,
  @SerialName("stargazers_count")
  public val stargazersCount: Long? = null,
  @SerialName("watchers_count")
  public val watchersCount: Long? = null,
  public val size: Long? = null,
  @SerialName("default_branch")
  public val defaultBranch: String? = null,
  @SerialName("open_issues_count")
  public val openIssuesCount: Long? = null,
  @SerialName("is_template")
  public val isTemplate: Boolean? = null,
  public val topics: List<String>? = null,
  @SerialName("has_issues")
  public val hasIssues: Boolean? = null,
  @SerialName("has_projects")
  public val hasProjects: Boolean? = null,
  @SerialName("has_wiki")
  public val hasWiki: Boolean? = null,
  @SerialName("has_pages")
  public val hasPages: Boolean? = null,
  @SerialName("has_downloads")
  public val hasDownloads: Boolean? = null,
  @SerialName("has_discussions")
  public val hasDiscussions: Boolean? = null,
  @SerialName("has_pull_requests")
  public val hasPullRequests: Boolean? = null,
  @SerialName("pull_request_creation_policy")
  public val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
  @SerialName("has_commit_comments")
  public val hasCommitComments: Boolean? = null,
  public val archived: Boolean? = null,
  public val disabled: Boolean? = null,
  public val visibility: String? = null,
  @SerialName("pushed_at")
  public val pushedAt: Instant? = null,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
  public val permissions: Permissions? = null,
  @SerialName("role_name")
  public val roleName: String? = null,
  @SerialName("temp_clone_token")
  public val tempCloneToken: String? = null,
  @SerialName("delete_branch_on_merge")
  public val deleteBranchOnMerge: Boolean? = null,
  @SerialName("subscribers_count")
  public val subscribersCount: Long? = null,
  @SerialName("network_count")
  public val networkCount: Long? = null,
  @SerialName("code_of_conduct")
  public val codeOfConduct: CodeOfConduct? = null,
  public val license: License? = null,
  public val forks: Long? = null,
  @SerialName("open_issues")
  public val openIssues: Long? = null,
  public val watchers: Long? = null,
  @SerialName("allow_forking")
  public val allowForking: Boolean? = null,
  @SerialName("web_commit_signoff_required")
  public val webCommitSignoffRequired: Boolean? = null,
  @SerialName("security_and_analysis")
  public val securityAndAnalysis: SecurityAndAnalysis? = null,
  @SerialName("custom_properties")
  public val customProperties: JsonElement? = null,
) {
  @Serializable
  public data class License(
    public val key: String? = null,
    public val name: String? = null,
    @SerialName("spdx_id")
    public val spdxId: String? = null,
    public val url: String? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
  )

  @Serializable
  public data class Permissions(
    public val admin: Boolean? = null,
    public val maintain: Boolean? = null,
    public val push: Boolean? = null,
    public val triage: Boolean? = null,
    public val pull: Boolean? = null,
  )

  @Serializable
  public enum class PullRequestCreationPolicy(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("collaborators_only")
    CollaboratorsOnly("collaborators_only"),
    ;
  }
}
