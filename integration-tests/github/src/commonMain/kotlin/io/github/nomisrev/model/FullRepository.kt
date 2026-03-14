package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FullRepository(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val owner: SimpleUser,
    val private: Boolean,
    @SerialName("html_url") val htmlUrl: String,
    val description: String?,
    val fork: Boolean,
    val url: String,
    @SerialName("archive_url") val archiveUrl: String,
    @SerialName("assignees_url") val assigneesUrl: String,
    @SerialName("blobs_url") val blobsUrl: String,
    @SerialName("branches_url") val branchesUrl: String,
    @SerialName("collaborators_url") val collaboratorsUrl: String,
    @SerialName("comments_url") val commentsUrl: String,
    @SerialName("commits_url") val commitsUrl: String,
    @SerialName("compare_url") val compareUrl: String,
    @SerialName("contents_url") val contentsUrl: String,
    @SerialName("contributors_url") val contributorsUrl: String,
    @SerialName("deployments_url") val deploymentsUrl: String,
    @SerialName("downloads_url") val downloadsUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("forks_url") val forksUrl: String,
    @SerialName("git_commits_url") val gitCommitsUrl: String,
    @SerialName("git_refs_url") val gitRefsUrl: String,
    @SerialName("git_tags_url") val gitTagsUrl: String,
    @SerialName("git_url") val gitUrl: String,
    @SerialName("issue_comment_url") val issueCommentUrl: String,
    @SerialName("issue_events_url") val issueEventsUrl: String,
    @SerialName("issues_url") val issuesUrl: String,
    @SerialName("keys_url") val keysUrl: String,
    @SerialName("labels_url") val labelsUrl: String,
    @SerialName("languages_url") val languagesUrl: String,
    @SerialName("merges_url") val mergesUrl: String,
    @SerialName("milestones_url") val milestonesUrl: String,
    @SerialName("notifications_url") val notificationsUrl: String,
    @SerialName("pulls_url") val pullsUrl: String,
    @SerialName("releases_url") val releasesUrl: String,
    @SerialName("ssh_url") val sshUrl: String,
    @SerialName("stargazers_url") val stargazersUrl: String,
    @SerialName("statuses_url") val statusesUrl: String,
    @SerialName("subscribers_url") val subscribersUrl: String,
    @SerialName("subscription_url") val subscriptionUrl: String,
    @SerialName("tags_url") val tagsUrl: String,
    @SerialName("teams_url") val teamsUrl: String,
    @SerialName("trees_url") val treesUrl: String,
    @SerialName("clone_url") val cloneUrl: String,
    @SerialName("mirror_url") val mirrorUrl: String?,
    @SerialName("hooks_url") val hooksUrl: String,
    @SerialName("svn_url") val svnUrl: String,
    val homepage: String?,
    val language: String?,
    @SerialName("forks_count") val forksCount: Long,
    @SerialName("stargazers_count") val stargazersCount: Long,
    @SerialName("watchers_count") val watchersCount: Long,
    val size: Long,
    @SerialName("default_branch") val defaultBranch: String,
    @SerialName("open_issues_count") val openIssuesCount: Long,
    @SerialName("is_template") val isTemplate: Boolean? = null,
    val topics: List<String>? = null,
    @SerialName("has_issues") val hasIssues: Boolean,
    @SerialName("has_projects") val hasProjects: Boolean,
    @SerialName("has_wiki") val hasWiki: Boolean,
    @SerialName("has_pages") val hasPages: Boolean,
    @SerialName("has_downloads") val hasDownloads: Boolean? = null,
    @SerialName("has_discussions") val hasDiscussions: Boolean,
    @SerialName("has_pull_requests") val hasPullRequests: Boolean? = null,
    @SerialName("pull_request_creation_policy") val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
    @SerialName("has_commit_comments") val hasCommitComments: Boolean? = null,
    val archived: Boolean,
    val disabled: Boolean,
    val visibility: String? = null,
    @SerialName("pushed_at") val pushedAt: LocalDateTime,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val permissions: Permissions? = null,
    @SerialName("allow_rebase_merge") val allowRebaseMerge: Boolean? = null,
    @SerialName("template_repository") val templateRepository: NullableRepository? = null,
    @SerialName("temp_clone_token") val tempCloneToken: String? = null,
    @SerialName("allow_squash_merge") val allowSquashMerge: Boolean? = null,
    @SerialName("allow_auto_merge") val allowAutoMerge: Boolean? = null,
    @SerialName("delete_branch_on_merge") val deleteBranchOnMerge: Boolean? = null,
    @SerialName("allow_merge_commit") val allowMergeCommit: Boolean? = null,
    @SerialName("allow_update_branch") val allowUpdateBranch: Boolean? = null,
    @SerialName("use_squash_pr_title_as_default") val useSquashPrTitleAsDefault: Boolean? = null,
    @SerialName("squash_merge_commit_title") val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
    @SerialName("squash_merge_commit_message") val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
    @SerialName("merge_commit_title") val mergeCommitTitle: MergeCommitTitle? = null,
    @SerialName("merge_commit_message") val mergeCommitMessage: MergeCommitMessage? = null,
    @SerialName("allow_forking") val allowForking: Boolean? = null,
    @SerialName("web_commit_signoff_required") val webCommitSignoffRequired: Boolean? = null,
    @SerialName("subscribers_count") val subscribersCount: Long,
    @SerialName("network_count") val networkCount: Long,
    val license: NullableLicenseSimple?,
    val organization: NullableSimpleUser? = null,
    val parent: Repository? = null,
    val source: Repository? = null,
    val forks: Long,
    @SerialName("master_branch") val masterBranch: String? = null,
    @SerialName("open_issues") val openIssues: Long,
    val watchers: Long,
    @SerialName("anonymous_access_enabled") val anonymousAccessEnabled: Boolean? = null,
    @SerialName("code_of_conduct") val codeOfConduct: CodeOfConductSimple? = null,
    @SerialName("security_and_analysis") val securityAndAnalysis: SecurityAndAnalysis? = null,
    @SerialName("custom_properties") val customProperties: JsonElement? = null,
) {
    @Serializable
    enum class PullRequestCreationPolicy {
        @SerialName("all") All, @SerialName("collaborators_only") CollaboratorsOnly;
    }

    @Serializable
    data class Permissions(
        val admin: Boolean,
        val maintain: Boolean? = null,
        val push: Boolean,
        val triage: Boolean? = null,
        val pull: Boolean,
    )

    @Serializable
    enum class SquashMergeCommitTitle {
        @SerialName("PR_TITLE") PRTITLE, @SerialName("COMMIT_OR_PR_TITLE") COMMITORPRTITLE;
    }

    @Serializable
    enum class SquashMergeCommitMessage {
        @SerialName("PR_BODY") PRBODY, @SerialName("COMMIT_MESSAGES") COMMITMESSAGES, BLANK;
    }

    @Serializable
    enum class MergeCommitTitle {
        @SerialName("PR_TITLE") PRTITLE, @SerialName("MERGE_MESSAGE") MERGEMESSAGE;
    }

    @Serializable
    enum class MergeCommitMessage {
        @SerialName("PR_BODY") PRBODY, @SerialName("PR_TITLE") PRTITLE, BLANK;
    }
}
