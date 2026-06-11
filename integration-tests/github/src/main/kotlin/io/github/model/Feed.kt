package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Feed
 */
@Serializable
public data class Feed(
  @SerialName("timeline_url")
  public val timelineUrl: String,
  @SerialName("user_url")
  public val userUrl: String,
  @SerialName("current_user_public_url")
  public val currentUserPublicUrl: String? = null,
  @SerialName("current_user_url")
  public val currentUserUrl: String? = null,
  @SerialName("current_user_actor_url")
  public val currentUserActorUrl: String? = null,
  @SerialName("current_user_organization_url")
  public val currentUserOrganizationUrl: String? = null,
  @SerialName("current_user_organization_urls")
  public val currentUserOrganizationUrls: List<String>? = null,
  @SerialName("security_advisories_url")
  public val securityAdvisoriesUrl: String? = null,
  @SerialName("repository_discussions_url")
  public val repositoryDiscussionsUrl: String? = null,
  @SerialName("repository_discussions_category_url")
  public val repositoryDiscussionsCategoryUrl: String? = null,
  @SerialName("_links")
  public val links: Links,
) {
  @Serializable
  public data class Links(
    public val timeline: LinkWithType,
    public val user: LinkWithType,
    @SerialName("security_advisories")
    public val securityAdvisories: LinkWithType? = null,
    @SerialName("current_user")
    public val currentUser: LinkWithType? = null,
    @SerialName("current_user_public")
    public val currentUserPublic: LinkWithType? = null,
    @SerialName("current_user_actor")
    public val currentUserActor: LinkWithType? = null,
    @SerialName("current_user_organization")
    public val currentUserOrganization: LinkWithType? = null,
    @SerialName("current_user_organizations")
    public val currentUserOrganizations: List<LinkWithType>? = null,
    @SerialName("repository_discussions")
    public val repositoryDiscussions: LinkWithType? = null,
    @SerialName("repository_discussions_category")
    public val repositoryDiscussionsCategory: LinkWithType? = null,
  )
}
