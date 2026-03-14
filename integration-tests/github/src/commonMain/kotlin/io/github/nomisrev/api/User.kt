package io.github.nomisrev.api

import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.PrivateUser
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.PublicUser
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.SimpleUser
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import io.github.nomisrev.model.Codespace
import kotlinx.serialization.builtins.serializer
import io.github.nomisrev.model.CodespacesSecret
import io.github.nomisrev.model.EmptyObject
import io.github.nomisrev.model.CodespacesUserPublicKey
import io.github.nomisrev.model.MinimalRepository
import io.github.nomisrev.model.CodespaceExportDetails
import io.github.nomisrev.model.CodespaceMachine
import io.github.nomisrev.model.CodespaceWithFullRepository
import io.github.nomisrev.model.Package
import io.github.nomisrev.model.Email
import kotlinx.serialization.builtins.ListSerializer
import io.github.nomisrev.model.GpgKey
import io.github.nomisrev.model.Installation
import io.github.nomisrev.model.NullableLicenseSimple
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Required
import io.github.nomisrev.model.InteractionLimitResponse
import io.github.nomisrev.model.InteractionLimit
import io.github.nomisrev.model.Issue
import io.github.nomisrev.model.Key
import io.github.nomisrev.model.UserMarketplacePurchase
import io.github.nomisrev.model.OrgMembership
import io.github.nomisrev.model.Migration
import io.github.nomisrev.model.OrganizationSimple
import io.github.nomisrev.model.PackageVersion
import io.github.nomisrev.model.Repository
import io.github.nomisrev.model.FullRepository
import io.github.nomisrev.model.RepositoryInvitation
import io.github.nomisrev.model.SocialAccount
import io.github.nomisrev.model.SshSigningKey
import io.github.nomisrev.model.TeamFull
import io.github.nomisrev.model.ProjectsV2ItemSimple
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
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.contentType

interface User {
    val blocks: User.Blocks

    val codespaces: User.Codespaces

    val docker: User.Docker

    val email: User.Email

    val emails: User.Emails

    val followers: User.Followers

    val following: User.Following

    val gpgKeys: User.GpgKeys

    val installations: User.Installations

    val interactionLimits: User.InteractionLimits

    val issues: User.Issues

    val keys: User.Keys

    val marketplacePurchases: User.MarketplacePurchases

    val memberships: User.Memberships

    val migrations: User.Migrations

    val orgs: User.Orgs

    val packages: User.Packages

    val publicEmails: User.PublicEmails

    val repos: User.Repos

    val repositoryInvitations: User.RepositoryInvitations

    val socialAccounts: User.SocialAccounts

    val sshSigningKeys: User.SshSigningKeys

    val starred: User.Starred

    val subscriptions: User.Subscriptions

    val teams: User.Teams

    val projectsV2: User.ProjectsV2

    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("user_view_type")
    @Serializable
    sealed interface UsersGetAuthenticatedResponse {
        @SerialName("private")
        @Serializable
        @JvmInline
        value class Private(val value: PrivateUser) : UsersGetAuthenticatedResponse

        @SerialName("public")
        @Serializable
        @JvmInline
        value class Public(val value: PublicUser) : UsersGetAuthenticatedResponse
    }


    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("user_view_type")
    @Serializable
    sealed interface UsersGetByIdResponse {
        @SerialName("private")
        @Serializable
        @JvmInline
        value class Private(val value: PrivateUser) : UsersGetByIdResponse

        @SerialName("public")
        @Serializable
        @JvmInline
        value class Public(val value: PublicUser) : UsersGetByIdResponse
    }


    @Serializable
    data class UsersUpdateAuthenticatedBody(
        val name: String? = null,
        val email: String? = null,
        val blog: String? = null,
        @SerialName("twitter_username") val twitterUsername: String? = null,
        val company: String? = null,
        val location: String? = null,
        val hireable: Boolean? = null,
        val bio: String? = null,
    )

    sealed interface UsersGetAuthenticatedResult {
        data class OK(val value: UsersGetAuthenticatedResponse) : UsersGetAuthenticatedResult

        data object NotModified : UsersGetAuthenticatedResult

        data class Unauthorized(val value: BasicError) : UsersGetAuthenticatedResult

        data class Forbidden(val value: BasicError) : UsersGetAuthenticatedResult
    }

    suspend fun usersGetAuthenticated(): UsersGetAuthenticatedResult

    sealed interface UsersUpdateAuthenticatedResult {
        data class OK(val value: PrivateUser) : UsersUpdateAuthenticatedResult

        data object NotModified : UsersUpdateAuthenticatedResult

        data class Unauthorized(val value: BasicError) : UsersUpdateAuthenticatedResult

        data class Forbidden(val value: BasicError) : UsersUpdateAuthenticatedResult

        data class NotFound(val value: BasicError) : UsersUpdateAuthenticatedResult

        data class UnprocessableEntity(val value: ValidationError) : UsersUpdateAuthenticatedResult
    }

    suspend fun usersUpdateAuthenticated(
        body: UsersUpdateAuthenticatedBody? = null,
    ): UsersUpdateAuthenticatedResult

    sealed interface UsersGetByIdResult {
        data class OK(val value: UsersGetByIdResponse) : UsersGetByIdResult

        data class NotFound(val value: BasicError) : UsersGetByIdResult
    }

    suspend fun usersGetById(
        accountId: Long,
    ): UsersGetByIdResult

    interface Blocks {
        sealed interface UsersListBlockedByAuthenticatedUserResult {
            data class OK(val value: List<SimpleUser>) : UsersListBlockedByAuthenticatedUserResult

            data object NotModified : UsersListBlockedByAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListBlockedByAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListBlockedByAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListBlockedByAuthenticatedUserResult
        }

        suspend fun usersListBlockedByAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListBlockedByAuthenticatedUserResult

        sealed interface UsersCheckBlockedResult {
            data object NoContent : UsersCheckBlockedResult

            data object NotModified : UsersCheckBlockedResult

            data class Unauthorized(val value: BasicError) : UsersCheckBlockedResult

            data class Forbidden(val value: BasicError) : UsersCheckBlockedResult

            data class NotFound(val value: BasicError) : UsersCheckBlockedResult
        }

        suspend fun usersCheckBlocked(
            username: String,
        ): UsersCheckBlockedResult

        sealed interface UsersBlockResult {
            data object NoContent : UsersBlockResult

            data object NotModified : UsersBlockResult

            data class Unauthorized(val value: BasicError) : UsersBlockResult

            data class Forbidden(val value: BasicError) : UsersBlockResult

            data class NotFound(val value: BasicError) : UsersBlockResult

            data class UnprocessableEntity(val value: ValidationError) : UsersBlockResult
        }

        suspend fun usersBlock(
            username: String,
        ): UsersBlockResult

        sealed interface UsersUnblockResult {
            data object NoContent : UsersUnblockResult

            data object NotModified : UsersUnblockResult

            data class Unauthorized(val value: BasicError) : UsersUnblockResult

            data class Forbidden(val value: BasicError) : UsersUnblockResult

            data class NotFound(val value: BasicError) : UsersUnblockResult
        }

        suspend fun usersUnblock(
            username: String,
        ): UsersUnblockResult
    }

    interface Codespaces {
        val secrets: User.Codespaces.Secrets

        val exports: User.Codespaces.Exports

        val machines: User.Codespaces.Machines

        val publish: User.Codespaces.Publish

        val start: User.Codespaces.Start

        val stop: User.Codespaces.Stop

        @Serializable(with = CodespacesCreateForAuthenticatedUserBody.Serializer::class)
        sealed interface CodespacesCreateForAuthenticatedUserBody {
            @Serializable
            data class One(
                @SerialName("repository_id") val repositoryId: Long,
                val ref: String? = null,
                val location: String? = null,
                val geo: Geo? = null,
                @SerialName("client_ip") val clientIp: String? = null,
                val machine: String? = null,
                @SerialName("devcontainer_path") val devcontainerPath: String? = null,
                @SerialName("multi_repo_permissions_opt_out") val multiRepoPermissionsOptOut: Boolean? = null,
                @SerialName("working_directory") val workingDirectory: String? = null,
                @SerialName("idle_timeout_minutes") val idleTimeoutMinutes: Long? = null,
                @SerialName("display_name") val displayName: String? = null,
                @SerialName("retention_period_minutes") val retentionPeriodMinutes: Long? = null,
            ) : CodespacesCreateForAuthenticatedUserBody {
                @Serializable
                enum class Geo {
                    EuropeWest, SoutheastAsia, UsEast, UsWest;
                }
            }

            @Serializable
            data class Two(
                @SerialName("pull_request") val pullRequest: PullRequest,
                val location: String? = null,
                val geo: Geo? = null,
                val machine: String? = null,
                @SerialName("devcontainer_path") val devcontainerPath: String? = null,
                @SerialName("working_directory") val workingDirectory: String? = null,
                @SerialName("idle_timeout_minutes") val idleTimeoutMinutes: Long? = null,
            ) : CodespacesCreateForAuthenticatedUserBody {
                @Serializable
                data class PullRequest(
                    @SerialName("pull_request_number") val pullRequestNumber: Long,
                    @SerialName("repository_id") val repositoryId: Long,
                )

                @Serializable
                enum class Geo {
                    EuropeWest, SoutheastAsia, UsEast, UsWest;
                }
            }

            object Serializer : KSerializer<CodespacesCreateForAuthenticatedUserBody> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.User.Codespaces.CodespacesCreateForAuthenticatedUserBody", PolymorphicKind.SEALED) {
                        element("One", CodespacesCreateForAuthenticatedUserBody.One.serializer().descriptor)
                        element("Two", CodespacesCreateForAuthenticatedUserBody.Two.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): CodespacesCreateForAuthenticatedUserBody {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        One::class to { decodeFromJsonElement(CodespacesCreateForAuthenticatedUserBody.One.serializer(), it) },
                        Two::class to { decodeFromJsonElement(CodespacesCreateForAuthenticatedUserBody.Two.serializer(), it) },
                    )
                }

