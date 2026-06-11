package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ForkEvent(
  public val action: String,
  public val forkee: Forkee,
) {
  @Serializable
  public data class Forkee(
    public val id: Long? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val name: String? = null,
    @SerialName("full_name")
    public val fullName: String? = null,
    public val `private`: Boolean? = null,
    public val owner: SimpleUser? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    public val description: String? = null,
    public val fork: Boolean? = null,
    public val url: String? = null,
    @SerialName("forks_url")
    public val forksUrl: String? = null,
    @SerialName("keys_url")
    public val keysUrl: String? = null,
    @SerialName("collaborators_url")
    public val collaboratorsUrl: String? = null,
    @SerialName("teams_url")
    public val teamsUrl: String? = null,
    @SerialName("hooks_url")
    public val hooksUrl: String? = null,
    @SerialName("issue_events_url")
    public val issueEventsUrl: String? = null,
    @SerialName("events_url")
    public val eventsUrl: String? = null,
    @SerialName("assignees_url")
    public val assigneesUrl: String? = null,
    @SerialName("branches_url")
    public val branchesUrl: String? = null,
    @SerialName("tags_url")
    public val tagsUrl: String? = null,
    @SerialName("blobs_url")
    public val blobsUrl: String? = null,
    @SerialName("git_tags_url")
    public val gitTagsUrl: String? = null,
    @SerialName("git_refs_url")
    public val gitRefsUrl: String? = null,
    @SerialName("trees_url")
    public val treesUrl: String? = null,
    @SerialName("statuses_url")
    public val statusesUrl: String? = null,
    @SerialName("languages_url")
    public val languagesUrl: String? = null,
    @SerialName("stargazers_url")
    public val stargazersUrl: String? = null,
    @SerialName("contributors_url")
    public val contributorsUrl: String? = null,
    @SerialName("subscribers_url")
    public val subscribersUrl: String? = null,
    @SerialName("subscription_url")
    public val subscriptionUrl: String? = null,
    @SerialName("commits_url")
    public val commitsUrl: String? = null,
    @SerialName("git_commits_url")
    public val gitCommitsUrl: String? = null,
    @SerialName("comments_url")
    public val commentsUrl: String? = null,
    @SerialName("issue_comment_url")
    public val issueCommentUrl: String? = null,
    @SerialName("contents_url")
    public val contentsUrl: String? = null,
    @SerialName("compare_url")
    public val compareUrl: String? = null,
    @SerialName("merges_url")
    public val mergesUrl: String? = null,
    @SerialName("archive_url")
    public val archiveUrl: String? = null,
    @SerialName("downloads_url")
    public val downloadsUrl: String? = null,
    @SerialName("issues_url")
    public val issuesUrl: String? = null,
    @SerialName("pulls_url")
    public val pullsUrl: String? = null,
    @SerialName("milestones_url")
    public val milestonesUrl: String? = null,
    @SerialName("notifications_url")
    public val notificationsUrl: String? = null,
    @SerialName("labels_url")
    public val labelsUrl: String? = null,
    @SerialName("releases_url")
    public val releasesUrl: String? = null,
    @SerialName("deployments_url")
    public val deploymentsUrl: String? = null,
    @SerialName("created_at")
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    public val updatedAt: Instant? = null,
    @SerialName("pushed_at")
    public val pushedAt: Instant? = null,
    @SerialName("git_url")
    public val gitUrl: String? = null,
    @SerialName("ssh_url")
    public val sshUrl: String? = null,
    @SerialName("clone_url")
    public val cloneUrl: String? = null,
    @SerialName("svn_url")
    public val svnUrl: String? = null,
    public val homepage: String? = null,
    public val size: Long? = null,
    @SerialName("stargazers_count")
    public val stargazersCount: Long? = null,
    @SerialName("watchers_count")
    public val watchersCount: Long? = null,
    public val language: String? = null,
    @SerialName("has_issues")
    public val hasIssues: Boolean? = null,
    @SerialName("has_projects")
    public val hasProjects: Boolean? = null,
    @SerialName("has_downloads")
    public val hasDownloads: Boolean? = null,
    @SerialName("has_wiki")
    public val hasWiki: Boolean? = null,
    @SerialName("has_pages")
    public val hasPages: Boolean? = null,
    @SerialName("has_discussions")
    public val hasDiscussions: Boolean? = null,
    @SerialName("has_pull_requests")
    public val hasPullRequests: Boolean? = null,
    @SerialName("pull_request_creation_policy")
    public val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
    @SerialName("has_commit_comments")
    public val hasCommitComments: Boolean? = null,
    @SerialName("forks_count")
    public val forksCount: Long? = null,
    @SerialName("mirror_url")
    public val mirrorUrl: String? = null,
    public val archived: Boolean? = null,
    public val disabled: Boolean? = null,
    @SerialName("open_issues_count")
    public val openIssuesCount: Long? = null,
    public val license: NullableLicenseSimple? = null,
    @SerialName("allow_forking")
    public val allowForking: Boolean? = null,
    @SerialName("is_template")
    public val isTemplate: Boolean? = null,
    @SerialName("web_commit_signoff_required")
    public val webCommitSignoffRequired: Boolean? = null,
    public val topics: List<String>? = null,
    public val visibility: String? = null,
    public val forks: Long? = null,
    @SerialName("open_issues")
    public val openIssues: Long? = null,
    public val watchers: Long? = null,
    @SerialName("default_branch")
    public val defaultBranch: String? = null,
    public val `public`: Boolean? = null,
  ) {
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
}
