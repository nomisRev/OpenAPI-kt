package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ForkEvent(
  public val action: String,
  public val forkee: Forkee,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Forkee(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @SerialName("full_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fullName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `private`: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: SimpleUser? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    public val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fork: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @SerialName("forks_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val forksUrl: String? = null,
    @SerialName("keys_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val keysUrl: String? = null,
    @SerialName("collaborators_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val collaboratorsUrl: String? = null,
    @SerialName("teams_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val teamsUrl: String? = null,
    @SerialName("hooks_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hooksUrl: String? = null,
    @SerialName("issue_events_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issueEventsUrl: String? = null,
    @SerialName("events_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val eventsUrl: String? = null,
    @SerialName("assignees_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val assigneesUrl: String? = null,
    @SerialName("branches_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchesUrl: String? = null,
    @SerialName("tags_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tagsUrl: String? = null,
    @SerialName("blobs_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val blobsUrl: String? = null,
    @SerialName("git_tags_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val gitTagsUrl: String? = null,
    @SerialName("git_refs_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val gitRefsUrl: String? = null,
    @SerialName("trees_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val treesUrl: String? = null,
    @SerialName("statuses_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val statusesUrl: String? = null,
    @SerialName("languages_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val languagesUrl: String? = null,
    @SerialName("stargazers_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val stargazersUrl: String? = null,
    @SerialName("contributors_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val contributorsUrl: String? = null,
    @SerialName("subscribers_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val subscribersUrl: String? = null,
    @SerialName("subscription_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val subscriptionUrl: String? = null,
    @SerialName("commits_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val commitsUrl: String? = null,
    @SerialName("git_commits_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val gitCommitsUrl: String? = null,
    @SerialName("comments_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val commentsUrl: String? = null,
    @SerialName("issue_comment_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issueCommentUrl: String? = null,
    @SerialName("contents_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val contentsUrl: String? = null,
    @SerialName("compare_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val compareUrl: String? = null,
    @SerialName("merges_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val mergesUrl: String? = null,
    @SerialName("archive_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val archiveUrl: String? = null,
    @SerialName("downloads_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val downloadsUrl: String? = null,
    @SerialName("issues_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issuesUrl: String? = null,
    @SerialName("pulls_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val pullsUrl: String? = null,
    @SerialName("milestones_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val milestonesUrl: String? = null,
    @SerialName("notifications_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val notificationsUrl: String? = null,
    @SerialName("labels_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val labelsUrl: String? = null,
    @SerialName("releases_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val releasesUrl: String? = null,
    @SerialName("deployments_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val deploymentsUrl: String? = null,
    @SerialName("created_at")
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    public val updatedAt: Instant? = null,
    @SerialName("pushed_at")
    public val pushedAt: Instant? = null,
    @SerialName("git_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val gitUrl: String? = null,
    @SerialName("ssh_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sshUrl: String? = null,
    @SerialName("clone_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val cloneUrl: String? = null,
    @SerialName("svn_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val svnUrl: String? = null,
    public val homepage: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val size: Long? = null,
    @SerialName("stargazers_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val stargazersCount: Long? = null,
    @SerialName("watchers_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val watchersCount: Long? = null,
    public val language: String? = null,
    @SerialName("has_issues")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasIssues: Boolean? = null,
    @SerialName("has_projects")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasProjects: Boolean? = null,
    @SerialName("has_downloads")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasDownloads: Boolean? = null,
    @SerialName("has_wiki")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasWiki: Boolean? = null,
    @SerialName("has_pages")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasPages: Boolean? = null,
    @SerialName("has_discussions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasDiscussions: Boolean? = null,
    @SerialName("has_pull_requests")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasPullRequests: Boolean? = null,
    @SerialName("pull_request_creation_policy")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
    @SerialName("has_commit_comments")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasCommitComments: Boolean? = null,
    @SerialName("forks_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val forksCount: Long? = null,
    @SerialName("mirror_url")
    public val mirrorUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val disabled: Boolean? = null,
    @SerialName("open_issues_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val openIssuesCount: Long? = null,
    public val license: NullableLicenseSimple? = null,
    @SerialName("allow_forking")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val allowForking: Boolean? = null,
    @SerialName("is_template")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isTemplate: Boolean? = null,
    @SerialName("web_commit_signoff_required")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val webCommitSignoffRequired: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val topics: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibility: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val forks: Long? = null,
    @SerialName("open_issues")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val openIssues: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val watchers: Long? = null,
    @SerialName("default_branch")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultBranch: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