                override fun serialize(encoder: Encoder, value: CodespacesCreateForAuthenticatedUserBody) = when(value) {
                    is One -> encoder.encodeSerializableValue(CodespacesCreateForAuthenticatedUserBody.One.serializer(), value)
                    is Two -> encoder.encodeSerializableValue(CodespacesCreateForAuthenticatedUserBody.Two.serializer(), value)
                }
            }
        }


        @Serializable
        data class CodespacesCreateForAuthenticatedUserResponse(
            val code: String? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        )


        @Serializable
        data class CodespacesListForAuthenticatedUserResponse(
            @SerialName("total_count") val totalCount: Long,
            val codespaces: List<Codespace>,
        )


        @Serializable
        data class CodespacesUpdateForAuthenticatedUserBody(
            val machine: String? = null,
            @SerialName("display_name") val displayName: String? = null,
            @SerialName("recent_folders") val recentFolders: List<String>? = null,
        )

        sealed interface CodespacesListForAuthenticatedUserResult {
            data class OK(val value: CodespacesListForAuthenticatedUserResponse) : CodespacesListForAuthenticatedUserResult

            data object NotModified : CodespacesListForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesListForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesListForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesListForAuthenticatedUserResult

            data class InternalServerError(val value: BasicError) : CodespacesListForAuthenticatedUserResult
        }

        suspend fun codespacesListForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
            repositoryId: Long? = null,
        ): CodespacesListForAuthenticatedUserResult

        sealed interface CodespacesCreateForAuthenticatedUserResult {
            data class Created(val value: Codespace) : CodespacesCreateForAuthenticatedUserResult

            data class Accepted(val value: Codespace) : CodespacesCreateForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesCreateForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesCreateForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesCreateForAuthenticatedUserResult

            data class ServiceUnavailable(val value: CodespacesCreateForAuthenticatedUserResponse) : CodespacesCreateForAuthenticatedUserResult
        }

        suspend fun codespacesCreateForAuthenticatedUser(
            body: CodespacesCreateForAuthenticatedUserBody,
        ): CodespacesCreateForAuthenticatedUserResult

        sealed interface CodespacesGetForAuthenticatedUserResult {
            data class OK(val value: Codespace) : CodespacesGetForAuthenticatedUserResult

            data object NotModified : CodespacesGetForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesGetForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesGetForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesGetForAuthenticatedUserResult

            data class InternalServerError(val value: BasicError) : CodespacesGetForAuthenticatedUserResult
        }

        suspend fun codespacesGetForAuthenticatedUser(
            codespaceName: String,
        ): CodespacesGetForAuthenticatedUserResult

        sealed interface CodespacesDeleteForAuthenticatedUserResult {
            data class Accepted(val value: JsonElement) : CodespacesDeleteForAuthenticatedUserResult

            data object NotModified : CodespacesDeleteForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesDeleteForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesDeleteForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesDeleteForAuthenticatedUserResult

            data class InternalServerError(val value: BasicError) : CodespacesDeleteForAuthenticatedUserResult
        }

        suspend fun codespacesDeleteForAuthenticatedUser(
            codespaceName: String,
        ): CodespacesDeleteForAuthenticatedUserResult

        sealed interface CodespacesUpdateForAuthenticatedUserResult {
            data class OK(val value: Codespace) : CodespacesUpdateForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : CodespacesUpdateForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : CodespacesUpdateForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : CodespacesUpdateForAuthenticatedUserResult
        }

        suspend fun codespacesUpdateForAuthenticatedUser(
            codespaceName: String,
            body: CodespacesUpdateForAuthenticatedUserBody? = null,
        ): CodespacesUpdateForAuthenticatedUserResult

        interface Secrets {
            val publicKey: User.Codespaces.Secrets.PublicKey

            val repositories: User.Codespaces.Secrets.Repositories

            @Serializable
            data class CodespacesCreateOrUpdateSecretForAuthenticatedUserBody(
                @SerialName("encrypted_value") val encryptedValue: String? = null,
                @SerialName("key_id") val keyId: String,
                @SerialName("selected_repository_ids") val selectedRepositoryIds: List<SelectedRepositoryIds>? = null,
            ) {
                @Serializable(with = SelectedRepositoryIds.Serializer::class)
                sealed interface SelectedRepositoryIds {
                    @Serializable
                    @JvmInline
                    value class CaseLong(val value: Long) : SelectedRepositoryIds

                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : SelectedRepositoryIds

                    object Serializer : KSerializer<SelectedRepositoryIds> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserBody.SelectedRepositoryIds", PolymorphicKind.SEALED) {
                                element("CaseLong", Long.serializer().descriptor)
                                element("CaseString", String.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): SelectedRepositoryIds {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: SelectedRepositoryIds) = when(value) {
                            is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        }
                    }
                }
            }


            @Serializable
            data class CodespacesListSecretsForAuthenticatedUserResponse(
                @SerialName("total_count") val totalCount: Long,
                val secrets: List<CodespacesSecret>,
            )

            suspend fun codespacesListSecretsForAuthenticatedUser(
                page: Long = 1L,
                perPage: Long = 30L,
            ): CodespacesListSecretsForAuthenticatedUserResponse

            suspend fun codespacesGetSecretForAuthenticatedUser(
                secretName: String,
            ): CodespacesSecret

            sealed interface CodespacesCreateOrUpdateSecretForAuthenticatedUserResult {
                data class Created(val value: EmptyObject) : CodespacesCreateOrUpdateSecretForAuthenticatedUserResult

                data object NoContent : CodespacesCreateOrUpdateSecretForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesCreateOrUpdateSecretForAuthenticatedUserResult

                data class UnprocessableEntity(val value: ValidationError) : CodespacesCreateOrUpdateSecretForAuthenticatedUserResult
            }

            suspend fun codespacesCreateOrUpdateSecretForAuthenticatedUser(
                secretName: String,
                body: CodespacesCreateOrUpdateSecretForAuthenticatedUserBody,
            ): CodespacesCreateOrUpdateSecretForAuthenticatedUserResult

            suspend fun codespacesDeleteSecretForAuthenticatedUser(
                secretName: String,
            ): Unit

            interface PublicKey {
                suspend fun codespacesGetPublicKeyForAuthenticatedUser(): CodespacesUserPublicKey
            }

            interface Repositories {
                @Serializable
                data class CodespacesListRepositoriesForSecretForAuthenticatedUserResponse(
                    @SerialName("total_count") val totalCount: Long,
                    val repositories: List<MinimalRepository>,
                )


                @Serializable
                @JvmInline
                value class CodespacesSetRepositoriesForSecretForAuthenticatedUserBody(@SerialName("selected_repository_ids") val selectedRepositoryIds: List<Long>)

                sealed interface CodespacesListRepositoriesForSecretForAuthenticatedUserResult {
                    data class OK(val value: CodespacesListRepositoriesForSecretForAuthenticatedUserResponse) : CodespacesListRepositoriesForSecretForAuthenticatedUserResult

                    data class Unauthorized(val value: BasicError) : CodespacesListRepositoriesForSecretForAuthenticatedUserResult

                    data class Forbidden(val value: BasicError) : CodespacesListRepositoriesForSecretForAuthenticatedUserResult

                    data class NotFound(val value: BasicError) : CodespacesListRepositoriesForSecretForAuthenticatedUserResult

                    data class InternalServerError(val value: BasicError) : CodespacesListRepositoriesForSecretForAuthenticatedUserResult
                }

                suspend fun codespacesListRepositoriesForSecretForAuthenticatedUser(
                    secretName: String,
                ): CodespacesListRepositoriesForSecretForAuthenticatedUserResult

                sealed interface CodespacesSetRepositoriesForSecretForAuthenticatedUserResult {
                    data object NoContent : CodespacesSetRepositoriesForSecretForAuthenticatedUserResult

                    data class Unauthorized(val value: BasicError) : CodespacesSetRepositoriesForSecretForAuthenticatedUserResult

                    data class Forbidden(val value: BasicError) : CodespacesSetRepositoriesForSecretForAuthenticatedUserResult

                    data class NotFound(val value: BasicError) : CodespacesSetRepositoriesForSecretForAuthenticatedUserResult

                    data class InternalServerError(val value: BasicError) : CodespacesSetRepositoriesForSecretForAuthenticatedUserResult
                }

                suspend fun codespacesSetRepositoriesForSecretForAuthenticatedUser(
                    secretName: String,
                    body: CodespacesSetRepositoriesForSecretForAuthenticatedUserBody,
                ): CodespacesSetRepositoriesForSecretForAuthenticatedUserResult

                sealed interface CodespacesAddRepositoryForSecretForAuthenticatedUserResult {
                    data object NoContent : CodespacesAddRepositoryForSecretForAuthenticatedUserResult

                    data class Unauthorized(val value: BasicError) : CodespacesAddRepositoryForSecretForAuthenticatedUserResult

                    data class Forbidden(val value: BasicError) : CodespacesAddRepositoryForSecretForAuthenticatedUserResult

                    data class NotFound(val value: BasicError) : CodespacesAddRepositoryForSecretForAuthenticatedUserResult

                    data class InternalServerError(val value: BasicError) : CodespacesAddRepositoryForSecretForAuthenticatedUserResult
                }

                suspend fun codespacesAddRepositoryForSecretForAuthenticatedUser(
                    secretName: String,
                    repositoryId: Long,
                ): CodespacesAddRepositoryForSecretForAuthenticatedUserResult

                sealed interface CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult {
                    data object NoContent : CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult

                    data class Unauthorized(val value: BasicError) : CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult

                    data class Forbidden(val value: BasicError) : CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult

                    data class NotFound(val value: BasicError) : CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult

                    data class InternalServerError(val value: BasicError) : CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult
                }

                suspend fun codespacesRemoveRepositoryForSecretForAuthenticatedUser(
                    secretName: String,
                    repositoryId: Long,
                ): CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult
            }
        }

        interface Exports {
            sealed interface CodespacesExportForAuthenticatedUserResult {
                data class Accepted(val value: CodespaceExportDetails) : CodespacesExportForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesExportForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesExportForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesExportForAuthenticatedUserResult

                data class UnprocessableEntity(val value: ValidationError) : CodespacesExportForAuthenticatedUserResult

                data class InternalServerError(val value: BasicError) : CodespacesExportForAuthenticatedUserResult
            }

            suspend fun codespacesExportForAuthenticatedUser(
                codespaceName: String,
            ): CodespacesExportForAuthenticatedUserResult

            sealed interface CodespacesGetExportDetailsForAuthenticatedUserResult {
                data class OK(val value: CodespaceExportDetails) : CodespacesGetExportDetailsForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesGetExportDetailsForAuthenticatedUserResult
            }

            suspend fun codespacesGetExportDetailsForAuthenticatedUser(
                codespaceName: String,
                exportId: String,
            ): CodespacesGetExportDetailsForAuthenticatedUserResult
        }

        interface Machines {
            @Serializable
            data class CodespacesCodespaceMachinesForAuthenticatedUserResponse(
                @SerialName("total_count") val totalCount: Long,
                val machines: List<CodespaceMachine>,
            )

            sealed interface CodespacesCodespaceMachinesForAuthenticatedUserResult {
                data class OK(val value: CodespacesCodespaceMachinesForAuthenticatedUserResponse) : CodespacesCodespaceMachinesForAuthenticatedUserResult

                data object NotModified : CodespacesCodespaceMachinesForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesCodespaceMachinesForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesCodespaceMachinesForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesCodespaceMachinesForAuthenticatedUserResult

                data class InternalServerError(val value: BasicError) : CodespacesCodespaceMachinesForAuthenticatedUserResult
            }

            suspend fun codespacesCodespaceMachinesForAuthenticatedUser(
                codespaceName: String,
            ): CodespacesCodespaceMachinesForAuthenticatedUserResult
        }

        interface Publish {
            @Serializable
            data class CodespacesPublishForAuthenticatedUserBody(val name: String? = null, val private: Boolean? = null)

            sealed interface CodespacesPublishForAuthenticatedUserResult {
                data class Created(val value: CodespaceWithFullRepository) : CodespacesPublishForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesPublishForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesPublishForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesPublishForAuthenticatedUserResult

                data class UnprocessableEntity(val value: ValidationError) : CodespacesPublishForAuthenticatedUserResult
            }

            suspend fun codespacesPublishForAuthenticatedUser(
                codespaceName: String,
                body: CodespacesPublishForAuthenticatedUserBody,
            ): CodespacesPublishForAuthenticatedUserResult
        }

        interface Start {
            sealed interface CodespacesStartForAuthenticatedUserResult {
                data class OK(val value: Codespace) : CodespacesStartForAuthenticatedUserResult

                data object NotModified : CodespacesStartForAuthenticatedUserResult

                data class BadRequest(val value: BasicError) : CodespacesStartForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesStartForAuthenticatedUserResult

                data class PaymentRequired(val value: BasicError) : CodespacesStartForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesStartForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesStartForAuthenticatedUserResult

                data class Conflict(val value: BasicError) : CodespacesStartForAuthenticatedUserResult

                data class InternalServerError(val value: BasicError) : CodespacesStartForAuthenticatedUserResult
            }

            suspend fun codespacesStartForAuthenticatedUser(
                codespaceName: String,
            ): CodespacesStartForAuthenticatedUserResult
        }

        interface Stop {
            sealed interface CodespacesStopForAuthenticatedUserResult {
                data class OK(val value: Codespace) : CodespacesStopForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : CodespacesStopForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : CodespacesStopForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : CodespacesStopForAuthenticatedUserResult

                data class InternalServerError(val value: BasicError) : CodespacesStopForAuthenticatedUserResult
            }

            suspend fun codespacesStopForAuthenticatedUser(
                codespaceName: String,
            ): CodespacesStopForAuthenticatedUserResult
        }
    }

    interface Docker {
        val conflicts: User.Docker.Conflicts

        interface Conflicts {
            suspend fun packagesListDockerMigrationConflictingPackagesForAuthenticatedUser(): List<Package>
        }
    }

    interface Email {
        val visibility: User.Email.Visibility

        interface Visibility {
            @Serializable
            @JvmInline
            value class UsersSetPrimaryEmailVisibilityForAuthenticatedUserBody(val visibility: Visibility) {
                @Serializable
                enum class Visibility {
                    @SerialName("public") Public, @SerialName("private") Private;
                }
            }

            sealed interface UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult {
                data class OK(val value: List<Email>) : UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult

                data object NotModified : UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult

                data class UnprocessableEntity(val value: ValidationError) : UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult
            }

            suspend fun usersSetPrimaryEmailVisibilityForAuthenticatedUser(
                body: UsersSetPrimaryEmailVisibilityForAuthenticatedUserBody,
            ): UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult
        }
    }

    interface Emails {
        @Serializable(with = UsersAddEmailForAuthenticatedUserBody.Serializer::class)
        sealed interface UsersAddEmailForAuthenticatedUserBody {
            @Serializable
            @JvmInline
            value class Emails(val emails: List<String>) : UsersAddEmailForAuthenticatedUserBody

            @Serializable
            @JvmInline
            value class CaseStrings(val value: List<String>) : UsersAddEmailForAuthenticatedUserBody

            @Serializable
            @JvmInline
            value class CaseString(val value: String) : UsersAddEmailForAuthenticatedUserBody

            object Serializer : KSerializer<UsersAddEmailForAuthenticatedUserBody> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.User.Emails.UsersAddEmailForAuthenticatedUserBody", PolymorphicKind.SEALED) {
                        element("Emails", UsersAddEmailForAuthenticatedUserBody.Emails.serializer().descriptor)
                        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                        element("CaseString", String.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): UsersAddEmailForAuthenticatedUserBody {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        Emails::class to { decodeFromJsonElement(UsersAddEmailForAuthenticatedUserBody.Emails.serializer(), it) },
                        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: UsersAddEmailForAuthenticatedUserBody) = when(value) {
                    is Emails -> encoder.encodeSerializableValue(UsersAddEmailForAuthenticatedUserBody.Emails.serializer(), value)
                    is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                }
            }
        }


        @Serializable(with = UsersDeleteEmailForAuthenticatedUserBody.Serializer::class)
        sealed interface UsersDeleteEmailForAuthenticatedUserBody {
            @Serializable
            @JvmInline
            value class Emails(val emails: List<String>) : UsersDeleteEmailForAuthenticatedUserBody

            @Serializable
            @JvmInline
            value class CaseStrings(val value: List<String>) : UsersDeleteEmailForAuthenticatedUserBody

            @Serializable
            @JvmInline
            value class CaseString(val value: String) : UsersDeleteEmailForAuthenticatedUserBody

            object Serializer : KSerializer<UsersDeleteEmailForAuthenticatedUserBody> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.User.Emails.UsersDeleteEmailForAuthenticatedUserBody", PolymorphicKind.SEALED) {
                        element("Emails", UsersDeleteEmailForAuthenticatedUserBody.Emails.serializer().descriptor)
                        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                        element("CaseString", String.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): UsersDeleteEmailForAuthenticatedUserBody {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        Emails::class to { decodeFromJsonElement(UsersDeleteEmailForAuthenticatedUserBody.Emails.serializer(), it) },
                        CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: UsersDeleteEmailForAuthenticatedUserBody) = when(value) {
                    is Emails -> encoder.encodeSerializableValue(UsersDeleteEmailForAuthenticatedUserBody.Emails.serializer(), value)
                    is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                }
            }
        }

        sealed interface UsersListEmailsForAuthenticatedUserResult {
            data class OK(val value: List<Email>) : UsersListEmailsForAuthenticatedUserResult

            data object NotModified : UsersListEmailsForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListEmailsForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListEmailsForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListEmailsForAuthenticatedUserResult
        }

        suspend fun usersListEmailsForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListEmailsForAuthenticatedUserResult

        sealed interface UsersAddEmailForAuthenticatedUserResult {
            data class Created(val value: List<Email>) : UsersAddEmailForAuthenticatedUserResult

            data object NotModified : UsersAddEmailForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersAddEmailForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersAddEmailForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersAddEmailForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersAddEmailForAuthenticatedUserResult
        }

        suspend fun usersAddEmailForAuthenticatedUser(
            body: UsersAddEmailForAuthenticatedUserBody? = null,
        ): UsersAddEmailForAuthenticatedUserResult

        sealed interface UsersDeleteEmailForAuthenticatedUserResult {
            data object NoContent : UsersDeleteEmailForAuthenticatedUserResult

            data object NotModified : UsersDeleteEmailForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersDeleteEmailForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersDeleteEmailForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersDeleteEmailForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersDeleteEmailForAuthenticatedUserResult
        }

        suspend fun usersDeleteEmailForAuthenticatedUser(
            body: UsersDeleteEmailForAuthenticatedUserBody? = null,
        ): UsersDeleteEmailForAuthenticatedUserResult
    }

    interface Followers {
        sealed interface UsersListFollowersForAuthenticatedUserResult {
            data class OK(val value: List<SimpleUser>) : UsersListFollowersForAuthenticatedUserResult

            data object NotModified : UsersListFollowersForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListFollowersForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListFollowersForAuthenticatedUserResult
        }

        suspend fun usersListFollowersForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListFollowersForAuthenticatedUserResult
    }

    interface Following {
        sealed interface UsersListFollowedByAuthenticatedUserResult {
            data class OK(val value: List<SimpleUser>) : UsersListFollowedByAuthenticatedUserResult

            data object NotModified : UsersListFollowedByAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListFollowedByAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListFollowedByAuthenticatedUserResult
        }

        suspend fun usersListFollowedByAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListFollowedByAuthenticatedUserResult

        sealed interface UsersCheckPersonIsFollowedByAuthenticatedResult {
            data object NoContent : UsersCheckPersonIsFollowedByAuthenticatedResult

            data object NotModified : UsersCheckPersonIsFollowedByAuthenticatedResult

            data class Unauthorized(val value: BasicError) : UsersCheckPersonIsFollowedByAuthenticatedResult

            data class Forbidden(val value: BasicError) : UsersCheckPersonIsFollowedByAuthenticatedResult

            data class NotFound(val value: BasicError) : UsersCheckPersonIsFollowedByAuthenticatedResult
        }

        suspend fun usersCheckPersonIsFollowedByAuthenticated(
            username: String,
        ): UsersCheckPersonIsFollowedByAuthenticatedResult

        sealed interface UsersFollowResult {
            data object NoContent : UsersFollowResult

            data object NotModified : UsersFollowResult

            data class Unauthorized(val value: BasicError) : UsersFollowResult

            data class Forbidden(val value: BasicError) : UsersFollowResult

            data class NotFound(val value: BasicError) : UsersFollowResult

            data class UnprocessableEntity(val value: ValidationError) : UsersFollowResult
        }

        suspend fun usersFollow(
            username: String,
        ): UsersFollowResult

        sealed interface UsersUnfollowResult {
            data object NoContent : UsersUnfollowResult

            data object NotModified : UsersUnfollowResult

            data class Unauthorized(val value: BasicError) : UsersUnfollowResult

            data class Forbidden(val value: BasicError) : UsersUnfollowResult

            data class NotFound(val value: BasicError) : UsersUnfollowResult
        }

        suspend fun usersUnfollow(
            username: String,
        ): UsersUnfollowResult
    }

    interface GpgKeys {
        @Serializable
        data class UsersCreateGpgKeyForAuthenticatedUserBody(
            val name: String? = null,
            @SerialName("armored_public_key") val armoredPublicKey: String,
        )

        sealed interface UsersListGpgKeysForAuthenticatedUserResult {
            data class OK(val value: List<GpgKey>) : UsersListGpgKeysForAuthenticatedUserResult

            data object NotModified : UsersListGpgKeysForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListGpgKeysForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListGpgKeysForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListGpgKeysForAuthenticatedUserResult
        }

        suspend fun usersListGpgKeysForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListGpgKeysForAuthenticatedUserResult

        sealed interface UsersCreateGpgKeyForAuthenticatedUserResult {
            data class Created(val value: GpgKey) : UsersCreateGpgKeyForAuthenticatedUserResult

            data object NotModified : UsersCreateGpgKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersCreateGpgKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersCreateGpgKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersCreateGpgKeyForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersCreateGpgKeyForAuthenticatedUserResult
        }

        suspend fun usersCreateGpgKeyForAuthenticatedUser(
            body: UsersCreateGpgKeyForAuthenticatedUserBody,
        ): UsersCreateGpgKeyForAuthenticatedUserResult

        sealed interface UsersGetGpgKeyForAuthenticatedUserResult {
            data class OK(val value: GpgKey) : UsersGetGpgKeyForAuthenticatedUserResult

            data object NotModified : UsersGetGpgKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersGetGpgKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersGetGpgKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersGetGpgKeyForAuthenticatedUserResult
        }

        suspend fun usersGetGpgKeyForAuthenticatedUser(
            gpgKeyId: Long,
        ): UsersGetGpgKeyForAuthenticatedUserResult

        sealed interface UsersDeleteGpgKeyForAuthenticatedUserResult {
            data object NoContent : UsersDeleteGpgKeyForAuthenticatedUserResult

            data object NotModified : UsersDeleteGpgKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersDeleteGpgKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersDeleteGpgKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersDeleteGpgKeyForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersDeleteGpgKeyForAuthenticatedUserResult
        }

        suspend fun usersDeleteGpgKeyForAuthenticatedUser(
            gpgKeyId: Long,
        ): UsersDeleteGpgKeyForAuthenticatedUserResult
    }

    interface Installations {
        val repositories: User.Installations.Repositories

        @Serializable
        data class AppsListInstallationsForAuthenticatedUserResponse(
            @SerialName("total_count") val totalCount: Long,
            val installations: List<Installation>,
        )

        sealed interface AppsListInstallationsForAuthenticatedUserResult {
            data class OK(val value: AppsListInstallationsForAuthenticatedUserResponse) : AppsListInstallationsForAuthenticatedUserResult

            data object NotModified : AppsListInstallationsForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : AppsListInstallationsForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : AppsListInstallationsForAuthenticatedUserResult
        }

        suspend fun appsListInstallationsForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): AppsListInstallationsForAuthenticatedUserResult

        interface Repositories {
            @Serializable
            data class AppsListInstallationReposForAuthenticatedUserResponse(
                @SerialName("total_count") val totalCount: Long,
                @SerialName("repository_selection") val repositorySelection: String? = null,
                val repositories: List<Repositories>,
            ) {
                @Serializable
                data class Repositories(
                    val id: Long,
                    @SerialName("node_id") val nodeId: String,
                    val name: String,
                    @SerialName("full_name") val fullName: String,
                    val license: NullableLicenseSimple?,
                    val forks: Long,
                    val permissions: Permissions? = null,
                    val owner: SimpleUser,
                    @Required val private: Boolean,
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
                    @SerialName("has_issues") @Required val hasIssues: Boolean,
                    @SerialName("has_projects") @Required val hasProjects: Boolean,
                    @SerialName("has_wiki") @Required val hasWiki: Boolean,
                    @SerialName("has_pages") val hasPages: Boolean,
                    @SerialName("has_downloads") @Required val hasDownloads: Boolean,
                    @SerialName("has_discussions") val hasDiscussions: Boolean? = null,
                    @SerialName("has_pull_requests") val hasPullRequests: Boolean? = null,
                    @SerialName("pull_request_creation_policy") val pullRequestCreationPolicy: PullRequestCreationPolicy? = null,
                    @SerialName("has_commit_comments") val hasCommitComments: Boolean? = null,
                    @Required val archived: Boolean,
                    val disabled: Boolean,
                    val visibility: String? = null,
                    @SerialName("pushed_at") val pushedAt: LocalDateTime?,
                    @SerialName("created_at") val createdAt: LocalDateTime?,
                    @SerialName("updated_at") val updatedAt: LocalDateTime?,
                    @SerialName("allow_rebase_merge") val allowRebaseMerge: Boolean? = null,
                    @SerialName("temp_clone_token") val tempCloneToken: String? = null,
                    @SerialName("allow_squash_merge") val allowSquashMerge: Boolean? = null,
                    @SerialName("allow_auto_merge") val allowAutoMerge: Boolean? = null,
                    @SerialName("delete_branch_on_merge") val deleteBranchOnMerge: Boolean? = null,
                    @SerialName("allow_update_branch") val allowUpdateBranch: Boolean? = null,
                    @SerialName("use_squash_pr_title_as_default") val useSquashPrTitleAsDefault: Boolean? = null,
                    @SerialName("squash_merge_commit_title") val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
                    @SerialName("squash_merge_commit_message") val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
                    @SerialName("merge_commit_title") val mergeCommitTitle: MergeCommitTitle? = null,
                    @SerialName("merge_commit_message") val mergeCommitMessage: MergeCommitMessage? = null,
                    @SerialName("allow_merge_commit") val allowMergeCommit: Boolean? = null,
                    @SerialName("allow_forking") val allowForking: Boolean? = null,
                    @SerialName("web_commit_signoff_required") val webCommitSignoffRequired: Boolean? = null,
                    @SerialName("open_issues") val openIssues: Long,
                    val watchers: Long,
                    @SerialName("master_branch") val masterBranch: String? = null,
                    @SerialName("starred_at") val starredAt: String? = null,
                    @SerialName("anonymous_access_enabled") val anonymousAccessEnabled: Boolean? = null,
                    @SerialName("code_search_index_status") val codeSearchIndexStatus: CodeSearchIndexStatus? = null,
                ) {
                    @Serializable
                    data class Permissions(
                        val admin: Boolean,
                        val pull: Boolean,
                        val triage: Boolean? = null,
                        val push: Boolean,
                        val maintain: Boolean? = null,
                    )

                    @Serializable
                    enum class PullRequestCreationPolicy {
                        @SerialName("all") All, @SerialName("collaborators_only") CollaboratorsOnly;
                    }

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

                    @Serializable
                    data class CodeSearchIndexStatus(
                        @SerialName("lexical_search_ok") val lexicalSearchOk: Boolean? = null,
                        @SerialName("lexical_commit_sha") val lexicalCommitSha: String? = null,
                    )
                }
            }

            sealed interface AppsListInstallationReposForAuthenticatedUserResult {
                data class OK(val value: AppsListInstallationReposForAuthenticatedUserResponse) : AppsListInstallationReposForAuthenticatedUserResult

                data object NotModified : AppsListInstallationReposForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : AppsListInstallationReposForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : AppsListInstallationReposForAuthenticatedUserResult
            }

            suspend fun appsListInstallationReposForAuthenticatedUser(
                installationId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): AppsListInstallationReposForAuthenticatedUserResult

            sealed interface AppsAddRepoToInstallationForAuthenticatedUserResult {
                data object NoContent : AppsAddRepoToInstallationForAuthenticatedUserResult

                data object NotModified : AppsAddRepoToInstallationForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : AppsAddRepoToInstallationForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : AppsAddRepoToInstallationForAuthenticatedUserResult
            }

            suspend fun appsAddRepoToInstallationForAuthenticatedUser(
                installationId: Long,
                repositoryId: Long,
            ): AppsAddRepoToInstallationForAuthenticatedUserResult

            sealed interface AppsRemoveRepoFromInstallationForAuthenticatedUserResult {
                data object NoContent : AppsRemoveRepoFromInstallationForAuthenticatedUserResult

                data object NotModified : AppsRemoveRepoFromInstallationForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : AppsRemoveRepoFromInstallationForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : AppsRemoveRepoFromInstallationForAuthenticatedUserResult

                data object UnprocessableEntity : AppsRemoveRepoFromInstallationForAuthenticatedUserResult
            }

            suspend fun appsRemoveRepoFromInstallationForAuthenticatedUser(
                installationId: Long,
                repositoryId: Long,
            ): AppsRemoveRepoFromInstallationForAuthenticatedUserResult
        }
    }

    interface InteractionLimits {
        @Serializable(with = InteractionsGetRestrictionsForAuthenticatedUserResponse.Serializer::class)
        sealed interface InteractionsGetRestrictionsForAuthenticatedUserResponse {
            @Serializable
            @JvmInline
            value class CaseInteractionLimitResponse(val value: InteractionLimitResponse) : InteractionsGetRestrictionsForAuthenticatedUserResponse

            @Serializable
            data object Empty : InteractionsGetRestrictionsForAuthenticatedUserResponse

            object Serializer : KSerializer<InteractionsGetRestrictionsForAuthenticatedUserResponse> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.User.InteractionLimits.InteractionsGetRestrictionsForAuthenticatedUserResponse", PolymorphicKind.SEALED) {
                        element("CaseInteractionLimitResponse", InteractionLimitResponse.serializer().descriptor)
                        element("Empty", Unit.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): InteractionsGetRestrictionsForAuthenticatedUserResponse {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseInteractionLimitResponse::class to { CaseInteractionLimitResponse(decodeFromJsonElement(InteractionLimitResponse.serializer(), it)) },
                        Empty::class to { decodeFromJsonElement(Empty.serializer(), it) },
                    )
                }

                override fun serialize(encoder: Encoder, value: InteractionsGetRestrictionsForAuthenticatedUserResponse) = when(value) {
                    is CaseInteractionLimitResponse -> encoder.encodeSerializableValue(InteractionLimitResponse.serializer(), value.value)
                    is Empty -> encoder.encodeSerializableValue(Empty.serializer(), value)
                }
            }
        }

        sealed interface InteractionsGetRestrictionsForAuthenticatedUserResult {
            data class OK(val value: InteractionsGetRestrictionsForAuthenticatedUserResponse) : InteractionsGetRestrictionsForAuthenticatedUserResult

            data object NoContent : InteractionsGetRestrictionsForAuthenticatedUserResult
        }

        suspend fun interactionsGetRestrictionsForAuthenticatedUser(): InteractionsGetRestrictionsForAuthenticatedUserResult

        sealed interface InteractionsSetRestrictionsForAuthenticatedUserResult {
            data class OK(val value: InteractionLimitResponse) : InteractionsSetRestrictionsForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : InteractionsSetRestrictionsForAuthenticatedUserResult
        }

        suspend fun interactionsSetRestrictionsForAuthenticatedUser(
            body: InteractionLimit,
        ): InteractionsSetRestrictionsForAuthenticatedUserResult

        suspend fun interactionsRemoveRestrictionsForAuthenticatedUser(): Unit
    }

    interface Issues {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class Filter {
            @SerialName("assigned")
            Assigned,
            @SerialName("created")
            Created,
            @SerialName("mentioned")
            Mentioned,
            @SerialName("subscribed")
            Subscribed,
            @SerialName("repos")
            Repos,
            @SerialName("all")
            All;
        }


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("comments") Comments;
        }


        @Serializable
        enum class State {
            @SerialName("open") Open, @SerialName("closed") Closed, @SerialName("all") All;
        }

        sealed interface IssuesListForAuthenticatedUserResult {
            data class OK(val value: List<Issue>) : IssuesListForAuthenticatedUserResult

            data object NotModified : IssuesListForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : IssuesListForAuthenticatedUserResult
        }

        suspend fun issuesListForAuthenticatedUser(
            direction: Direction = Direction.Desc,
            filter: Filter = Filter.Assigned,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
            state: State = State.Open,
            labels: String? = null,
            since: LocalDateTime? = null,
        ): IssuesListForAuthenticatedUserResult
    }

    interface Keys {
        @Serializable
        data class UsersCreatePublicSshKeyForAuthenticatedUserBody(val title: String? = null, val key: String)

        sealed interface UsersListPublicSshKeysForAuthenticatedUserResult {
            data class OK(val value: List<Key>) : UsersListPublicSshKeysForAuthenticatedUserResult

            data object NotModified : UsersListPublicSshKeysForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListPublicSshKeysForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListPublicSshKeysForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListPublicSshKeysForAuthenticatedUserResult
        }

        suspend fun usersListPublicSshKeysForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListPublicSshKeysForAuthenticatedUserResult

        sealed interface UsersCreatePublicSshKeyForAuthenticatedUserResult {
            data class Created(val value: Key) : UsersCreatePublicSshKeyForAuthenticatedUserResult

            data object NotModified : UsersCreatePublicSshKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersCreatePublicSshKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersCreatePublicSshKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersCreatePublicSshKeyForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersCreatePublicSshKeyForAuthenticatedUserResult
        }

        suspend fun usersCreatePublicSshKeyForAuthenticatedUser(
            body: UsersCreatePublicSshKeyForAuthenticatedUserBody,
        ): UsersCreatePublicSshKeyForAuthenticatedUserResult

        sealed interface UsersGetPublicSshKeyForAuthenticatedUserResult {
            data class OK(val value: Key) : UsersGetPublicSshKeyForAuthenticatedUserResult

            data object NotModified : UsersGetPublicSshKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersGetPublicSshKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersGetPublicSshKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersGetPublicSshKeyForAuthenticatedUserResult
        }

        suspend fun usersGetPublicSshKeyForAuthenticatedUser(
            keyId: Long,
        ): UsersGetPublicSshKeyForAuthenticatedUserResult

        sealed interface UsersDeletePublicSshKeyForAuthenticatedUserResult {
            data object NoContent : UsersDeletePublicSshKeyForAuthenticatedUserResult

            data object NotModified : UsersDeletePublicSshKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersDeletePublicSshKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersDeletePublicSshKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersDeletePublicSshKeyForAuthenticatedUserResult
        }

        suspend fun usersDeletePublicSshKeyForAuthenticatedUser(
            keyId: Long,
        ): UsersDeletePublicSshKeyForAuthenticatedUserResult
    }

    interface MarketplacePurchases {
        val stubbed: User.MarketplacePurchases.Stubbed

        sealed interface AppsListSubscriptionsForAuthenticatedUserResult {
            data class OK(val value: List<UserMarketplacePurchase>) : AppsListSubscriptionsForAuthenticatedUserResult

            data object NotModified : AppsListSubscriptionsForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : AppsListSubscriptionsForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : AppsListSubscriptionsForAuthenticatedUserResult
        }

        suspend fun appsListSubscriptionsForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): AppsListSubscriptionsForAuthenticatedUserResult

        interface Stubbed {
            sealed interface AppsListSubscriptionsForAuthenticatedUserStubbedResult {
                data class OK(val value: List<UserMarketplacePurchase>) : AppsListSubscriptionsForAuthenticatedUserStubbedResult

                data object NotModified : AppsListSubscriptionsForAuthenticatedUserStubbedResult

                data class Unauthorized(val value: BasicError) : AppsListSubscriptionsForAuthenticatedUserStubbedResult
            }

            suspend fun appsListSubscriptionsForAuthenticatedUserStubbed(
                page: Long = 1L,
                perPage: Long = 30L,
            ): AppsListSubscriptionsForAuthenticatedUserStubbedResult
        }
    }

    interface Memberships {
        val orgs: User.Memberships.OrgsApi

        interface OrgsApi {
            @Serializable
            @JvmInline
            value class OrgsUpdateMembershipForAuthenticatedUserBody(val state: State) {
                @Serializable
                enum class State {
                    @SerialName("active") Active;
                }
            }


            @Serializable
            enum class State {
                @SerialName("active") Active, @SerialName("pending") Pending;
            }

            sealed interface OrgsListMembershipsForAuthenticatedUserResult {
                data class OK(val value: List<OrgMembership>) : OrgsListMembershipsForAuthenticatedUserResult

                data object NotModified : OrgsListMembershipsForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : OrgsListMembershipsForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : OrgsListMembershipsForAuthenticatedUserResult

                data class UnprocessableEntity(val value: ValidationError) : OrgsListMembershipsForAuthenticatedUserResult
            }

            suspend fun orgsListMembershipsForAuthenticatedUser(
                page: Long = 1L,
                perPage: Long = 30L,
                state: State? = null,
            ): OrgsListMembershipsForAuthenticatedUserResult

            sealed interface OrgsGetMembershipForAuthenticatedUserResult {
                data class OK(val value: OrgMembership) : OrgsGetMembershipForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : OrgsGetMembershipForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : OrgsGetMembershipForAuthenticatedUserResult
            }

            suspend fun orgsGetMembershipForAuthenticatedUser(
                org: String,
            ): OrgsGetMembershipForAuthenticatedUserResult

            sealed interface OrgsUpdateMembershipForAuthenticatedUserResult {
                data class OK(val value: OrgMembership) : OrgsUpdateMembershipForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : OrgsUpdateMembershipForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : OrgsUpdateMembershipForAuthenticatedUserResult

                data class UnprocessableEntity(val value: ValidationError) : OrgsUpdateMembershipForAuthenticatedUserResult
            }

            suspend fun orgsUpdateMembershipForAuthenticatedUser(
                org: String,
                body: OrgsUpdateMembershipForAuthenticatedUserBody,
            ): OrgsUpdateMembershipForAuthenticatedUserResult
        }
    }

    interface Migrations {
        val archive: User.Migrations.Archive

        val repos: User.Migrations.ReposApi

        val repositories: User.Migrations.Repositories

        @Serializable
        data class MigrationsStartForAuthenticatedUserBody(
            @SerialName("lock_repositories") val lockRepositories: Boolean? = null,
            @SerialName("exclude_metadata") val excludeMetadata: Boolean? = null,
            @SerialName("exclude_git_data") val excludeGitData: Boolean? = null,
            @SerialName("exclude_attachments") val excludeAttachments: Boolean? = null,
            @SerialName("exclude_releases") val excludeReleases: Boolean? = null,
            @SerialName("exclude_owner_projects") val excludeOwnerProjects: Boolean? = null,
            @SerialName("org_metadata_only") val orgMetadataOnly: Boolean? = null,
            val exclude: List<Exclude>? = null,
            val repositories: List<String>,
        ) {
            @Serializable
            enum class Exclude {
                @SerialName("repositories") Repositories;
            }
        }

        sealed interface MigrationsListForAuthenticatedUserResult {
            data class OK(val value: List<Migration>) : MigrationsListForAuthenticatedUserResult

            data object NotModified : MigrationsListForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : MigrationsListForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : MigrationsListForAuthenticatedUserResult
        }

        suspend fun migrationsListForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): MigrationsListForAuthenticatedUserResult

        sealed interface MigrationsStartForAuthenticatedUserResult {
            data class Created(val value: Migration) : MigrationsStartForAuthenticatedUserResult

            data object NotModified : MigrationsStartForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : MigrationsStartForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : MigrationsStartForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : MigrationsStartForAuthenticatedUserResult
        }

        suspend fun migrationsStartForAuthenticatedUser(
            body: MigrationsStartForAuthenticatedUserBody,
        ): MigrationsStartForAuthenticatedUserResult

        sealed interface MigrationsGetStatusForAuthenticatedUserResult {
            data class OK(val value: Migration) : MigrationsGetStatusForAuthenticatedUserResult

            data object NotModified : MigrationsGetStatusForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : MigrationsGetStatusForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : MigrationsGetStatusForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : MigrationsGetStatusForAuthenticatedUserResult
        }

        suspend fun migrationsGetStatusForAuthenticatedUser(
            migrationId: Long,
            exclude: List<String>? = null,
        ): MigrationsGetStatusForAuthenticatedUserResult

        interface Archive {
            sealed interface MigrationsGetArchiveForAuthenticatedUserResult {
                data object Found : MigrationsGetArchiveForAuthenticatedUserResult

                data object NotModified : MigrationsGetArchiveForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : MigrationsGetArchiveForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : MigrationsGetArchiveForAuthenticatedUserResult
            }

            suspend fun migrationsGetArchiveForAuthenticatedUser(
                migrationId: Long,
            ): MigrationsGetArchiveForAuthenticatedUserResult

            sealed interface MigrationsDeleteArchiveForAuthenticatedUserResult {
                data object NoContent : MigrationsDeleteArchiveForAuthenticatedUserResult

                data object NotModified : MigrationsDeleteArchiveForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : MigrationsDeleteArchiveForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : MigrationsDeleteArchiveForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : MigrationsDeleteArchiveForAuthenticatedUserResult
            }

            suspend fun migrationsDeleteArchiveForAuthenticatedUser(
                migrationId: Long,
            ): MigrationsDeleteArchiveForAuthenticatedUserResult
        }

        interface ReposApi {
            val lock: User.Migrations.ReposApi.Lock

            interface Lock {
                sealed interface MigrationsUnlockRepoForAuthenticatedUserResult {
                    data object NoContent : MigrationsUnlockRepoForAuthenticatedUserResult

                    data object NotModified : MigrationsUnlockRepoForAuthenticatedUserResult

                    data class Unauthorized(val value: BasicError) : MigrationsUnlockRepoForAuthenticatedUserResult

                    data class Forbidden(val value: BasicError) : MigrationsUnlockRepoForAuthenticatedUserResult

                    data class NotFound(val value: BasicError) : MigrationsUnlockRepoForAuthenticatedUserResult
                }

                suspend fun migrationsUnlockRepoForAuthenticatedUser(
                    migrationId: Long,
                    repoName: String,
                ): MigrationsUnlockRepoForAuthenticatedUserResult
            }
        }

        interface Repositories {
            sealed interface MigrationsListReposForAuthenticatedUserResult {
                data class OK(val value: List<MinimalRepository>) : MigrationsListReposForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : MigrationsListReposForAuthenticatedUserResult
            }

            suspend fun migrationsListReposForAuthenticatedUser(
                migrationId: Long,
                page: Long = 1L,
                perPage: Long = 30L,
            ): MigrationsListReposForAuthenticatedUserResult
        }
    }

    interface Orgs {
        sealed interface OrgsListForAuthenticatedUserResult {
            data class OK(val value: List<OrganizationSimple>) : OrgsListForAuthenticatedUserResult

            data object NotModified : OrgsListForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : OrgsListForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : OrgsListForAuthenticatedUserResult
        }

        suspend fun orgsListForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): OrgsListForAuthenticatedUserResult
    }

    interface Packages {
        val restore: User.Packages.Restore

        val versions: User.Packages.Versions

        @Serializable
        enum class PackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }


        @Serializable
        enum class PackagesGetPackageForAuthenticatedUserPackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }


        @Serializable
        enum class PackagesListPackagesForAuthenticatedUserPackageType {
            @SerialName("npm")
            Npm,
            @SerialName("maven")
            Maven,
            @SerialName("rubygems")
            Rubygems,
            @SerialName("docker")
            Docker,
            @SerialName("nuget")
            Nuget,
            @SerialName("container")
            Container;
        }


        @Serializable
        enum class Visibility {
            @SerialName("public") Public, @SerialName("private") Private, @SerialName("internal") Internal;
        }

        sealed interface PackagesListPackagesForAuthenticatedUserResult {
            data class OK(val value: List<Package>) : PackagesListPackagesForAuthenticatedUserResult

            data object BadRequest : PackagesListPackagesForAuthenticatedUserResult
        }

        suspend fun packagesListPackagesForAuthenticatedUser(
            packageType: PackagesListPackagesForAuthenticatedUserPackageType,
            page: Long = 1L,
            perPage: Long = 30L,
            visibility: Visibility? = null,
        ): PackagesListPackagesForAuthenticatedUserResult

        suspend fun packagesGetPackageForAuthenticatedUser(
            packageType: PackagesGetPackageForAuthenticatedUserPackageType,
            packageName: String,
        ): Package

        sealed interface PackagesDeletePackageForAuthenticatedUserResult {
            data object NoContent : PackagesDeletePackageForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : PackagesDeletePackageForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : PackagesDeletePackageForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : PackagesDeletePackageForAuthenticatedUserResult
        }

        suspend fun packagesDeletePackageForAuthenticatedUser(
            packageType: PackageType,
            packageName: String,
        ): PackagesDeletePackageForAuthenticatedUserResult

        interface Restore {
            @Serializable
            enum class PackagesRestorePackageForAuthenticatedUserPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }

            sealed interface PackagesRestorePackageForAuthenticatedUserResult {
                data object NoContent : PackagesRestorePackageForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : PackagesRestorePackageForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : PackagesRestorePackageForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : PackagesRestorePackageForAuthenticatedUserResult
            }

            suspend fun packagesRestorePackageForAuthenticatedUser(
                packageType: PackagesRestorePackageForAuthenticatedUserPackageType,
                packageName: String,
                token: String? = null,
            ): PackagesRestorePackageForAuthenticatedUserResult
        }

        interface Versions {
            val restore: User.Packages.Versions.RestoreApi

            @Serializable
            enum class PackagesDeletePackageVersionForAuthenticatedUserPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }


            @Serializable
            enum class PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }


            @Serializable
            enum class PackagesGetPackageVersionForAuthenticatedUserPackageType {
                @SerialName("npm")
                Npm,
                @SerialName("maven")
                Maven,
                @SerialName("rubygems")
                Rubygems,
                @SerialName("docker")
                Docker,
                @SerialName("nuget")
                Nuget,
                @SerialName("container")
                Container;
            }


            @Serializable
            enum class State {
                @SerialName("active") Active, @SerialName("deleted") Deleted;
            }

            sealed interface PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult {
                data class OK(val value: List<PackageVersion>) : PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult

                data class NotFound(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult
            }

            suspend fun packagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUser(
                packageType: PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserPackageType,
                packageName: String,
                page: Long = 1L,
                perPage: Long = 30L,
                state: State = State.Active,
            ): PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult

            suspend fun packagesGetPackageVersionForAuthenticatedUser(
                packageType: PackagesGetPackageVersionForAuthenticatedUserPackageType,
                packageName: String,
                packageVersionId: Long,
            ): PackageVersion

            sealed interface PackagesDeletePackageVersionForAuthenticatedUserResult {
                data object NoContent : PackagesDeletePackageVersionForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : PackagesDeletePackageVersionForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : PackagesDeletePackageVersionForAuthenticatedUserResult

                data class NotFound(val value: BasicError) : PackagesDeletePackageVersionForAuthenticatedUserResult
            }

            suspend fun packagesDeletePackageVersionForAuthenticatedUser(
                packageType: PackagesDeletePackageVersionForAuthenticatedUserPackageType,
                packageName: String,
                packageVersionId: Long,
            ): PackagesDeletePackageVersionForAuthenticatedUserResult

            interface RestoreApi {
                @Serializable
                enum class PackagesRestorePackageVersionForAuthenticatedUserPackageType {
                    @SerialName("npm")
                    Npm,
                    @SerialName("maven")
                    Maven,
                    @SerialName("rubygems")
                    Rubygems,
                    @SerialName("docker")
                    Docker,
                    @SerialName("nuget")
                    Nuget,
                    @SerialName("container")
                    Container;
                }

                sealed interface PackagesRestorePackageVersionForAuthenticatedUserResult {
                    data object NoContent : PackagesRestorePackageVersionForAuthenticatedUserResult

                    data class Unauthorized(val value: BasicError) : PackagesRestorePackageVersionForAuthenticatedUserResult

                    data class Forbidden(val value: BasicError) : PackagesRestorePackageVersionForAuthenticatedUserResult

                    data class NotFound(val value: BasicError) : PackagesRestorePackageVersionForAuthenticatedUserResult
                }

                suspend fun packagesRestorePackageVersionForAuthenticatedUser(
                    packageType: PackagesRestorePackageVersionForAuthenticatedUserPackageType,
                    packageName: String,
                    packageVersionId: Long,
                ): PackagesRestorePackageVersionForAuthenticatedUserResult
            }
        }
    }

    interface PublicEmails {
        sealed interface UsersListPublicEmailsForAuthenticatedUserResult {
            data class OK(val value: List<Email>) : UsersListPublicEmailsForAuthenticatedUserResult

            data object NotModified : UsersListPublicEmailsForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListPublicEmailsForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListPublicEmailsForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListPublicEmailsForAuthenticatedUserResult
        }

        suspend fun usersListPublicEmailsForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListPublicEmailsForAuthenticatedUserResult
    }

    interface Repos {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        data class ReposCreateForAuthenticatedUserBody(
            val name: String,
            val description: String? = null,
            val homepage: String? = null,
            val private: Boolean? = null,
            @SerialName("has_issues") val hasIssues: Boolean? = null,
            @SerialName("has_projects") val hasProjects: Boolean? = null,
            @SerialName("has_wiki") val hasWiki: Boolean? = null,
            @SerialName("has_discussions") val hasDiscussions: Boolean? = null,
            @SerialName("team_id") val teamId: Long? = null,
            @SerialName("auto_init") val autoInit: Boolean? = null,
            @SerialName("gitignore_template") val gitignoreTemplate: String? = null,
            @SerialName("license_template") val licenseTemplate: String? = null,
            @SerialName("allow_squash_merge") val allowSquashMerge: Boolean? = null,
            @SerialName("allow_merge_commit") val allowMergeCommit: Boolean? = null,
            @SerialName("allow_rebase_merge") val allowRebaseMerge: Boolean? = null,
            @SerialName("allow_auto_merge") val allowAutoMerge: Boolean? = null,
            @SerialName("delete_branch_on_merge") val deleteBranchOnMerge: Boolean? = null,
            @SerialName("squash_merge_commit_title") val squashMergeCommitTitle: SquashMergeCommitTitle? = null,
            @SerialName("squash_merge_commit_message") val squashMergeCommitMessage: SquashMergeCommitMessage? = null,
            @SerialName("merge_commit_title") val mergeCommitTitle: MergeCommitTitle? = null,
            @SerialName("merge_commit_message") val mergeCommitMessage: MergeCommitMessage? = null,
            @SerialName("has_downloads") val hasDownloads: Boolean? = null,
            @SerialName("is_template") val isTemplate: Boolean? = null,
        ) {
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


        @Serializable
        enum class Sort {
            @SerialName("created")
            Created,
            @SerialName("updated")
            Updated,
            @SerialName("pushed")
            Pushed,
            @SerialName("full_name")
            FullName;
        }


        @Serializable
        enum class Type {
            @SerialName("all")
            All,
            @SerialName("owner")
            Owner,
            @SerialName("public")
            Public,
            @SerialName("private")
            Private,
            @SerialName("member")
            Member;
        }


        @Serializable
        enum class Visibility {
            @SerialName("all") All, @SerialName("public") Public, @SerialName("private") Private;
        }

        sealed interface ReposListForAuthenticatedUserResult {
            data class OK(val value: List<Repository>) : ReposListForAuthenticatedUserResult

            data object NotModified : ReposListForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ReposListForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ReposListForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : ReposListForAuthenticatedUserResult
        }

        suspend fun reposListForAuthenticatedUser(
            affiliation: String = "owner,collaborator,organization_member",
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.FullName,
            type: Type = Type.All,
            visibility: Visibility = Visibility.All,
            before: LocalDateTime? = null,
            direction: Direction? = null,
            since: LocalDateTime? = null,
        ): ReposListForAuthenticatedUserResult

        sealed interface ReposCreateForAuthenticatedUserResult {
            data class Created(val value: FullRepository) : ReposCreateForAuthenticatedUserResult

            data object NotModified : ReposCreateForAuthenticatedUserResult

            data class BadRequest(val value: BasicError) : ReposCreateForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ReposCreateForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ReposCreateForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ReposCreateForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : ReposCreateForAuthenticatedUserResult
        }

        suspend fun reposCreateForAuthenticatedUser(
            body: ReposCreateForAuthenticatedUserBody,
        ): ReposCreateForAuthenticatedUserResult
    }

    interface RepositoryInvitations {
        sealed interface ReposListInvitationsForAuthenticatedUserResult {
            data class OK(val value: List<RepositoryInvitation>) : ReposListInvitationsForAuthenticatedUserResult

            data object NotModified : ReposListInvitationsForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ReposListInvitationsForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ReposListInvitationsForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ReposListInvitationsForAuthenticatedUserResult
        }

        suspend fun reposListInvitationsForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): ReposListInvitationsForAuthenticatedUserResult

        sealed interface ReposDeclineInvitationForAuthenticatedUserResult {
            data object NoContent : ReposDeclineInvitationForAuthenticatedUserResult

            data object NotModified : ReposDeclineInvitationForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ReposDeclineInvitationForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ReposDeclineInvitationForAuthenticatedUserResult

            data class Conflict(val value: BasicError) : ReposDeclineInvitationForAuthenticatedUserResult
        }

        suspend fun reposDeclineInvitationForAuthenticatedUser(
            invitationId: Long,
        ): ReposDeclineInvitationForAuthenticatedUserResult

        sealed interface ReposAcceptInvitationForAuthenticatedUserResult {
            data object NoContent : ReposAcceptInvitationForAuthenticatedUserResult

            data object NotModified : ReposAcceptInvitationForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ReposAcceptInvitationForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ReposAcceptInvitationForAuthenticatedUserResult

            data class Conflict(val value: BasicError) : ReposAcceptInvitationForAuthenticatedUserResult
        }

        suspend fun reposAcceptInvitationForAuthenticatedUser(
            invitationId: Long,
        ): ReposAcceptInvitationForAuthenticatedUserResult
    }

    interface SocialAccounts {
        @Serializable
        @JvmInline
        value class UsersAddSocialAccountForAuthenticatedUserBody(@SerialName("account_urls") val accountUrls: List<String>)


        @Serializable
        @JvmInline
        value class UsersDeleteSocialAccountForAuthenticatedUserBody(@SerialName("account_urls") val accountUrls: List<String>)

        sealed interface UsersListSocialAccountsForAuthenticatedUserResult {
            data class OK(val value: List<SocialAccount>) : UsersListSocialAccountsForAuthenticatedUserResult

            data object NotModified : UsersListSocialAccountsForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListSocialAccountsForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListSocialAccountsForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListSocialAccountsForAuthenticatedUserResult
        }

        suspend fun usersListSocialAccountsForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListSocialAccountsForAuthenticatedUserResult

        sealed interface UsersAddSocialAccountForAuthenticatedUserResult {
            data class Created(val value: List<SocialAccount>) : UsersAddSocialAccountForAuthenticatedUserResult

            data object NotModified : UsersAddSocialAccountForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersAddSocialAccountForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersAddSocialAccountForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersAddSocialAccountForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersAddSocialAccountForAuthenticatedUserResult
        }

        suspend fun usersAddSocialAccountForAuthenticatedUser(
            body: UsersAddSocialAccountForAuthenticatedUserBody,
        ): UsersAddSocialAccountForAuthenticatedUserResult

        sealed interface UsersDeleteSocialAccountForAuthenticatedUserResult {
            data object NoContent : UsersDeleteSocialAccountForAuthenticatedUserResult

            data object NotModified : UsersDeleteSocialAccountForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersDeleteSocialAccountForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersDeleteSocialAccountForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersDeleteSocialAccountForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersDeleteSocialAccountForAuthenticatedUserResult
        }

        suspend fun usersDeleteSocialAccountForAuthenticatedUser(
            body: UsersDeleteSocialAccountForAuthenticatedUserBody,
        ): UsersDeleteSocialAccountForAuthenticatedUserResult
    }

    interface SshSigningKeys {
        @Serializable
        data class UsersCreateSshSigningKeyForAuthenticatedUserBody(val title: String? = null, val key: String)

        sealed interface UsersListSshSigningKeysForAuthenticatedUserResult {
            data class OK(val value: List<SshSigningKey>) : UsersListSshSigningKeysForAuthenticatedUserResult

            data object NotModified : UsersListSshSigningKeysForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersListSshSigningKeysForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersListSshSigningKeysForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersListSshSigningKeysForAuthenticatedUserResult
        }

        suspend fun usersListSshSigningKeysForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): UsersListSshSigningKeysForAuthenticatedUserResult

        sealed interface UsersCreateSshSigningKeyForAuthenticatedUserResult {
            data class Created(val value: SshSigningKey) : UsersCreateSshSigningKeyForAuthenticatedUserResult

            data object NotModified : UsersCreateSshSigningKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersCreateSshSigningKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersCreateSshSigningKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersCreateSshSigningKeyForAuthenticatedUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersCreateSshSigningKeyForAuthenticatedUserResult
        }

        suspend fun usersCreateSshSigningKeyForAuthenticatedUser(
            body: UsersCreateSshSigningKeyForAuthenticatedUserBody,
        ): UsersCreateSshSigningKeyForAuthenticatedUserResult

        sealed interface UsersGetSshSigningKeyForAuthenticatedUserResult {
            data class OK(val value: SshSigningKey) : UsersGetSshSigningKeyForAuthenticatedUserResult

            data object NotModified : UsersGetSshSigningKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersGetSshSigningKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersGetSshSigningKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersGetSshSigningKeyForAuthenticatedUserResult
        }

        suspend fun usersGetSshSigningKeyForAuthenticatedUser(
            sshSigningKeyId: Long,
        ): UsersGetSshSigningKeyForAuthenticatedUserResult

        sealed interface UsersDeleteSshSigningKeyForAuthenticatedUserResult {
            data object NoContent : UsersDeleteSshSigningKeyForAuthenticatedUserResult

            data object NotModified : UsersDeleteSshSigningKeyForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : UsersDeleteSshSigningKeyForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : UsersDeleteSshSigningKeyForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : UsersDeleteSshSigningKeyForAuthenticatedUserResult
        }

        suspend fun usersDeleteSshSigningKeyForAuthenticatedUser(
            sshSigningKeyId: Long,
        ): UsersDeleteSshSigningKeyForAuthenticatedUserResult
    }

    interface Starred {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated;
        }

        sealed interface ActivityListReposStarredByAuthenticatedUserResult {
            data class OK(val value: List<Repository>) : ActivityListReposStarredByAuthenticatedUserResult

            data object NotModified : ActivityListReposStarredByAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ActivityListReposStarredByAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ActivityListReposStarredByAuthenticatedUserResult
        }

        suspend fun activityListReposStarredByAuthenticatedUser(
            direction: Direction = Direction.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
        ): ActivityListReposStarredByAuthenticatedUserResult

        sealed interface ActivityCheckRepoIsStarredByAuthenticatedUserResult {
            data object NoContent : ActivityCheckRepoIsStarredByAuthenticatedUserResult

            data object NotModified : ActivityCheckRepoIsStarredByAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ActivityCheckRepoIsStarredByAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ActivityCheckRepoIsStarredByAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ActivityCheckRepoIsStarredByAuthenticatedUserResult
        }

        suspend fun activityCheckRepoIsStarredByAuthenticatedUser(
            owner: String,
            repo: String,
        ): ActivityCheckRepoIsStarredByAuthenticatedUserResult

        sealed interface ActivityStarRepoForAuthenticatedUserResult {
            data object NoContent : ActivityStarRepoForAuthenticatedUserResult

            data object NotModified : ActivityStarRepoForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ActivityStarRepoForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ActivityStarRepoForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ActivityStarRepoForAuthenticatedUserResult
        }

        suspend fun activityStarRepoForAuthenticatedUser(
            owner: String,
            repo: String,
        ): ActivityStarRepoForAuthenticatedUserResult

        sealed interface ActivityUnstarRepoForAuthenticatedUserResult {
            data object NoContent : ActivityUnstarRepoForAuthenticatedUserResult

            data object NotModified : ActivityUnstarRepoForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ActivityUnstarRepoForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ActivityUnstarRepoForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : ActivityUnstarRepoForAuthenticatedUserResult
        }

        suspend fun activityUnstarRepoForAuthenticatedUser(
            owner: String,
            repo: String,
        ): ActivityUnstarRepoForAuthenticatedUserResult
    }

    interface Subscriptions {
        sealed interface ActivityListWatchedReposForAuthenticatedUserResult {
            data class OK(val value: List<MinimalRepository>) : ActivityListWatchedReposForAuthenticatedUserResult

            data object NotModified : ActivityListWatchedReposForAuthenticatedUserResult

            data class Unauthorized(val value: BasicError) : ActivityListWatchedReposForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : ActivityListWatchedReposForAuthenticatedUserResult
        }

        suspend fun activityListWatchedReposForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): ActivityListWatchedReposForAuthenticatedUserResult
    }

    interface Teams {
        sealed interface TeamsListForAuthenticatedUserResult {
            data class OK(val value: List<TeamFull>) : TeamsListForAuthenticatedUserResult

            data object NotModified : TeamsListForAuthenticatedUserResult

            data class Forbidden(val value: BasicError) : TeamsListForAuthenticatedUserResult

            data class NotFound(val value: BasicError) : TeamsListForAuthenticatedUserResult
        }

        suspend fun teamsListForAuthenticatedUser(
            page: Long = 1L,
            perPage: Long = 30L,
        ): TeamsListForAuthenticatedUserResult
    }

    interface ProjectsV2 {
        val drafts: User.ProjectsV2.Drafts

        interface Drafts {
            @Serializable
            data class ProjectsCreateDraftItemForAuthenticatedUserBody(val title: String, val body: String? = null)

            sealed interface ProjectsCreateDraftItemForAuthenticatedUserResult {
                data class Created(val value: ProjectsV2ItemSimple) : ProjectsCreateDraftItemForAuthenticatedUserResult

                data object NotModified : ProjectsCreateDraftItemForAuthenticatedUserResult

                data class Unauthorized(val value: BasicError) : ProjectsCreateDraftItemForAuthenticatedUserResult

                data class Forbidden(val value: BasicError) : ProjectsCreateDraftItemForAuthenticatedUserResult
            }

            suspend fun projectsCreateDraftItemForAuthenticatedUser(
                userId: String,
                projectNumber: Long,
                body: ProjectsCreateDraftItemForAuthenticatedUserBody,
            ): ProjectsCreateDraftItemForAuthenticatedUserResult
        }
    }
}

