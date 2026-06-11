package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A team's access to a repository.
 */
@Serializable
public data class TeamRepository(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  @SerialName("full_name")
  public val fullName: String,
  public val license: NullableLicenseSimple?,
  public val forks: Long,
  public val permissions: Permissions? = null,
  @SerialName("role_name")
  public val roleName: String? = null,
  public val owner: NullableSimpleUser?,
  @Required
  public val `private`: Boolean = false,
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
  public val gitUrl: String,
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
  public val sshUrl: String,
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
  public val cloneUrl: String,
  @SerialName("mirror_url")
  public val mirrorUrl: String?,
  @SerialName("hooks_url")
  public val hooksUrl: String,
  @SerialName("svn_url")
  public val svnUrl: String,
  public val homepage: String?,
  public val language: String?,
  @SerialName("forks_count")
  public val forksCount: Long,
  @SerialName("stargazers_count")
  public val stargazersCount: Long,
  @SerialName("watchers_count")
  public val watchersCount: Long,
  public val size: Long,
  @SerialName("default_branch")
  public val defaultBranch: String,
  @SerialName("open_issues_count")
  public val openIssuesCount: Long,
  @SerialName("is_template")
  public val isTemplate: Boolean? = null,
  public val topics: List<String>? = null,
  @SerialName("has_issues")
  @Required
  public val hasIssues: Boolean = true,
  @SerialName("has_projects")
  @Required
  public val hasProjects: Boolean = true,
  @SerialName("has_wiki")
  @Required
  public val hasWiki: Boolean = true,
  @SerialName("has_pages")
  public val hasPages: Boolean,
  @SerialName("has_downloads")
  @Required
  public val hasDownloads: Boolean = true,
  @Required
  public val archived: Boolean = false,
  public val disabled: Boolean,
  public val visibility: String? = null,
  @SerialName("pushed_at")
  public val pushedAt: Instant?,
  @SerialName("created_at")
  public val createdAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  @SerialName("allow_rebase_merge")
  public val allowRebaseMerge: Boolean? = null,
  @SerialName("temp_clone_token")
  public val tempCloneToken: String? = null,
  @SerialName("allow_squash_merge")
  public val allowSquashMerge: Boolean? = null,
  @SerialName("allow_auto_merge")
  public val allowAutoMerge: Boolean? = null,
  @SerialName("delete_branch_on_merge")
  public val deleteBranchOnMerge: Boolean? = null,
  @SerialName("allow_merge_commit")
  public val allowMergeCommit: Boolean? = null,
  @SerialName("allow_forking")
  public val allowForking: Boolean? = null,
  @SerialName("web_commit_signoff_required")
  public val webCommitSignoffRequired: Boolean? = null,
  @SerialName("subscribers_count")
  public val subscribersCount: Long? = null,
  @SerialName("network_count")
  public val networkCount: Long? = null,
  @SerialName("open_issues")
  public val openIssues: Long,
  public val watchers: Long,
  @SerialName("master_branch")
  public val masterBranch: String? = null,
) {
  @Serializable
  public data class Permissions(
    public val admin: Boolean,
    public val pull: Boolean,
    public val triage: Boolean? = null,
    public val push: Boolean,
    public val maintain: Boolean? = null,
  )
}
