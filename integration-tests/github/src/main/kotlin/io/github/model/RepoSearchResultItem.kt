package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Repo Search Result Item
 */
@Serializable
public data class RepoSearchResultItem(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  @SerialName("full_name")
  public val fullName: String,
  public val owner: NullableSimpleUser?,
  public val `private`: Boolean,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val description: String?,
  public val fork: Boolean,
  public val url: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("pushed_at")
  public val pushedAt: Instant,
  public val homepage: String?,
  public val size: Long,
  @SerialName("stargazers_count")
  public val stargazersCount: Long,
  @SerialName("watchers_count")
  public val watchersCount: Long,
  public val language: String?,
  @SerialName("forks_count")
  public val forksCount: Long,
  @SerialName("open_issues_count")
  public val openIssuesCount: Long,
  @SerialName("master_branch")
  public val masterBranch: String? = null,
  @SerialName("default_branch")
  public val defaultBranch: String,
  public val score: Double,
  @SerialName("forks_url")
  public val forksUrl: String,
  @SerialName("keys_url")
  public val keysUrl: String,
  @SerialName("collaborators_url")
  public val collaboratorsUrl: String,
  @SerialName("teams_url")
  public val teamsUrl: String,
  @SerialName("hooks_url")
  public val hooksUrl: String,
  @SerialName("issue_events_url")
  public val issueEventsUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("assignees_url")
  public val assigneesUrl: String,
  @SerialName("branches_url")
  public val branchesUrl: String,
  @SerialName("tags_url")
  public val tagsUrl: String,
  @SerialName("blobs_url")
  public val blobsUrl: String,
  @SerialName("git_tags_url")
  public val gitTagsUrl: String,
  @SerialName("git_refs_url")
  public val gitRefsUrl: String,
  @SerialName("trees_url")
  public val treesUrl: String,
  @SerialName("statuses_url")
  public val statusesUrl: String,
  @SerialName("languages_url")
  public val languagesUrl: String,
  @SerialName("stargazers_url")
  public val stargazersUrl: String,
  @SerialName("contributors_url")
  public val contributorsUrl: String,
  @SerialName("subscribers_url")
  public val subscribersUrl: String,
  @SerialName("subscription_url")
  public val subscriptionUrl: String,
  @SerialName("commits_url")
  public val commitsUrl: String,
  @SerialName("git_commits_url")
  public val gitCommitsUrl: String,
  @SerialName("comments_url")
  public val commentsUrl: String,
  @SerialName("issue_comment_url")
  public val issueCommentUrl: String,
  @SerialName("contents_url")
  public val contentsUrl: String,
  @SerialName("compare_url")
  public val compareUrl: String,
  @SerialName("merges_url")
  public val mergesUrl: String,
  @SerialName("archive_url")
  public val archiveUrl: String,
  @SerialName("downloads_url")
  public val downloadsUrl: String,
  @SerialName("issues_url")
  public val issuesUrl: String,
  @SerialName("pulls_url")
  public val pullsUrl: String,
  @SerialName("milestones_url")
  public val milestonesUrl: String,
  @SerialName("notifications_url")
  public val notificationsUrl: String,
  @SerialName("labels_url")
  public val labelsUrl: String,
  @SerialName("releases_url")
  public val releasesUrl: String,
  @SerialName("deployments_url")
  public val deploymentsUrl: String,
  @SerialName("git_url")
  public val gitUrl: String,
  @SerialName("ssh_url")
  public val sshUrl: String,
  @SerialName("clone_url")
  public val cloneUrl: String,
  @SerialName("svn_url")
  public val svnUrl: String,
  public val forks: Long,
  @SerialName("open_issues")
  public val openIssues: Long,
  public val watchers: Long,
  public val topics: List<String>? = null,
  @SerialName("mirror_url")
  public val mirrorUrl: String?,
  @SerialName("has_issues")
  public val hasIssues: Boolean,
  @SerialName("has_projects")
  public val hasProjects: Boolean,
  @SerialName("has_pages")
  public val hasPages: Boolean,
  @SerialName("has_wiki")
  public val hasWiki: Boolean,
  @SerialName("has_downloads")
  public val hasDownloads: Boolean,
  @SerialName("has_discussions")
  public val hasDiscussions: Boolean? = null,
  @SerialName("has_pull_requests")
  public val hasPullRequests: Boolean? = null,
  @SerialName("pull_request_creation_policy")
  public val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
  @SerialName("has_commit_comments")
  public val hasCommitComments: Boolean? = null,
  public val archived: Boolean,
  public val disabled: Boolean,
  public val visibility: String? = null,
  public val license: NullableLicenseSimple?,
  public val permissions: Permissions? = null,
  @SerialName("text_matches")
  public val textMatches: SearchResultTextMatches? = null,
  @SerialName("temp_clone_token")
  public val tempCloneToken: String? = null,
  @SerialName("allow_merge_commit")
  public val allowMergeCommit: Boolean? = null,
  @SerialName("allow_squash_merge")
  public val allowSquashMerge: Boolean? = null,
  @SerialName("allow_rebase_merge")
  public val allowRebaseMerge: Boolean? = null,
  @SerialName("allow_auto_merge")
  public val allowAutoMerge: Boolean? = null,
  @SerialName("delete_branch_on_merge")
  public val deleteBranchOnMerge: Boolean? = null,
  @SerialName("allow_forking")
  public val allowForking: Boolean? = null,
  @SerialName("is_template")
  public val isTemplate: Boolean? = null,
  @SerialName("web_commit_signoff_required")
  public val webCommitSignoffRequired: Boolean? = null,
) {
  @Serializable
  public data class Permissions(
    public val admin: Boolean,
    public val maintain: Boolean? = null,
    public val push: Boolean,
    public val triage: Boolean? = null,
    public val pull: Boolean,
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
