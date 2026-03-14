package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.TeamFull
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.OrganizationInvitation
import io.github.nomisrev.model.SimpleUser
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.TeamMembership
import io.github.nomisrev.model.MinimalRepository
import io.github.nomisrev.model.TeamRepository
import io.github.nomisrev.model.Team
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.http.contentType

interface Teams {
    val invitations: Teams.Invitations

    val members: Teams.Members

    val memberships: Teams.Memberships

    val repos: Teams.Repos

    val teams: Teams.TeamsApi

    @Serializable
    data class TeamsUpdateLegacyBody(
        val name: String,
        val description: String? = null,
        val privacy: Privacy? = null,
        @SerialName("notification_setting") val notificationSetting: NotificationSetting? = null,
        val permission: Permission? = null,
        @SerialName("parent_team_id") val parentTeamId: Long? = null,
    ) {
        @Serializable
        enum class Privacy {
            @SerialName("secret") Secret, @SerialName("closed") Closed;
        }

        @Serializable
        enum class NotificationSetting {
            @SerialName("notifications_enabled")
            NotificationsEnabled,
            @SerialName("notifications_disabled")
            NotificationsDisabled;
        }

        @Serializable
        enum class Permission {
            @SerialName("pull") Pull, @SerialName("push") Push, @SerialName("admin") Admin;
        }
    }

    sealed interface TeamsGetLegacyResult {
        data class OK(val value: TeamFull) : TeamsGetLegacyResult

        data class NotFound(val value: BasicError) : TeamsGetLegacyResult
    }

    @Deprecated("Deprecated by the API provider")
    suspend fun teamsGetLegacy(
        teamId: Long,
    ): TeamsGetLegacyResult

    sealed interface TeamsDeleteLegacyResult {
        data object NoContent : TeamsDeleteLegacyResult

        data class NotFound(val value: BasicError) : TeamsDeleteLegacyResult

        data class UnprocessableEntity(val value: ValidationError) : TeamsDeleteLegacyResult
    }

    @Deprecated("Deprecated by the API provider")
    suspend fun teamsDeleteLegacy(
        teamId: Long,
    ): TeamsDeleteLegacyResult

    sealed interface TeamsUpdateLegacyResult {
        data class OK(val value: TeamFull) : TeamsUpdateLegacyResult

        data class Created(val value: TeamFull) : TeamsUpdateLegacyResult

        data class Forbidden(val value: BasicError) : TeamsUpdateLegacyResult

        data class NotFound(val value: BasicError) : TeamsUpdateLegacyResult

        data class UnprocessableEntity(val value: ValidationError) : TeamsUpdateLegacyResult
    }

    @Deprecated("Deprecated by the API provider")
    suspend fun teamsUpdateLegacy(
        teamId: Long,
        body: TeamsUpdateLegacyBody,
    ): TeamsUpdateLegacyResult

    interface Invitations {
        @Deprecated("Deprecated by the API provider")
        suspend fun teamsListPendingInvitationsLegacy(
            teamId: Long,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<OrganizationInvitation>
    }

    interface Members {
        @Serializable
        enum class Role {
            @SerialName("member") Member, @SerialName("maintainer") Maintainer, @SerialName("all") All;
        }

        sealed interface TeamsListMembersLegacyResult {
            data class OK(val value: List<SimpleUser>) : TeamsListMembersLegacyResult

            data class NotFound(val value: BasicError) : TeamsListMembersLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsListMembersLegacy(
            teamId: Long,
            page: Long = 1L,
            perPage: Long = 30L,
            role: Role = Role.All,
        ): TeamsListMembersLegacyResult

        sealed interface TeamsGetMemberLegacyResult {
            data object NoContent : TeamsGetMemberLegacyResult

            data object NotFound : TeamsGetMemberLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsGetMemberLegacy(
            teamId: Long,
            username: String,
        ): TeamsGetMemberLegacyResult

        sealed interface TeamsAddMemberLegacyResult {
            data object NoContent : TeamsAddMemberLegacyResult

            data class Forbidden(val value: BasicError) : TeamsAddMemberLegacyResult

            data object NotFound : TeamsAddMemberLegacyResult

