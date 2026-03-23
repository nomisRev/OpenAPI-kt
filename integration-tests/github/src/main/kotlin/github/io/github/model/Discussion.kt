package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Discussion in a repository.
 */
@Serializable
public data class Discussion(
  @SerialName("active_lock_reason")
  public val activeLockReason: String?,
  @SerialName("answer_chosen_at")
  public val answerChosenAt: String?,
  @SerialName("answer_chosen_by")
  public val answerChosenBy: AnswerChosenBy?,
  @SerialName("answer_html_url")
  public val answerHtmlUrl: String?,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation? = null,
  public val body: String,
  public val category: Category,
  public val comments: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val id: Long,
  public val locked: Boolean,
  @SerialName("node_id")
  public val nodeId: String,
  public val number: Long,
  public val reactions: Reactions? = null,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  public val state: State,
  @SerialName("state_reason")
  public val stateReason: StateReason?,
  @SerialName("timeline_url")
  public val timelineUrl: String? = null,
  public val title: String,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val user: User?,
  public val labels: List<Label>? = null,
) {
  @Serializable
  public data class AnswerChosenBy(
    @SerialName("avatar_url")
    public val avatarUrl: String? = null,
    public val deleted: Boolean? = null,
    public val email: String? = null,
    @SerialName("events_url")
    public val eventsUrl: String? = null,
    @SerialName("followers_url")
    public val followersUrl: String? = null,
    @SerialName("following_url")
    public val followingUrl: String? = null,
    @SerialName("gists_url")
    public val gistsUrl: String? = null,
    @SerialName("gravatar_id")
    public val gravatarId: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    public val id: Long,
    public val login: String,
    public val name: String? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    @SerialName("organizations_url")
    public val organizationsUrl: String? = null,
    @SerialName("received_events_url")
    public val receivedEventsUrl: String? = null,
    @SerialName("repos_url")
    public val reposUrl: String? = null,
    @SerialName("site_admin")
    public val siteAdmin: Boolean? = null,
    @SerialName("starred_url")
    public val starredUrl: String? = null,
    @SerialName("subscriptions_url")
    public val subscriptionsUrl: String? = null,
    public val type: Type? = null,
    public val url: String? = null,
    @SerialName("user_view_type")
    public val userViewType: String? = null,
  ) {
    @Serializable
    public enum class Type {
      Bot,
      User,
      Organization,
    }
  }

  @Serializable
  public enum class AuthorAssociation(
    public val `value`: String,
  ) {
    COLLABORATOR("COLLABORATOR"),
    CONTRIBUTOR("CONTRIBUTOR"),
    @SerialName("FIRST_TIMER")
    FIRSTTIMER("FIRST_TIMER"),
    @SerialName("FIRST_TIME_CONTRIBUTOR")
    FIRSTTIMECONTRIBUTOR("FIRST_TIME_CONTRIBUTOR"),
    MANNEQUIN("MANNEQUIN"),
    MEMBER("MEMBER"),
    NONE("NONE"),
    OWNER("OWNER"),
    ;
  }

  @Serializable
  public data class Category(
    @SerialName("created_at")
    public val createdAt: Instant,
    public val description: String,
    public val emoji: String,
    public val id: Long,
    @SerialName("is_answerable")
    public val isAnswerable: Boolean,
    public val name: String,
    @SerialName("node_id")
    public val nodeId: String? = null,
    @SerialName("repository_id")
    public val repositoryId: Long,
    public val slug: String,
    @SerialName("updated_at")
    public val updatedAt: String,
  )

  @Serializable
  public data class Reactions(
    public val `+1`: Long,
    public val `-1`: Long,
    public val confused: Long,
    public val eyes: Long,
    public val heart: Long,
    public val hooray: Long,
    public val laugh: Long,
    public val rocket: Long,
    @SerialName("total_count")
    public val totalCount: Long,
    public val url: String,
  )

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("open")
    Open("open"),
    @SerialName("closed")
    Closed("closed"),
    @SerialName("locked")
    Locked("locked"),
    @SerialName("converting")
    Converting("converting"),
    @SerialName("transferring")
    Transferring("transferring"),
    ;
  }

  @Serializable
  public enum class StateReason(
    public val `value`: String,
  ) {
    @SerialName("resolved")
    Resolved("resolved"),
    @SerialName("outdated")
    Outdated("outdated"),
    @SerialName("duplicate")
    Duplicate("duplicate"),
    @SerialName("reopened")
    Reopened("reopened"),
    ;
  }

  @Serializable
  public data class User(
    @SerialName("avatar_url")
    public val avatarUrl: String? = null,
    public val deleted: Boolean? = null,
    public val email: String? = null,
    @SerialName("events_url")
    public val eventsUrl: String? = null,
    @SerialName("followers_url")
    public val followersUrl: String? = null,
    @SerialName("following_url")
    public val followingUrl: String? = null,
    @SerialName("gists_url")
    public val gistsUrl: String? = null,
    @SerialName("gravatar_id")
    public val gravatarId: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    public val id: Long,
    public val login: String,
    public val name: String? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    @SerialName("organizations_url")
    public val organizationsUrl: String? = null,
    @SerialName("received_events_url")
    public val receivedEventsUrl: String? = null,
    @SerialName("repos_url")
    public val reposUrl: String? = null,
    @SerialName("site_admin")
    public val siteAdmin: Boolean? = null,
    @SerialName("starred_url")
    public val starredUrl: String? = null,
    @SerialName("subscriptions_url")
    public val subscriptionsUrl: String? = null,
    public val type: Type? = null,
    public val url: String? = null,
    @SerialName("user_view_type")
    public val userViewType: String? = null,
  ) {
    @Serializable
    public enum class Type {
      Bot,
      User,
      Organization,
    }
  }
}
