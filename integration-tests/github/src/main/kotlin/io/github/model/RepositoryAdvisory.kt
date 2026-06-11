package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A repository security advisory.
 */
@Serializable
public data class RepositoryAdvisory(
  @SerialName("ghsa_id")
  public val ghsaId: String,
  @SerialName("cve_id")
  public val cveId: String?,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val summary: String,
  public val description: String?,
  public val severity: Severity?,
  public val author: Author,
  public val publisher: Publisher,
  public val identifiers: List<Identifiers>,
  public val state: State,
  @SerialName("created_at")
  public val createdAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  @SerialName("published_at")
  public val publishedAt: Instant?,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("withdrawn_at")
  public val withdrawnAt: Instant?,
  public val submission: Submission?,
  public val vulnerabilities: List<RepositoryAdvisoryVulnerability>?,
  public val cvss: Cvss?,
  @SerialName("cvss_severities")
  public val cvssSeverities: CvssSeverities? = null,
  public val cwes: List<Cwes>?,
  @SerialName("cwe_ids")
  public val cweIds: List<String>?,
  public val credits: List<Credits>?,
  @SerialName("credits_detailed")
  public val creditsDetailed: List<RepositoryAdvisoryCredit>?,
  @SerialName("collaborating_users")
  public val collaboratingUsers: List<SimpleUser>?,
  @SerialName("collaborating_teams")
  public val collaboratingTeams: List<Team>?,
  @SerialName("private_fork")
  public val privateFork: PrivateFork,
) {
  /**
   * A GitHub user.
   */
  @Serializable
  public data class Author(
    public val name: String? = null,
    public val email: String? = null,
    public val login: String,
    public val id: Long,
    @SerialName("node_id")
    public val nodeId: String,
    @SerialName("avatar_url")
    public val avatarUrl: String,
    @SerialName("gravatar_id")
    public val gravatarId: String?,
    public val url: String,
    @SerialName("html_url")
    public val htmlUrl: String,
    @SerialName("followers_url")
    public val followersUrl: String,
    @SerialName("following_url")
    public val followingUrl: String,
    @SerialName("gists_url")
    public val gistsUrl: String,
    @SerialName("starred_url")
    public val starredUrl: String,
    @SerialName("subscriptions_url")
    public val subscriptionsUrl: String,
    @SerialName("organizations_url")
    public val organizationsUrl: String,
    @SerialName("repos_url")
    public val reposUrl: String,
    @SerialName("events_url")
    public val eventsUrl: String,
    @SerialName("received_events_url")
    public val receivedEventsUrl: String,
    public val type: String,
    @SerialName("site_admin")
    public val siteAdmin: Boolean,
    @SerialName("starred_at")
    public val starredAt: String? = null,
    @SerialName("user_view_type")
    public val userViewType: String? = null,
  )

  @Serializable
  public data class Credits(
    public val login: String? = null,
    public val type: SecurityAdvisoryCreditTypes? = null,
  )

  @Serializable
  public data class Cvss(
    @SerialName("vector_string")
    public val vectorString: String?,
    public val score: Double?,
  )

  @Serializable
  public data class Cwes(
    @SerialName("cwe_id")
    public val cweId: String,
    public val name: String,
  )

  @Serializable
  public data class Identifiers(
    public val type: Type,
    public val `value`: String,
  ) {
    @Serializable
    public enum class Type {
      CVE,
      GHSA,
    }
  }

  /**
   * A GitHub repository.
   */
  @Serializable
  public data class PrivateFork(
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

  /**
   * A GitHub user.
   */
  @Serializable
  public data class Publisher(
    public val name: String? = null,
    public val email: String? = null,
    public val login: String,
    public val id: Long,
    @SerialName("node_id")
    public val nodeId: String,
    @SerialName("avatar_url")
    public val avatarUrl: String,
    @SerialName("gravatar_id")
    public val gravatarId: String?,
    public val url: String,
    @SerialName("html_url")
    public val htmlUrl: String,
    @SerialName("followers_url")
    public val followersUrl: String,
    @SerialName("following_url")
    public val followingUrl: String,
    @SerialName("gists_url")
    public val gistsUrl: String,
    @SerialName("starred_url")
    public val starredUrl: String,
    @SerialName("subscriptions_url")
    public val subscriptionsUrl: String,
    @SerialName("organizations_url")
    public val organizationsUrl: String,
    @SerialName("repos_url")
    public val reposUrl: String,
    @SerialName("events_url")
    public val eventsUrl: String,
    @SerialName("received_events_url")
    public val receivedEventsUrl: String,
    public val type: String,
    @SerialName("site_admin")
    public val siteAdmin: Boolean,
    @SerialName("starred_at")
    public val starredAt: String? = null,
    @SerialName("user_view_type")
    public val userViewType: String? = null,
  )

  @Serializable
  public enum class Severity(
    public val `value`: String,
  ) {
    @SerialName("critical")
    Critical("critical"),
    @SerialName("high")
    High("high"),
    @SerialName("medium")
    Medium("medium"),
    @SerialName("low")
    Low("low"),
    ;
  }

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("published")
    Published("published"),
    @SerialName("closed")
    Closed("closed"),
    @SerialName("withdrawn")
    Withdrawn("withdrawn"),
    @SerialName("draft")
    Draft("draft"),
    @SerialName("triage")
    Triage("triage"),
    ;
  }

  @JvmInline
  @Serializable
  public value class Submission(
    public val accepted: Boolean,
  )
}