            data object UnprocessableEntity : TeamsAddMemberLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsAddMemberLegacy(
            teamId: Long,
            username: String,
        ): TeamsAddMemberLegacyResult

        sealed interface TeamsRemoveMemberLegacyResult {
            data object NoContent : TeamsRemoveMemberLegacyResult

            data object NotFound : TeamsRemoveMemberLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsRemoveMemberLegacy(
            teamId: Long,
            username: String,
        ): TeamsRemoveMemberLegacyResult
    }

    interface Memberships {
        @Serializable
        @JvmInline
        value class TeamsAddOrUpdateMembershipForUserLegacyBody(val role: Role? = null) {
            @Serializable
            enum class Role {
                @SerialName("member") Member, @SerialName("maintainer") Maintainer;
            }
        }

        sealed interface TeamsGetMembershipForUserLegacyResult {
            data class OK(val value: TeamMembership) : TeamsGetMembershipForUserLegacyResult

            data class NotFound(val value: BasicError) : TeamsGetMembershipForUserLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsGetMembershipForUserLegacy(
            teamId: Long,
            username: String,
        ): TeamsGetMembershipForUserLegacyResult

        sealed interface TeamsAddOrUpdateMembershipForUserLegacyResult {
            data class OK(val value: TeamMembership) : TeamsAddOrUpdateMembershipForUserLegacyResult

            data object Forbidden : TeamsAddOrUpdateMembershipForUserLegacyResult

            data class NotFound(val value: BasicError) : TeamsAddOrUpdateMembershipForUserLegacyResult

            data object UnprocessableEntity : TeamsAddOrUpdateMembershipForUserLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsAddOrUpdateMembershipForUserLegacy(
            teamId: Long,
            username: String,
            body: TeamsAddOrUpdateMembershipForUserLegacyBody? = null,
        ): TeamsAddOrUpdateMembershipForUserLegacyResult

        sealed interface TeamsRemoveMembershipForUserLegacyResult {
            data object NoContent : TeamsRemoveMembershipForUserLegacyResult

            data object Forbidden : TeamsRemoveMembershipForUserLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsRemoveMembershipForUserLegacy(
            teamId: Long,
            username: String,
        ): TeamsRemoveMembershipForUserLegacyResult
    }

    interface Repos {
        @Serializable
        @JvmInline
        value class TeamsAddOrUpdateRepoPermissionsLegacyBody(val permission: Permission? = null) {
            @Serializable
            enum class Permission {
                @SerialName("pull") Pull, @SerialName("push") Push, @SerialName("admin") Admin;
            }
        }

        sealed interface TeamsListReposLegacyResult {
            data class OK(val value: List<MinimalRepository>) : TeamsListReposLegacyResult

            data class NotFound(val value: BasicError) : TeamsListReposLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsListReposLegacy(
            teamId: Long,
            page: Long = 1L,
            perPage: Long = 30L,
        ): TeamsListReposLegacyResult

        sealed interface TeamsCheckPermissionsForRepoLegacyResult {
            data class OK(val value: TeamRepository) : TeamsCheckPermissionsForRepoLegacyResult

            data object NoContent : TeamsCheckPermissionsForRepoLegacyResult

            data object NotFound : TeamsCheckPermissionsForRepoLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsCheckPermissionsForRepoLegacy(
            teamId: Long,
            owner: String,
            repo: String,
        ): TeamsCheckPermissionsForRepoLegacyResult

        sealed interface TeamsAddOrUpdateRepoPermissionsLegacyResult {
            data object NoContent : TeamsAddOrUpdateRepoPermissionsLegacyResult

            data class Forbidden(val value: BasicError) : TeamsAddOrUpdateRepoPermissionsLegacyResult

            data class UnprocessableEntity(val value: ValidationError) : TeamsAddOrUpdateRepoPermissionsLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsAddOrUpdateRepoPermissionsLegacy(
            teamId: Long,
            owner: String,
            repo: String,
            body: TeamsAddOrUpdateRepoPermissionsLegacyBody? = null,
        ): TeamsAddOrUpdateRepoPermissionsLegacyResult

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsRemoveRepoLegacy(
            teamId: Long,
            owner: String,
            repo: String,
        ): Unit
    }

    interface TeamsApi {
        sealed interface TeamsListChildLegacyResult {
            data class OK(val value: List<Team>) : TeamsListChildLegacyResult

