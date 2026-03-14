package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Feed(
    @SerialName("timeline_url") val timelineUrl: String,
    @SerialName("user_url") val userUrl: String,
    @SerialName("current_user_public_url") val currentUserPublicUrl: String? = null,
    @SerialName("current_user_url") val currentUserUrl: String? = null,
    @SerialName("current_user_actor_url") val currentUserActorUrl: String? = null,
    @SerialName("current_user_organization_url") val currentUserOrganizationUrl: String? = null,
    @SerialName("current_user_organization_urls") val currentUserOrganizationUrls: List<String>? = null,
    @SerialName("security_advisories_url") val securityAdvisoriesUrl: String? = null,
    @SerialName("repository_discussions_url") val repositoryDiscussionsUrl: String? = null,
    @SerialName("repository_discussions_category_url") val repositoryDiscussionsCategoryUrl: String? = null,
    @SerialName("_links") val links: Links,
) {
    @Serializable
    data class Links(
        val timeline: LinkWithType,
        val user: LinkWithType,
        @SerialName("security_advisories") val securityAdvisories: LinkWithType? = null,
        @SerialName("current_user") val currentUser: LinkWithType? = null,
        @SerialName("current_user_public") val currentUserPublic: LinkWithType? = null,
        @SerialName("current_user_actor") val currentUserActor: LinkWithType? = null,
        @SerialName("current_user_organization") val currentUserOrganization: LinkWithType? = null,
        @SerialName("current_user_organizations") val currentUserOrganizations: List<LinkWithType>? = null,
        @SerialName("repository_discussions") val repositoryDiscussions: LinkWithType? = null,
        @SerialName("repository_discussions_category") val repositoryDiscussionsCategory: LinkWithType? = null,
    )
}
