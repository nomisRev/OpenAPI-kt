package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Feed
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Feed(
  @SerialName("timeline_url")
  public val timelineUrl: String,
  @SerialName("user_url")
  public val userUrl: String,
  @SerialName("current_user_public_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentUserPublicUrl: String? = null,
  @SerialName("current_user_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentUserUrl: String? = null,
  @SerialName("current_user_actor_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentUserActorUrl: String? = null,
  @SerialName("current_user_organization_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentUserOrganizationUrl: String? = null,
  @SerialName("current_user_organization_urls")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentUserOrganizationUrls: List<String>? = null,
  @SerialName("security_advisories_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val securityAdvisoriesUrl: String? = null,
  @SerialName("repository_discussions_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositoryDiscussionsUrl: String? = null,
  @SerialName("repository_discussions_category_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositoryDiscussionsCategoryUrl: String? = null,
  @SerialName("_links")
  public val links: Links,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Links(
    public val timeline: LinkWithType,
    public val user: LinkWithType,
    @SerialName("security_advisories")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val securityAdvisories: LinkWithType? = null,
    @SerialName("current_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val currentUser: LinkWithType? = null,
    @SerialName("current_user_public")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val currentUserPublic: LinkWithType? = null,
    @SerialName("current_user_actor")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val currentUserActor: LinkWithType? = null,
    @SerialName("current_user_organization")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val currentUserOrganization: LinkWithType? = null,
    @SerialName("current_user_organizations")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val currentUserOrganizations: List<LinkWithType>? = null,
    @SerialName("repository_discussions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repositoryDiscussions: LinkWithType? = null,
    @SerialName("repository_discussions_category")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repositoryDiscussionsCategory: LinkWithType? = null,
  )
}