            data class Forbidden(val value: BasicError) : TeamsListChildLegacyResult

            data class NotFound(val value: BasicError) : TeamsListChildLegacyResult

            data class UnprocessableEntity(val value: ValidationError) : TeamsListChildLegacyResult
        }

        @Deprecated("Deprecated by the API provider")
        suspend fun teamsListChildLegacy(
            teamId: Long,
            page: Long = 1L,
            perPage: Long = 30L,
        ): TeamsListChildLegacyResult
    }
}

internal class KtorTeams(private val client: HttpClient) : Teams {
    override val invitations: Teams.Invitations = KtorTeamsInvitations(client)

    override val members: Teams.Members = KtorTeamsMembers(client)

    override val memberships: Teams.Memberships = KtorTeamsMemberships(client)

    override val repos: Teams.Repos = KtorTeamsRepos(client)

    override val teams: Teams.TeamsApi = KtorTeamsTeamsApi(client)

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsGetLegacy(teamId: Long): Teams.TeamsGetLegacyResult {
        val response = client.get("/teams/$teamId")
        return when (response.status) {
            HttpStatusCode.OK -> Teams.TeamsGetLegacyResult.OK(response.body())
            HttpStatusCode.NotFound -> Teams.TeamsGetLegacyResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsDeleteLegacy(teamId: Long): Teams.TeamsDeleteLegacyResult {
        val response = client.delete("/teams/$teamId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Teams.TeamsDeleteLegacyResult.NoContent
            HttpStatusCode.NotFound -> Teams.TeamsDeleteLegacyResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Teams.TeamsDeleteLegacyResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsUpdateLegacy(teamId: Long, body: Teams.TeamsUpdateLegacyBody): Teams.TeamsUpdateLegacyResult {
        val response = client.patch("/teams/$teamId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Teams.TeamsUpdateLegacyResult.OK(response.body())
            HttpStatusCode.Created -> Teams.TeamsUpdateLegacyResult.Created(response.body())
            HttpStatusCode.Forbidden -> Teams.TeamsUpdateLegacyResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Teams.TeamsUpdateLegacyResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Teams.TeamsUpdateLegacyResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorTeamsInvitations(private val client: HttpClient) : Teams.Invitations {
    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsListPendingInvitationsLegacy(teamId: Long, page: Long, perPage: Long): List<OrganizationInvitation> =
        client.get("/teams/$teamId/invitations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorTeamsMembers(private val client: HttpClient) : Teams.Members {
    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsListMembersLegacy(teamId: Long, page: Long, perPage: Long, role: Teams.Members.Role): Teams.Members.TeamsListMembersLegacyResult {
        val response = client.get("/teams/$teamId/members") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("role", role)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Teams.Members.TeamsListMembersLegacyResult.OK(response.body())
            HttpStatusCode.NotFound -> Teams.Members.TeamsListMembersLegacyResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsGetMemberLegacy(teamId: Long, username: String): Teams.Members.TeamsGetMemberLegacyResult {
        val response = client.get("/teams/$teamId/members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Teams.Members.TeamsGetMemberLegacyResult.NoContent
            HttpStatusCode.NotFound -> Teams.Members.TeamsGetMemberLegacyResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsAddMemberLegacy(teamId: Long, username: String): Teams.Members.TeamsAddMemberLegacyResult {
        val response = client.put("/teams/$teamId/members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Teams.Members.TeamsAddMemberLegacyResult.NoContent
            HttpStatusCode.Forbidden -> Teams.Members.TeamsAddMemberLegacyResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Teams.Members.TeamsAddMemberLegacyResult.NotFound
            HttpStatusCode.UnprocessableEntity -> Teams.Members.TeamsAddMemberLegacyResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsRemoveMemberLegacy(teamId: Long, username: String): Teams.Members.TeamsRemoveMemberLegacyResult {
        val response = client.delete("/teams/$teamId/members/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Teams.Members.TeamsRemoveMemberLegacyResult.NoContent
            HttpStatusCode.NotFound -> Teams.Members.TeamsRemoveMemberLegacyResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorTeamsMemberships(private val client: HttpClient) : Teams.Memberships {
    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsGetMembershipForUserLegacy(teamId: Long, username: String): Teams.Memberships.TeamsGetMembershipForUserLegacyResult {
        val response = client.get("/teams/$teamId/memberships/$username")
        return when (response.status) {
            HttpStatusCode.OK -> Teams.Memberships.TeamsGetMembershipForUserLegacyResult.OK(response.body())
            HttpStatusCode.NotFound -> Teams.Memberships.TeamsGetMembershipForUserLegacyResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsAddOrUpdateMembershipForUserLegacy(teamId: Long, username: String, body: Teams.Memberships.TeamsAddOrUpdateMembershipForUserLegacyBody?): Teams.Memberships.TeamsAddOrUpdateMembershipForUserLegacyResult {
        val response = client.put("/teams/$teamId/memberships/$username") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Teams.Memberships.TeamsAddOrUpdateMembershipForUserLegacyResult.OK(response.body())
            HttpStatusCode.Forbidden -> Teams.Memberships.TeamsAddOrUpdateMembershipForUserLegacyResult.Forbidden
            HttpStatusCode.NotFound -> Teams.Memberships.TeamsAddOrUpdateMembershipForUserLegacyResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Teams.Memberships.TeamsAddOrUpdateMembershipForUserLegacyResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsRemoveMembershipForUserLegacy(teamId: Long, username: String): Teams.Memberships.TeamsRemoveMembershipForUserLegacyResult {
        val response = client.delete("/teams/$teamId/memberships/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Teams.Memberships.TeamsRemoveMembershipForUserLegacyResult.NoContent
            HttpStatusCode.Forbidden -> Teams.Memberships.TeamsRemoveMembershipForUserLegacyResult.Forbidden
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorTeamsRepos(private val client: HttpClient) : Teams.Repos {
    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsListReposLegacy(teamId: Long, page: Long, perPage: Long): Teams.Repos.TeamsListReposLegacyResult {
        val response = client.get("/teams/$teamId/repos") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Teams.Repos.TeamsListReposLegacyResult.OK(response.body())
            HttpStatusCode.NotFound -> Teams.Repos.TeamsListReposLegacyResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsCheckPermissionsForRepoLegacy(teamId: Long, owner: String, repo: String): Teams.Repos.TeamsCheckPermissionsForRepoLegacyResult {
        val response = client.get("/teams/$teamId/repos/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.OK -> Teams.Repos.TeamsCheckPermissionsForRepoLegacyResult.OK(response.body())
            HttpStatusCode.NoContent -> Teams.Repos.TeamsCheckPermissionsForRepoLegacyResult.NoContent
            HttpStatusCode.NotFound -> Teams.Repos.TeamsCheckPermissionsForRepoLegacyResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsAddOrUpdateRepoPermissionsLegacy(teamId: Long, owner: String, repo: String, body: Teams.Repos.TeamsAddOrUpdateRepoPermissionsLegacyBody?): Teams.Repos.TeamsAddOrUpdateRepoPermissionsLegacyResult {
        val response = client.put("/teams/$teamId/repos/$owner/$repo") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Teams.Repos.TeamsAddOrUpdateRepoPermissionsLegacyResult.NoContent
            HttpStatusCode.Forbidden -> Teams.Repos.TeamsAddOrUpdateRepoPermissionsLegacyResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Teams.Repos.TeamsAddOrUpdateRepoPermissionsLegacyResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsRemoveRepoLegacy(teamId: Long, owner: String, repo: String): Unit =
        client.delete("/teams/$teamId/repos/$owner/$repo").body()
}

internal class KtorTeamsTeamsApi(private val client: HttpClient) : Teams.TeamsApi {
    @Deprecated("Deprecated by the API provider")
    override suspend fun teamsListChildLegacy(teamId: Long, page: Long, perPage: Long): Teams.TeamsApi.TeamsListChildLegacyResult {
        val response = client.get("/teams/$teamId/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Teams.TeamsApi.TeamsListChildLegacyResult.OK(response.body())
            HttpStatusCode.Forbidden -> Teams.TeamsApi.TeamsListChildLegacyResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Teams.TeamsApi.TeamsListChildLegacyResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Teams.TeamsApi.TeamsListChildLegacyResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