internal class KtorUser(private val client: HttpClient) : User {
    override val blocks: User.Blocks = KtorUserBlocks(client)

    override val codespaces: User.Codespaces = KtorUserCodespaces(client)

    override val docker: User.Docker = KtorUserDocker(client)

    override val email: User.Email = KtorUserEmail(client)

    override val emails: User.Emails = KtorUserEmails(client)

    override val followers: User.Followers = KtorUserFollowers(client)

    override val following: User.Following = KtorUserFollowing(client)

    override val gpgKeys: User.GpgKeys = KtorUserGpgKeys(client)

    override val installations: User.Installations = KtorUserInstallations(client)

    override val interactionLimits: User.InteractionLimits = KtorUserInteractionLimits(client)

    override val issues: User.Issues = KtorUserIssues(client)

    override val keys: User.Keys = KtorUserKeys(client)

    override val marketplacePurchases: User.MarketplacePurchases = KtorUserMarketplacePurchases(client)

    override val memberships: User.Memberships = KtorUserMemberships(client)

    override val migrations: User.Migrations = KtorUserMigrations(client)

    override val orgs: User.Orgs = KtorUserOrgs(client)

    override val packages: User.Packages = KtorUserPackages(client)

    override val publicEmails: User.PublicEmails = KtorUserPublicEmails(client)

