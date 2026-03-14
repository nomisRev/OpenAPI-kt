package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class RepositoryAdvisoryResponse(
    @SerialName("ghsa_id") val ghsaId: String,
    @SerialName("cve_id") val cveId: String?,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    val summary: String,
    val description: String?,
    val severity: Severity?,
    val author: Author,
    val publisher: Publisher,
    val identifiers: List<Identifiers>,
    val state: State,
    @SerialName("created_at") val createdAt: LocalDateTime?,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
    @SerialName("published_at") val publishedAt: LocalDateTime?,
    @SerialName("closed_at") val closedAt: LocalDateTime?,
    @SerialName("withdrawn_at") val withdrawnAt: LocalDateTime?,
    val submission: Submission?,
    val vulnerabilities: List<RepositoryAdvisoryVulnerability>?,
    val cvss: Cvss?,
    @SerialName("cvss_severities") val cvssSeverities: CvssSeveritiesResponse? = null,
    val cwes: List<Cwes>?,
    @SerialName("cwe_ids") val cweIds: List<String>?,
    val credits: List<Credits>?,
    @SerialName("credits_detailed") val creditsDetailed: List<RepositoryAdvisoryCredit>?,
    @SerialName("collaborating_users") val collaboratingUsers: List<SimpleUser>?,
    @SerialName("collaborating_teams") val collaboratingTeams: List<Team>?,
    @SerialName("private_fork") val privateFork: PrivateFork,
) {
    @Serializable
    enum class Severity {
        @SerialName("critical") Critical, @SerialName("high") High, @SerialName("medium") Medium, @SerialName("low") Low;
    }

    @Serializable
    data class Author(
        val name: String? = null,
        val email: String? = null,
        val login: String,
        val id: Long,
        @SerialName("node_id") val nodeId: String,
        @SerialName("avatar_url") val avatarUrl: String,
        @SerialName("gravatar_id") val gravatarId: String?,
        val url: String,
        @SerialName("html_url") val htmlUrl: String,
        @SerialName("followers_url") val followersUrl: String,
        @SerialName("following_url") val followingUrl: String,
        @SerialName("gists_url") val gistsUrl: String,
        @SerialName("starred_url") val starredUrl: String,
        @SerialName("subscriptions_url") val subscriptionsUrl: String,
        @SerialName("organizations_url") val organizationsUrl: String,
        @SerialName("repos_url") val reposUrl: String,
        @SerialName("events_url") val eventsUrl: String,
        @SerialName("received_events_url") val receivedEventsUrl: String,
        val type: String,
        @SerialName("site_admin") val siteAdmin: Boolean,
        @SerialName("starred_at") val starredAt: String? = null,
        @SerialName("user_view_type") val userViewType: String? = null,
    )

    @Serializable
    data class Publisher(
        val name: String? = null,
        val email: String? = null,
        val login: String,
        val id: Long,
        @SerialName("node_id") val nodeId: String,
        @SerialName("avatar_url") val avatarUrl: String,
        @SerialName("gravatar_id") val gravatarId: String?,
        val url: String,
        @SerialName("html_url") val htmlUrl: String,
        @SerialName("followers_url") val followersUrl: String,
        @SerialName("following_url") val followingUrl: String,
        @SerialName("gists_url") val gistsUrl: String,
        @SerialName("starred_url") val starredUrl: String,
        @SerialName("subscriptions_url") val subscriptionsUrl: String,
        @SerialName("organizations_url") val organizationsUrl: String,
        @SerialName("repos_url") val reposUrl: String,
        @SerialName("events_url") val eventsUrl: String,
        @SerialName("received_events_url") val receivedEventsUrl: String,
        val type: String,
        @SerialName("site_admin") val siteAdmin: Boolean,
        @SerialName("starred_at") val starredAt: String? = null,
        @SerialName("user_view_type") val userViewType: String? = null,
    )

    @Serializable
    data class Identifiers(val type: Type, val value: String) {
        @Serializable
        enum class Type {
            CVE, GHSA;
        }
    }

    @Serializable
    enum class State {
        @SerialName("published")
        Published,
        @SerialName("closed")
        Closed,
        @SerialName("withdrawn")
        Withdrawn,
        @SerialName("draft")
        Draft,
        @SerialName("triage")
        Triage;
    }

    @Serializable
    @JvmInline
    value class Submission(val accepted: Boolean)

    @Serializable
    data class Cvss(@SerialName("vector_string") val vectorString: String?, val score: Double?)

    @Serializable
    data class Cwes(@SerialName("cwe_id") val cweId: String, val name: String)

    @Serializable
    data class Credits(val login: String? = null, val type: SecurityAdvisoryCreditTypes? = null)

    @Serializable
    data class PrivateFork(
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
        @SerialName("stargazers_url") val stargazersUrl: String,
        @SerialName("statuses_url") val statusesUrl: String,
        @SerialName("subscribers_url") val subscribersUrl: String,
        @SerialName("subscription_url") val subscriptionUrl: String,
        @SerialName("tags_url") val tagsUrl: String,
        @SerialName("teams_url") val teamsUrl: String,
        @SerialName("trees_url") val treesUrl: String,
        @SerialName("hooks_url") val hooksUrl: String,
    )
}