    override val repos: User.Repos = KtorUserRepos(client)

    override val repositoryInvitations: User.RepositoryInvitations = KtorUserRepositoryInvitations(client)

    override val socialAccounts: User.SocialAccounts = KtorUserSocialAccounts(client)

    override val sshSigningKeys: User.SshSigningKeys = KtorUserSshSigningKeys(client)

    override val starred: User.Starred = KtorUserStarred(client)

    override val subscriptions: User.Subscriptions = KtorUserSubscriptions(client)

    override val teams: User.Teams = KtorUserTeams(client)

    override val projectsV2: User.ProjectsV2 = KtorUserProjectsV2(client)

    override suspend fun usersGetAuthenticated(): User.UsersGetAuthenticatedResult {
        val response = client.get("/user")
        return when (response.status) {
            HttpStatusCode.OK -> User.UsersGetAuthenticatedResult.OK(response.body())
            HttpStatusCode.NotModified -> User.UsersGetAuthenticatedResult.NotModified
            HttpStatusCode.Unauthorized -> User.UsersGetAuthenticatedResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.UsersGetAuthenticatedResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersUpdateAuthenticated(body: User.UsersUpdateAuthenticatedBody?): User.UsersUpdateAuthenticatedResult {
        val response = client.patch("/user") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.UsersUpdateAuthenticatedResult.OK(response.body())
            HttpStatusCode.NotModified -> User.UsersUpdateAuthenticatedResult.NotModified
            HttpStatusCode.Unauthorized -> User.UsersUpdateAuthenticatedResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.UsersUpdateAuthenticatedResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.UsersUpdateAuthenticatedResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.UsersUpdateAuthenticatedResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersGetById(accountId: Long): User.UsersGetByIdResult {
        val response = client.get("/user/$accountId")
        return when (response.status) {
            HttpStatusCode.OK -> User.UsersGetByIdResult.OK(response.body())
            HttpStatusCode.NotFound -> User.UsersGetByIdResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserBlocks(private val client: HttpClient) : User.Blocks {
    override suspend fun usersListBlockedByAuthenticatedUser(page: Long, perPage: Long): User.Blocks.UsersListBlockedByAuthenticatedUserResult {
        val response = client.get("/user/blocks") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Blocks.UsersListBlockedByAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Blocks.UsersListBlockedByAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Blocks.UsersListBlockedByAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Blocks.UsersListBlockedByAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Blocks.UsersListBlockedByAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersCheckBlocked(username: String): User.Blocks.UsersCheckBlockedResult {
        val response = client.get("/user/blocks/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Blocks.UsersCheckBlockedResult.NoContent
            HttpStatusCode.NotModified -> User.Blocks.UsersCheckBlockedResult.NotModified
            HttpStatusCode.Unauthorized -> User.Blocks.UsersCheckBlockedResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Blocks.UsersCheckBlockedResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Blocks.UsersCheckBlockedResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersBlock(username: String): User.Blocks.UsersBlockResult {
        val response = client.put("/user/blocks/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Blocks.UsersBlockResult.NoContent
            HttpStatusCode.NotModified -> User.Blocks.UsersBlockResult.NotModified
            HttpStatusCode.Unauthorized -> User.Blocks.UsersBlockResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Blocks.UsersBlockResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Blocks.UsersBlockResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Blocks.UsersBlockResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersUnblock(username: String): User.Blocks.UsersUnblockResult {
        val response = client.delete("/user/blocks/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Blocks.UsersUnblockResult.NoContent
            HttpStatusCode.NotModified -> User.Blocks.UsersUnblockResult.NotModified
            HttpStatusCode.Unauthorized -> User.Blocks.UsersUnblockResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Blocks.UsersUnblockResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Blocks.UsersUnblockResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespaces(private val client: HttpClient) : User.Codespaces {
    override val secrets: User.Codespaces.Secrets = KtorUserCodespacesSecrets(client)

    override val exports: User.Codespaces.Exports = KtorUserCodespacesExports(client)

    override val machines: User.Codespaces.Machines = KtorUserCodespacesMachines(client)

    override val publish: User.Codespaces.Publish = KtorUserCodespacesPublish(client)

    override val start: User.Codespaces.Start = KtorUserCodespacesStart(client)

    override val stop: User.Codespaces.Stop = KtorUserCodespacesStop(client)

    override suspend fun codespacesListForAuthenticatedUser(page: Long, perPage: Long, repositoryId: Long?): User.Codespaces.CodespacesListForAuthenticatedUserResult {
        val response = client.get("/user/codespaces") {
            parameter("page", page)
            parameter("per_page", perPage)
            repositoryId?.let { parameter("repository_id", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.CodespacesListForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Codespaces.CodespacesListForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Codespaces.CodespacesListForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.CodespacesListForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.CodespacesListForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.CodespacesListForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesCreateForAuthenticatedUser(body: User.Codespaces.CodespacesCreateForAuthenticatedUserBody): User.Codespaces.CodespacesCreateForAuthenticatedUserResult {
        val response = client.post("/user/codespaces") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Codespaces.CodespacesCreateForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.Accepted -> User.Codespaces.CodespacesCreateForAuthenticatedUserResult.Accepted(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.CodespacesCreateForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.CodespacesCreateForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.CodespacesCreateForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.ServiceUnavailable -> User.Codespaces.CodespacesCreateForAuthenticatedUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesGetForAuthenticatedUser(codespaceName: String): User.Codespaces.CodespacesGetForAuthenticatedUserResult {
        val response = client.get("/user/codespaces/$codespaceName")
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.CodespacesGetForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Codespaces.CodespacesGetForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Codespaces.CodespacesGetForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.CodespacesGetForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.CodespacesGetForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.CodespacesGetForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesDeleteForAuthenticatedUser(codespaceName: String): User.Codespaces.CodespacesDeleteForAuthenticatedUserResult {
        val response = client.delete("/user/codespaces/$codespaceName")
        return when (response.status) {
            HttpStatusCode.Accepted -> User.Codespaces.CodespacesDeleteForAuthenticatedUserResult.Accepted(response.body())
            HttpStatusCode.NotModified -> User.Codespaces.CodespacesDeleteForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Codespaces.CodespacesDeleteForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.CodespacesDeleteForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.CodespacesDeleteForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.CodespacesDeleteForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesUpdateForAuthenticatedUser(codespaceName: String, body: User.Codespaces.CodespacesUpdateForAuthenticatedUserBody?): User.Codespaces.CodespacesUpdateForAuthenticatedUserResult {
        val response = client.patch("/user/codespaces/$codespaceName") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.CodespacesUpdateForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.CodespacesUpdateForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.CodespacesUpdateForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.CodespacesUpdateForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespacesSecrets(private val client: HttpClient) : User.Codespaces.Secrets {
    override val publicKey: User.Codespaces.Secrets.PublicKey = KtorUserCodespacesSecretsPublicKey(client)

    override val repositories: User.Codespaces.Secrets.Repositories = KtorUserCodespacesSecretsRepositories(client)

    override suspend fun codespacesListSecretsForAuthenticatedUser(page: Long, perPage: Long): User.Codespaces.Secrets.CodespacesListSecretsForAuthenticatedUserResponse =
        client.get("/user/codespaces/secrets") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun codespacesGetSecretForAuthenticatedUser(secretName: String): CodespacesSecret =
        client.get("/user/codespaces/secrets/$secretName").body()

    override suspend fun codespacesCreateOrUpdateSecretForAuthenticatedUser(secretName: String, body: User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserBody): User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserResult {
        val response = client.put("/user/codespaces/secrets/$secretName") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NoContent -> User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotFound -> User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Codespaces.Secrets.CodespacesCreateOrUpdateSecretForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesDeleteSecretForAuthenticatedUser(secretName: String): Unit =
        client.delete("/user/codespaces/secrets/$secretName").body()
}

internal class KtorUserCodespacesSecretsPublicKey(private val client: HttpClient) : User.Codespaces.Secrets.PublicKey {
    override suspend fun codespacesGetPublicKeyForAuthenticatedUser(): CodespacesUserPublicKey =
        client.get("/user/codespaces/secrets/public-key").body()
}

internal class KtorUserCodespacesSecretsRepositories(private val client: HttpClient) : User.Codespaces.Secrets.Repositories {
    override suspend fun codespacesListRepositoriesForSecretForAuthenticatedUser(secretName: String): User.Codespaces.Secrets.Repositories.CodespacesListRepositoriesForSecretForAuthenticatedUserResult {
        val response = client.get("/user/codespaces/secrets/$secretName/repositories")
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.Secrets.Repositories.CodespacesListRepositoriesForSecretForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.Secrets.Repositories.CodespacesListRepositoriesForSecretForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Secrets.Repositories.CodespacesListRepositoriesForSecretForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Secrets.Repositories.CodespacesListRepositoriesForSecretForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Secrets.Repositories.CodespacesListRepositoriesForSecretForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesSetRepositoriesForSecretForAuthenticatedUser(secretName: String, body: User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserBody): User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserResult {
        val response = client.put("/user/codespaces/secrets/$secretName/repositories") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Secrets.Repositories.CodespacesSetRepositoriesForSecretForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesAddRepositoryForSecretForAuthenticatedUser(secretName: String, repositoryId: Long): User.Codespaces.Secrets.Repositories.CodespacesAddRepositoryForSecretForAuthenticatedUserResult {
        val response = client.put("/user/codespaces/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Codespaces.Secrets.Repositories.CodespacesAddRepositoryForSecretForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Codespaces.Secrets.Repositories.CodespacesAddRepositoryForSecretForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Secrets.Repositories.CodespacesAddRepositoryForSecretForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Secrets.Repositories.CodespacesAddRepositoryForSecretForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Secrets.Repositories.CodespacesAddRepositoryForSecretForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesRemoveRepositoryForSecretForAuthenticatedUser(secretName: String, repositoryId: Long): User.Codespaces.Secrets.Repositories.CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult {
        val response = client.delete("/user/codespaces/secrets/$secretName/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Codespaces.Secrets.Repositories.CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Codespaces.Secrets.Repositories.CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Secrets.Repositories.CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Secrets.Repositories.CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Secrets.Repositories.CodespacesRemoveRepositoryForSecretForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespacesExports(private val client: HttpClient) : User.Codespaces.Exports {
    override suspend fun codespacesExportForAuthenticatedUser(codespaceName: String): User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult {
        val response = client.post("/user/codespaces/$codespaceName/exports")
        return when (response.status) {
            HttpStatusCode.Accepted -> User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult.Accepted(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult.UnprocessableEntity(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Exports.CodespacesExportForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codespacesGetExportDetailsForAuthenticatedUser(codespaceName: String, exportId: String): User.Codespaces.Exports.CodespacesGetExportDetailsForAuthenticatedUserResult {
        val response = client.get("/user/codespaces/$codespaceName/exports/$exportId")
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.Exports.CodespacesGetExportDetailsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Exports.CodespacesGetExportDetailsForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespacesMachines(private val client: HttpClient) : User.Codespaces.Machines {
    override suspend fun codespacesCodespaceMachinesForAuthenticatedUser(codespaceName: String): User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult {
        val response = client.get("/user/codespaces/$codespaceName/machines")
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Machines.CodespacesCodespaceMachinesForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespacesPublish(private val client: HttpClient) : User.Codespaces.Publish {
    override suspend fun codespacesPublishForAuthenticatedUser(codespaceName: String, body: User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserBody): User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserResult {
        val response = client.post("/user/codespaces/$codespaceName/publish") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Codespaces.Publish.CodespacesPublishForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespacesStart(private val client: HttpClient) : User.Codespaces.Start {
    override suspend fun codespacesStartForAuthenticatedUser(codespaceName: String): User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult {
        val response = client.post("/user/codespaces/$codespaceName/start")
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.NotModified
            HttpStatusCode.BadRequest -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.BadRequest(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.PaymentRequired -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.PaymentRequired(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.Conflict -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.Conflict(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Start.CodespacesStartForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserCodespacesStop(private val client: HttpClient) : User.Codespaces.Stop {
    override suspend fun codespacesStopForAuthenticatedUser(codespaceName: String): User.Codespaces.Stop.CodespacesStopForAuthenticatedUserResult {
        val response = client.post("/user/codespaces/$codespaceName/stop")
        return when (response.status) {
            HttpStatusCode.OK -> User.Codespaces.Stop.CodespacesStopForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> User.Codespaces.Stop.CodespacesStopForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Codespaces.Stop.CodespacesStopForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Codespaces.Stop.CodespacesStopForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> User.Codespaces.Stop.CodespacesStopForAuthenticatedUserResult.InternalServerError(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserDocker(private val client: HttpClient) : User.Docker {
    override val conflicts: User.Docker.Conflicts = KtorUserDockerConflicts(client)
}

internal class KtorUserDockerConflicts(private val client: HttpClient) : User.Docker.Conflicts {
    override suspend fun packagesListDockerMigrationConflictingPackagesForAuthenticatedUser(): List<Package> =
        client.get("/user/docker/conflicts").body()
}

internal class KtorUserEmail(private val client: HttpClient) : User.Email {
    override val visibility: User.Email.Visibility = KtorUserEmailVisibility(client)
}

internal class KtorUserEmailVisibility(private val client: HttpClient) : User.Email.Visibility {
    override suspend fun usersSetPrimaryEmailVisibilityForAuthenticatedUser(body: User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserBody): User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult {
        val response = client.patch("/user/email/visibility") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Email.Visibility.UsersSetPrimaryEmailVisibilityForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserEmails(private val client: HttpClient) : User.Emails {
    override suspend fun usersListEmailsForAuthenticatedUser(page: Long, perPage: Long): User.Emails.UsersListEmailsForAuthenticatedUserResult {
        val response = client.get("/user/emails") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Emails.UsersListEmailsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Emails.UsersListEmailsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Emails.UsersListEmailsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Emails.UsersListEmailsForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Emails.UsersListEmailsForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersAddEmailForAuthenticatedUser(body: User.Emails.UsersAddEmailForAuthenticatedUserBody?): User.Emails.UsersAddEmailForAuthenticatedUserResult {
        val response = client.post("/user/emails") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Emails.UsersAddEmailForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.Emails.UsersAddEmailForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Emails.UsersAddEmailForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Emails.UsersAddEmailForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Emails.UsersAddEmailForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Emails.UsersAddEmailForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersDeleteEmailForAuthenticatedUser(body: User.Emails.UsersDeleteEmailForAuthenticatedUserBody?): User.Emails.UsersDeleteEmailForAuthenticatedUserResult {
        val response = client.delete("/user/emails") {
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Emails.UsersDeleteEmailForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Emails.UsersDeleteEmailForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Emails.UsersDeleteEmailForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Emails.UsersDeleteEmailForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Emails.UsersDeleteEmailForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Emails.UsersDeleteEmailForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserFollowers(private val client: HttpClient) : User.Followers {
    override suspend fun usersListFollowersForAuthenticatedUser(page: Long, perPage: Long): User.Followers.UsersListFollowersForAuthenticatedUserResult {
        val response = client.get("/user/followers") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Followers.UsersListFollowersForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Followers.UsersListFollowersForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Followers.UsersListFollowersForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Followers.UsersListFollowersForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserFollowing(private val client: HttpClient) : User.Following {
    override suspend fun usersListFollowedByAuthenticatedUser(page: Long, perPage: Long): User.Following.UsersListFollowedByAuthenticatedUserResult {
        val response = client.get("/user/following") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Following.UsersListFollowedByAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Following.UsersListFollowedByAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Following.UsersListFollowedByAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Following.UsersListFollowedByAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersCheckPersonIsFollowedByAuthenticated(username: String): User.Following.UsersCheckPersonIsFollowedByAuthenticatedResult {
        val response = client.get("/user/following/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Following.UsersCheckPersonIsFollowedByAuthenticatedResult.NoContent
            HttpStatusCode.NotModified -> User.Following.UsersCheckPersonIsFollowedByAuthenticatedResult.NotModified
            HttpStatusCode.Unauthorized -> User.Following.UsersCheckPersonIsFollowedByAuthenticatedResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Following.UsersCheckPersonIsFollowedByAuthenticatedResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Following.UsersCheckPersonIsFollowedByAuthenticatedResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersFollow(username: String): User.Following.UsersFollowResult {
        val response = client.put("/user/following/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Following.UsersFollowResult.NoContent
            HttpStatusCode.NotModified -> User.Following.UsersFollowResult.NotModified
            HttpStatusCode.Unauthorized -> User.Following.UsersFollowResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Following.UsersFollowResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Following.UsersFollowResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Following.UsersFollowResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersUnfollow(username: String): User.Following.UsersUnfollowResult {
        val response = client.delete("/user/following/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Following.UsersUnfollowResult.NoContent
            HttpStatusCode.NotModified -> User.Following.UsersUnfollowResult.NotModified
            HttpStatusCode.Unauthorized -> User.Following.UsersUnfollowResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Following.UsersUnfollowResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Following.UsersUnfollowResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserGpgKeys(private val client: HttpClient) : User.GpgKeys {
    override suspend fun usersListGpgKeysForAuthenticatedUser(page: Long, perPage: Long): User.GpgKeys.UsersListGpgKeysForAuthenticatedUserResult {
        val response = client.get("/user/gpg_keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.GpgKeys.UsersListGpgKeysForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.GpgKeys.UsersListGpgKeysForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.GpgKeys.UsersListGpgKeysForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.GpgKeys.UsersListGpgKeysForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.GpgKeys.UsersListGpgKeysForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersCreateGpgKeyForAuthenticatedUser(body: User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserBody): User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult {
        val response = client.post("/user/gpg_keys") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.GpgKeys.UsersCreateGpgKeyForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersGetGpgKeyForAuthenticatedUser(gpgKeyId: Long): User.GpgKeys.UsersGetGpgKeyForAuthenticatedUserResult {
        val response = client.get("/user/gpg_keys/$gpgKeyId")
        return when (response.status) {
            HttpStatusCode.OK -> User.GpgKeys.UsersGetGpgKeyForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.GpgKeys.UsersGetGpgKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.GpgKeys.UsersGetGpgKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.GpgKeys.UsersGetGpgKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.GpgKeys.UsersGetGpgKeyForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersDeleteGpgKeyForAuthenticatedUser(gpgKeyId: Long): User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult {
        val response = client.delete("/user/gpg_keys/$gpgKeyId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.GpgKeys.UsersDeleteGpgKeyForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserInstallations(private val client: HttpClient) : User.Installations {
    override val repositories: User.Installations.Repositories = KtorUserInstallationsRepositories(client)

    override suspend fun appsListInstallationsForAuthenticatedUser(page: Long, perPage: Long): User.Installations.AppsListInstallationsForAuthenticatedUserResult {
        val response = client.get("/user/installations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Installations.AppsListInstallationsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Installations.AppsListInstallationsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Installations.AppsListInstallationsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Installations.AppsListInstallationsForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserInstallationsRepositories(private val client: HttpClient) : User.Installations.Repositories {
    override suspend fun appsListInstallationReposForAuthenticatedUser(installationId: Long, page: Long, perPage: Long): User.Installations.Repositories.AppsListInstallationReposForAuthenticatedUserResult {
        val response = client.get("/user/installations/$installationId/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Installations.Repositories.AppsListInstallationReposForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Installations.Repositories.AppsListInstallationReposForAuthenticatedUserResult.NotModified
            HttpStatusCode.Forbidden -> User.Installations.Repositories.AppsListInstallationReposForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Installations.Repositories.AppsListInstallationReposForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsAddRepoToInstallationForAuthenticatedUser(installationId: Long, repositoryId: Long): User.Installations.Repositories.AppsAddRepoToInstallationForAuthenticatedUserResult {
        val response = client.put("/user/installations/$installationId/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Installations.Repositories.AppsAddRepoToInstallationForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Installations.Repositories.AppsAddRepoToInstallationForAuthenticatedUserResult.NotModified
            HttpStatusCode.Forbidden -> User.Installations.Repositories.AppsAddRepoToInstallationForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Installations.Repositories.AppsAddRepoToInstallationForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun appsRemoveRepoFromInstallationForAuthenticatedUser(installationId: Long, repositoryId: Long): User.Installations.Repositories.AppsRemoveRepoFromInstallationForAuthenticatedUserResult {
        val response = client.delete("/user/installations/$installationId/repositories/$repositoryId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Installations.Repositories.AppsRemoveRepoFromInstallationForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Installations.Repositories.AppsRemoveRepoFromInstallationForAuthenticatedUserResult.NotModified
            HttpStatusCode.Forbidden -> User.Installations.Repositories.AppsRemoveRepoFromInstallationForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Installations.Repositories.AppsRemoveRepoFromInstallationForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Installations.Repositories.AppsRemoveRepoFromInstallationForAuthenticatedUserResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserInteractionLimits(private val client: HttpClient) : User.InteractionLimits {
    override suspend fun interactionsGetRestrictionsForAuthenticatedUser(): User.InteractionLimits.InteractionsGetRestrictionsForAuthenticatedUserResult {
        val response = client.get("/user/interaction-limits")
        return when (response.status) {
            HttpStatusCode.OK -> User.InteractionLimits.InteractionsGetRestrictionsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NoContent -> User.InteractionLimits.InteractionsGetRestrictionsForAuthenticatedUserResult.NoContent
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun interactionsSetRestrictionsForAuthenticatedUser(body: InteractionLimit): User.InteractionLimits.InteractionsSetRestrictionsForAuthenticatedUserResult {
        val response = client.put("/user/interaction-limits") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.InteractionLimits.InteractionsSetRestrictionsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> User.InteractionLimits.InteractionsSetRestrictionsForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun interactionsRemoveRestrictionsForAuthenticatedUser(): Unit =
        client.delete("/user/interaction-limits").body()
}

internal class KtorUserIssues(private val client: HttpClient) : User.Issues {
    override suspend fun issuesListForAuthenticatedUser(direction: User.Issues.Direction, filter: User.Issues.Filter, page: Long, perPage: Long, sort: User.Issues.Sort, state: User.Issues.State, labels: String?, since: LocalDateTime?): User.Issues.IssuesListForAuthenticatedUserResult {
        val response = client.get("/user/issues") {
            parameter("direction", direction)
            parameter("filter", filter)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("state", state)
            labels?.let { parameter("labels", it) }
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Issues.IssuesListForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Issues.IssuesListForAuthenticatedUserResult.NotModified
            HttpStatusCode.NotFound -> User.Issues.IssuesListForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserKeys(private val client: HttpClient) : User.Keys {
    override suspend fun usersListPublicSshKeysForAuthenticatedUser(page: Long, perPage: Long): User.Keys.UsersListPublicSshKeysForAuthenticatedUserResult {
        val response = client.get("/user/keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Keys.UsersListPublicSshKeysForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Keys.UsersListPublicSshKeysForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Keys.UsersListPublicSshKeysForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Keys.UsersListPublicSshKeysForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Keys.UsersListPublicSshKeysForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersCreatePublicSshKeyForAuthenticatedUser(body: User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserBody): User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult {
        val response = client.post("/user/keys") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Keys.UsersCreatePublicSshKeyForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersGetPublicSshKeyForAuthenticatedUser(keyId: Long): User.Keys.UsersGetPublicSshKeyForAuthenticatedUserResult {
        val response = client.get("/user/keys/$keyId")
        return when (response.status) {
            HttpStatusCode.OK -> User.Keys.UsersGetPublicSshKeyForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Keys.UsersGetPublicSshKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Keys.UsersGetPublicSshKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Keys.UsersGetPublicSshKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Keys.UsersGetPublicSshKeyForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersDeletePublicSshKeyForAuthenticatedUser(keyId: Long): User.Keys.UsersDeletePublicSshKeyForAuthenticatedUserResult {
        val response = client.delete("/user/keys/$keyId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Keys.UsersDeletePublicSshKeyForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Keys.UsersDeletePublicSshKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Keys.UsersDeletePublicSshKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Keys.UsersDeletePublicSshKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Keys.UsersDeletePublicSshKeyForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMarketplacePurchases(private val client: HttpClient) : User.MarketplacePurchases {
    override val stubbed: User.MarketplacePurchases.Stubbed = KtorUserMarketplacePurchasesStubbed(client)

    override suspend fun appsListSubscriptionsForAuthenticatedUser(page: Long, perPage: Long): User.MarketplacePurchases.AppsListSubscriptionsForAuthenticatedUserResult {
        val response = client.get("/user/marketplace_purchases") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.MarketplacePurchases.AppsListSubscriptionsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.MarketplacePurchases.AppsListSubscriptionsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.MarketplacePurchases.AppsListSubscriptionsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.NotFound -> User.MarketplacePurchases.AppsListSubscriptionsForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMarketplacePurchasesStubbed(private val client: HttpClient) : User.MarketplacePurchases.Stubbed {
    override suspend fun appsListSubscriptionsForAuthenticatedUserStubbed(page: Long, perPage: Long): User.MarketplacePurchases.Stubbed.AppsListSubscriptionsForAuthenticatedUserStubbedResult {
        val response = client.get("/user/marketplace_purchases/stubbed") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.MarketplacePurchases.Stubbed.AppsListSubscriptionsForAuthenticatedUserStubbedResult.OK(response.body())
            HttpStatusCode.NotModified -> User.MarketplacePurchases.Stubbed.AppsListSubscriptionsForAuthenticatedUserStubbedResult.NotModified
            HttpStatusCode.Unauthorized -> User.MarketplacePurchases.Stubbed.AppsListSubscriptionsForAuthenticatedUserStubbedResult.Unauthorized(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMemberships(private val client: HttpClient) : User.Memberships {
    override val orgs: User.Memberships.OrgsApi = KtorUserMembershipsOrgsApi(client)
}

internal class KtorUserMembershipsOrgsApi(private val client: HttpClient) : User.Memberships.OrgsApi {
    override suspend fun orgsListMembershipsForAuthenticatedUser(page: Long, perPage: Long, state: User.Memberships.OrgsApi.State?): User.Memberships.OrgsApi.OrgsListMembershipsForAuthenticatedUserResult {
        val response = client.get("/user/memberships/orgs") {
            parameter("page", page)
            parameter("per_page", perPage)
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Memberships.OrgsApi.OrgsListMembershipsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Memberships.OrgsApi.OrgsListMembershipsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Memberships.OrgsApi.OrgsListMembershipsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Memberships.OrgsApi.OrgsListMembershipsForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Memberships.OrgsApi.OrgsListMembershipsForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsGetMembershipForAuthenticatedUser(org: String): User.Memberships.OrgsApi.OrgsGetMembershipForAuthenticatedUserResult {
        val response = client.get("/user/memberships/orgs/$org")
        return when (response.status) {
            HttpStatusCode.OK -> User.Memberships.OrgsApi.OrgsGetMembershipForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Forbidden -> User.Memberships.OrgsApi.OrgsGetMembershipForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Memberships.OrgsApi.OrgsGetMembershipForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun orgsUpdateMembershipForAuthenticatedUser(org: String, body: User.Memberships.OrgsApi.OrgsUpdateMembershipForAuthenticatedUserBody): User.Memberships.OrgsApi.OrgsUpdateMembershipForAuthenticatedUserResult {
        val response = client.patch("/user/memberships/orgs/$org") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Memberships.OrgsApi.OrgsUpdateMembershipForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Forbidden -> User.Memberships.OrgsApi.OrgsUpdateMembershipForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Memberships.OrgsApi.OrgsUpdateMembershipForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Memberships.OrgsApi.OrgsUpdateMembershipForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMigrations(private val client: HttpClient) : User.Migrations {
    override val archive: User.Migrations.Archive = KtorUserMigrationsArchive(client)

    override val repos: User.Migrations.ReposApi = KtorUserMigrationsReposApi(client)

    override val repositories: User.Migrations.Repositories = KtorUserMigrationsRepositories(client)

    override suspend fun migrationsListForAuthenticatedUser(page: Long, perPage: Long): User.Migrations.MigrationsListForAuthenticatedUserResult {
        val response = client.get("/user/migrations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Migrations.MigrationsListForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Migrations.MigrationsListForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Migrations.MigrationsListForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Migrations.MigrationsListForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun migrationsStartForAuthenticatedUser(body: User.Migrations.MigrationsStartForAuthenticatedUserBody): User.Migrations.MigrationsStartForAuthenticatedUserResult {
        val response = client.post("/user/migrations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Migrations.MigrationsStartForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.Migrations.MigrationsStartForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Migrations.MigrationsStartForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Migrations.MigrationsStartForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Migrations.MigrationsStartForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun migrationsGetStatusForAuthenticatedUser(migrationId: Long, exclude: List<String>?): User.Migrations.MigrationsGetStatusForAuthenticatedUserResult {
        val response = client.get("/user/migrations/$migrationId") {
            exclude?.let { parameter("exclude", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Migrations.MigrationsGetStatusForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Migrations.MigrationsGetStatusForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Migrations.MigrationsGetStatusForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Migrations.MigrationsGetStatusForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Migrations.MigrationsGetStatusForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMigrationsArchive(private val client: HttpClient) : User.Migrations.Archive {
    override suspend fun migrationsGetArchiveForAuthenticatedUser(migrationId: Long): User.Migrations.Archive.MigrationsGetArchiveForAuthenticatedUserResult {
        val response = client.get("/user/migrations/$migrationId/archive")
        return when (response.status) {
            HttpStatusCode.Found -> User.Migrations.Archive.MigrationsGetArchiveForAuthenticatedUserResult.Found
            HttpStatusCode.NotModified -> User.Migrations.Archive.MigrationsGetArchiveForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Migrations.Archive.MigrationsGetArchiveForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Migrations.Archive.MigrationsGetArchiveForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun migrationsDeleteArchiveForAuthenticatedUser(migrationId: Long): User.Migrations.Archive.MigrationsDeleteArchiveForAuthenticatedUserResult {
        val response = client.delete("/user/migrations/$migrationId/archive")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Migrations.Archive.MigrationsDeleteArchiveForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Migrations.Archive.MigrationsDeleteArchiveForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Migrations.Archive.MigrationsDeleteArchiveForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Migrations.Archive.MigrationsDeleteArchiveForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Migrations.Archive.MigrationsDeleteArchiveForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMigrationsReposApi(private val client: HttpClient) : User.Migrations.ReposApi {
    override val lock: User.Migrations.ReposApi.Lock = KtorUserMigrationsReposApiLock(client)
}

internal class KtorUserMigrationsReposApiLock(private val client: HttpClient) : User.Migrations.ReposApi.Lock {
    override suspend fun migrationsUnlockRepoForAuthenticatedUser(migrationId: Long, repoName: String): User.Migrations.ReposApi.Lock.MigrationsUnlockRepoForAuthenticatedUserResult {
        val response = client.delete("/user/migrations/$migrationId/repos/$repoName/lock")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Migrations.ReposApi.Lock.MigrationsUnlockRepoForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Migrations.ReposApi.Lock.MigrationsUnlockRepoForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Migrations.ReposApi.Lock.MigrationsUnlockRepoForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Migrations.ReposApi.Lock.MigrationsUnlockRepoForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Migrations.ReposApi.Lock.MigrationsUnlockRepoForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserMigrationsRepositories(private val client: HttpClient) : User.Migrations.Repositories {
    override suspend fun migrationsListReposForAuthenticatedUser(migrationId: Long, page: Long, perPage: Long): User.Migrations.Repositories.MigrationsListReposForAuthenticatedUserResult {
        val response = client.get("/user/migrations/$migrationId/repositories") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Migrations.Repositories.MigrationsListReposForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotFound -> User.Migrations.Repositories.MigrationsListReposForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserOrgs(private val client: HttpClient) : User.Orgs {
    override suspend fun orgsListForAuthenticatedUser(page: Long, perPage: Long): User.Orgs.OrgsListForAuthenticatedUserResult {
        val response = client.get("/user/orgs") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Orgs.OrgsListForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Orgs.OrgsListForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Orgs.OrgsListForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Orgs.OrgsListForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserPackages(private val client: HttpClient) : User.Packages {
    override val restore: User.Packages.Restore = KtorUserPackagesRestore(client)

    override val versions: User.Packages.Versions = KtorUserPackagesVersions(client)

    override suspend fun packagesListPackagesForAuthenticatedUser(packageType: User.Packages.PackagesListPackagesForAuthenticatedUserPackageType, page: Long, perPage: Long, visibility: User.Packages.Visibility?): User.Packages.PackagesListPackagesForAuthenticatedUserResult {
        val response = client.get("/user/packages") {
            parameter("package_type", packageType)
            parameter("page", page)
            parameter("per_page", perPage)
            visibility?.let { parameter("visibility", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Packages.PackagesListPackagesForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.BadRequest -> User.Packages.PackagesListPackagesForAuthenticatedUserResult.BadRequest
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun packagesGetPackageForAuthenticatedUser(packageType: User.Packages.PackagesGetPackageForAuthenticatedUserPackageType, packageName: String): Package =
        client.get("/user/packages/$packageType/$packageName").body()

    override suspend fun packagesDeletePackageForAuthenticatedUser(packageType: User.Packages.PackageType, packageName: String): User.Packages.PackagesDeletePackageForAuthenticatedUserResult {
        val response = client.delete("/user/packages/$packageType/$packageName")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Packages.PackagesDeletePackageForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Packages.PackagesDeletePackageForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Packages.PackagesDeletePackageForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Packages.PackagesDeletePackageForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserPackagesRestore(private val client: HttpClient) : User.Packages.Restore {
    override suspend fun packagesRestorePackageForAuthenticatedUser(packageType: User.Packages.Restore.PackagesRestorePackageForAuthenticatedUserPackageType, packageName: String, token: String?): User.Packages.Restore.PackagesRestorePackageForAuthenticatedUserResult {
        val response = client.post("/user/packages/$packageType/$packageName/restore") {
            token?.let { parameter("token", it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Packages.Restore.PackagesRestorePackageForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Packages.Restore.PackagesRestorePackageForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Packages.Restore.PackagesRestorePackageForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Packages.Restore.PackagesRestorePackageForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserPackagesVersions(private val client: HttpClient) : User.Packages.Versions {
    override val restore: User.Packages.Versions.RestoreApi = KtorUserPackagesVersionsRestoreApi(client)

    override suspend fun packagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUser(packageType: User.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserPackageType, packageName: String, page: Long, perPage: Long, state: User.Packages.Versions.State): User.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult {
        val response = client.get("/user/packages/$packageType/$packageName/versions") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("state", state)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> User.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun packagesGetPackageVersionForAuthenticatedUser(packageType: User.Packages.Versions.PackagesGetPackageVersionForAuthenticatedUserPackageType, packageName: String, packageVersionId: Long): PackageVersion =
        client.get("/user/packages/$packageType/$packageName/versions/$packageVersionId").body()

    override suspend fun packagesDeletePackageVersionForAuthenticatedUser(packageType: User.Packages.Versions.PackagesDeletePackageVersionForAuthenticatedUserPackageType, packageName: String, packageVersionId: Long): User.Packages.Versions.PackagesDeletePackageVersionForAuthenticatedUserResult {
        val response = client.delete("/user/packages/$packageType/$packageName/versions/$packageVersionId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Packages.Versions.PackagesDeletePackageVersionForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Packages.Versions.PackagesDeletePackageVersionForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Packages.Versions.PackagesDeletePackageVersionForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Packages.Versions.PackagesDeletePackageVersionForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserPackagesVersionsRestoreApi(private val client: HttpClient) : User.Packages.Versions.RestoreApi {
    override suspend fun packagesRestorePackageVersionForAuthenticatedUser(packageType: User.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForAuthenticatedUserPackageType, packageName: String, packageVersionId: Long): User.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForAuthenticatedUserResult {
        val response = client.post("/user/packages/$packageType/$packageName/versions/$packageVersionId/restore")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForAuthenticatedUserResult.NoContent
            HttpStatusCode.Unauthorized -> User.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserPublicEmails(private val client: HttpClient) : User.PublicEmails {
    override suspend fun usersListPublicEmailsForAuthenticatedUser(page: Long, perPage: Long): User.PublicEmails.UsersListPublicEmailsForAuthenticatedUserResult {
        val response = client.get("/user/public_emails") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.PublicEmails.UsersListPublicEmailsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.PublicEmails.UsersListPublicEmailsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.PublicEmails.UsersListPublicEmailsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.PublicEmails.UsersListPublicEmailsForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.PublicEmails.UsersListPublicEmailsForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserRepos(private val client: HttpClient) : User.Repos {
    override suspend fun reposListForAuthenticatedUser(affiliation: String, page: Long, perPage: Long, sort: User.Repos.Sort, type: User.Repos.Type, visibility: User.Repos.Visibility, before: LocalDateTime?, direction: User.Repos.Direction?, since: LocalDateTime?): User.Repos.ReposListForAuthenticatedUserResult {
        val response = client.get("/user/repos") {
            parameter("affiliation", affiliation)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("type", type)
            parameter("visibility", visibility)
            before?.let { parameter("before", it) }
            direction?.let { parameter("direction", it) }
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Repos.ReposListForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Repos.ReposListForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Repos.ReposListForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Repos.ReposListForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Repos.ReposListForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposCreateForAuthenticatedUser(body: User.Repos.ReposCreateForAuthenticatedUserBody): User.Repos.ReposCreateForAuthenticatedUserResult {
        val response = client.post("/user/repos") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.Repos.ReposCreateForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.Repos.ReposCreateForAuthenticatedUserResult.NotModified
            HttpStatusCode.BadRequest -> User.Repos.ReposCreateForAuthenticatedUserResult.BadRequest(response.body())
            HttpStatusCode.Unauthorized -> User.Repos.ReposCreateForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Repos.ReposCreateForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Repos.ReposCreateForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.Repos.ReposCreateForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserRepositoryInvitations(private val client: HttpClient) : User.RepositoryInvitations {
    override suspend fun reposListInvitationsForAuthenticatedUser(page: Long, perPage: Long): User.RepositoryInvitations.ReposListInvitationsForAuthenticatedUserResult {
        val response = client.get("/user/repository_invitations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.RepositoryInvitations.ReposListInvitationsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.RepositoryInvitations.ReposListInvitationsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.RepositoryInvitations.ReposListInvitationsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.RepositoryInvitations.ReposListInvitationsForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.RepositoryInvitations.ReposListInvitationsForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposDeclineInvitationForAuthenticatedUser(invitationId: Long): User.RepositoryInvitations.ReposDeclineInvitationForAuthenticatedUserResult {
        val response = client.delete("/user/repository_invitations/$invitationId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.RepositoryInvitations.ReposDeclineInvitationForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.RepositoryInvitations.ReposDeclineInvitationForAuthenticatedUserResult.NotModified
            HttpStatusCode.Forbidden -> User.RepositoryInvitations.ReposDeclineInvitationForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.RepositoryInvitations.ReposDeclineInvitationForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.Conflict -> User.RepositoryInvitations.ReposDeclineInvitationForAuthenticatedUserResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun reposAcceptInvitationForAuthenticatedUser(invitationId: Long): User.RepositoryInvitations.ReposAcceptInvitationForAuthenticatedUserResult {
        val response = client.patch("/user/repository_invitations/$invitationId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.RepositoryInvitations.ReposAcceptInvitationForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.RepositoryInvitations.ReposAcceptInvitationForAuthenticatedUserResult.NotModified
            HttpStatusCode.Forbidden -> User.RepositoryInvitations.ReposAcceptInvitationForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.RepositoryInvitations.ReposAcceptInvitationForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.Conflict -> User.RepositoryInvitations.ReposAcceptInvitationForAuthenticatedUserResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserSocialAccounts(private val client: HttpClient) : User.SocialAccounts {
    override suspend fun usersListSocialAccountsForAuthenticatedUser(page: Long, perPage: Long): User.SocialAccounts.UsersListSocialAccountsForAuthenticatedUserResult {
        val response = client.get("/user/social_accounts") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.SocialAccounts.UsersListSocialAccountsForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.SocialAccounts.UsersListSocialAccountsForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SocialAccounts.UsersListSocialAccountsForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SocialAccounts.UsersListSocialAccountsForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SocialAccounts.UsersListSocialAccountsForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersAddSocialAccountForAuthenticatedUser(body: User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserBody): User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult {
        val response = client.post("/user/social_accounts") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.SocialAccounts.UsersAddSocialAccountForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersDeleteSocialAccountForAuthenticatedUser(body: User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserBody): User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult {
        val response = client.delete("/user/social_accounts") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.SocialAccounts.UsersDeleteSocialAccountForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserSshSigningKeys(private val client: HttpClient) : User.SshSigningKeys {
    override suspend fun usersListSshSigningKeysForAuthenticatedUser(page: Long, perPage: Long): User.SshSigningKeys.UsersListSshSigningKeysForAuthenticatedUserResult {
        val response = client.get("/user/ssh_signing_keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.SshSigningKeys.UsersListSshSigningKeysForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.SshSigningKeys.UsersListSshSigningKeysForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SshSigningKeys.UsersListSshSigningKeysForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SshSigningKeys.UsersListSshSigningKeysForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SshSigningKeys.UsersListSshSigningKeysForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersCreateSshSigningKeyForAuthenticatedUser(body: User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserBody): User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult {
        val response = client.post("/user/ssh_signing_keys") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> User.SshSigningKeys.UsersCreateSshSigningKeyForAuthenticatedUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersGetSshSigningKeyForAuthenticatedUser(sshSigningKeyId: Long): User.SshSigningKeys.UsersGetSshSigningKeyForAuthenticatedUserResult {
        val response = client.get("/user/ssh_signing_keys/$sshSigningKeyId")
        return when (response.status) {
            HttpStatusCode.OK -> User.SshSigningKeys.UsersGetSshSigningKeyForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.SshSigningKeys.UsersGetSshSigningKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SshSigningKeys.UsersGetSshSigningKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SshSigningKeys.UsersGetSshSigningKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SshSigningKeys.UsersGetSshSigningKeyForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersDeleteSshSigningKeyForAuthenticatedUser(sshSigningKeyId: Long): User.SshSigningKeys.UsersDeleteSshSigningKeyForAuthenticatedUserResult {
        val response = client.delete("/user/ssh_signing_keys/$sshSigningKeyId")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.SshSigningKeys.UsersDeleteSshSigningKeyForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.SshSigningKeys.UsersDeleteSshSigningKeyForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.SshSigningKeys.UsersDeleteSshSigningKeyForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.SshSigningKeys.UsersDeleteSshSigningKeyForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.SshSigningKeys.UsersDeleteSshSigningKeyForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserStarred(private val client: HttpClient) : User.Starred {
    override suspend fun activityListReposStarredByAuthenticatedUser(direction: User.Starred.Direction, page: Long, perPage: Long, sort: User.Starred.Sort): User.Starred.ActivityListReposStarredByAuthenticatedUserResult {
        val response = client.get("/user/starred") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Starred.ActivityListReposStarredByAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Starred.ActivityListReposStarredByAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Starred.ActivityListReposStarredByAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Starred.ActivityListReposStarredByAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activityCheckRepoIsStarredByAuthenticatedUser(owner: String, repo: String): User.Starred.ActivityCheckRepoIsStarredByAuthenticatedUserResult {
        val response = client.get("/user/starred/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Starred.ActivityCheckRepoIsStarredByAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Starred.ActivityCheckRepoIsStarredByAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Starred.ActivityCheckRepoIsStarredByAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Starred.ActivityCheckRepoIsStarredByAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Starred.ActivityCheckRepoIsStarredByAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activityStarRepoForAuthenticatedUser(owner: String, repo: String): User.Starred.ActivityStarRepoForAuthenticatedUserResult {
        val response = client.put("/user/starred/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Starred.ActivityStarRepoForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Starred.ActivityStarRepoForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Starred.ActivityStarRepoForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Starred.ActivityStarRepoForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Starred.ActivityStarRepoForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun activityUnstarRepoForAuthenticatedUser(owner: String, repo: String): User.Starred.ActivityUnstarRepoForAuthenticatedUserResult {
        val response = client.delete("/user/starred/$owner/$repo")
        return when (response.status) {
            HttpStatusCode.NoContent -> User.Starred.ActivityUnstarRepoForAuthenticatedUserResult.NoContent
            HttpStatusCode.NotModified -> User.Starred.ActivityUnstarRepoForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Starred.ActivityUnstarRepoForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Starred.ActivityUnstarRepoForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Starred.ActivityUnstarRepoForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserSubscriptions(private val client: HttpClient) : User.Subscriptions {
    override suspend fun activityListWatchedReposForAuthenticatedUser(page: Long, perPage: Long): User.Subscriptions.ActivityListWatchedReposForAuthenticatedUserResult {
        val response = client.get("/user/subscriptions") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Subscriptions.ActivityListWatchedReposForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Subscriptions.ActivityListWatchedReposForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.Subscriptions.ActivityListWatchedReposForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.Subscriptions.ActivityListWatchedReposForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserTeams(private val client: HttpClient) : User.Teams {
    override suspend fun teamsListForAuthenticatedUser(page: Long, perPage: Long): User.Teams.TeamsListForAuthenticatedUserResult {
        val response = client.get("/user/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> User.Teams.TeamsListForAuthenticatedUserResult.OK(response.body())
            HttpStatusCode.NotModified -> User.Teams.TeamsListForAuthenticatedUserResult.NotModified
            HttpStatusCode.Forbidden -> User.Teams.TeamsListForAuthenticatedUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> User.Teams.TeamsListForAuthenticatedUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUserProjectsV2(private val client: HttpClient) : User.ProjectsV2 {
    override val drafts: User.ProjectsV2.Drafts = KtorUserProjectsV2Drafts(client)
}

internal class KtorUserProjectsV2Drafts(private val client: HttpClient) : User.ProjectsV2.Drafts {
    override suspend fun projectsCreateDraftItemForAuthenticatedUser(userId: String, projectNumber: Long, body: User.ProjectsV2.Drafts.ProjectsCreateDraftItemForAuthenticatedUserBody): User.ProjectsV2.Drafts.ProjectsCreateDraftItemForAuthenticatedUserResult {
        val response = client.post("/user/$userId/projectsV2/$projectNumber/drafts") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> User.ProjectsV2.Drafts.ProjectsCreateDraftItemForAuthenticatedUserResult.Created(response.body())
            HttpStatusCode.NotModified -> User.ProjectsV2.Drafts.ProjectsCreateDraftItemForAuthenticatedUserResult.NotModified
            HttpStatusCode.Unauthorized -> User.ProjectsV2.Drafts.ProjectsCreateDraftItemForAuthenticatedUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> User.ProjectsV2.Drafts.ProjectsCreateDraftItemForAuthenticatedUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
