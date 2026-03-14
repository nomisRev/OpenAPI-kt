package io.github.nomisrev.api

import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.PrivateUser
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.PublicUser
import io.github.nomisrev.model.SimpleUser
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ProjectsV2
import io.github.nomisrev.model.ProjectsV2View
import io.github.nomisrev.model.ValidationError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import io.github.nomisrev.model.ProjectsV2ItemWithContent
import io.github.nomisrev.model.ProjectsV2FieldSingleSelectOption
import io.github.nomisrev.model.ProjectsV2FieldIterationConfiguration
import io.github.nomisrev.model.ProjectsV2Field
import io.github.nomisrev.model.ProjectsV2ItemSimple
import io.github.nomisrev.model.EmptyObject
import io.github.nomisrev.model.Package
import io.github.nomisrev.model.Event
import io.github.nomisrev.model.BaseGist
import kotlinx.datetime.LocalDateTime
import io.github.nomisrev.model.GpgKey
import io.github.nomisrev.model.Hovercard
import io.github.nomisrev.model.Installation
import io.github.nomisrev.model.KeySimple
import io.github.nomisrev.model.OrganizationSimple
import io.github.nomisrev.model.PackageVersion
import io.github.nomisrev.model.MinimalRepository
import io.github.nomisrev.model.BillingPremiumRequestUsageReportUser
import io.github.nomisrev.model.BillingUsageReportUser
import io.github.nomisrev.model.BillingUsageSummaryReportUser
import io.github.nomisrev.model.SocialAccount
import io.github.nomisrev.model.SshSigningKey
import io.github.nomisrev.model.StarredRepository
import io.github.nomisrev.model.Repository
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
import io.ktor.http.contentType

interface Users {
    val projectsV2: Users.ProjectsV2

    val attestations: Users.Attestations

    val docker: Users.Docker

    val events: Users.Events

    val followers: Users.Followers

    val following: Users.Following

    val gists: Users.Gists

    val gpgKeys: Users.GpgKeys

    val hovercard: Users.Hovercard

    val installation: Users.Installation

    val keys: Users.Keys

    val orgs: Users.Orgs

    val packages: Users.Packages

    val receivedEvents: Users.ReceivedEvents

    val repos: Users.Repos

    val settings: Users.Settings

    val socialAccounts: Users.SocialAccounts

    val sshSigningKeys: Users.SshSigningKeys

    val starred: Users.Starred

    val subscriptions: Users.Subscriptions

    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("user_view_type")
    @Serializable
    sealed interface UsersGetByUsernameResponse {
        @SerialName("private")
        @Serializable
        @JvmInline
        value class Private(val value: PrivateUser) : UsersGetByUsernameResponse

        @SerialName("public")
        @Serializable
        @JvmInline
        value class Public(val value: PublicUser) : UsersGetByUsernameResponse
    }

    sealed interface UsersListResult {
        data class OK(val value: List<SimpleUser>) : UsersListResult

        data object NotModified : UsersListResult
    }

    suspend fun usersList(
        perPage: Long = 30L,
        since: Long? = null,
    ): UsersListResult

    sealed interface UsersGetByUsernameResult {
        data class OK(val value: UsersGetByUsernameResponse) : UsersGetByUsernameResult

        data class NotFound(val value: BasicError) : UsersGetByUsernameResult
    }

    suspend fun usersGetByUsername(
        username: String,
    ): UsersGetByUsernameResult

    interface ProjectsV2 {
        val views: Users.ProjectsV2.Views

        val fields: Users.ProjectsV2.Fields

        val items: Users.ProjectsV2.Items

        sealed interface ProjectsListForUserResult {
            data class OK(val value: List<ProjectsV2>) : ProjectsListForUserResult

            data object NotModified : ProjectsListForUserResult

            data class Unauthorized(val value: BasicError) : ProjectsListForUserResult

            data class Forbidden(val value: BasicError) : ProjectsListForUserResult
        }

        suspend fun projectsListForUser(
            username: String,
            perPage: Long = 30L,
            after: String? = null,
            before: String? = null,
            q: String? = null,
        ): ProjectsListForUserResult

        sealed interface ProjectsGetForUserResult {
            data class OK(val value: ProjectsV2) : ProjectsGetForUserResult

            data object NotModified : ProjectsGetForUserResult

            data class Unauthorized(val value: BasicError) : ProjectsGetForUserResult

            data class Forbidden(val value: BasicError) : ProjectsGetForUserResult
        }

        suspend fun projectsGetForUser(
            username: String,
            projectNumber: Long,
        ): ProjectsGetForUserResult

        interface Views {
            val items: Users.ProjectsV2.Views.ItemsApi

            @Serializable
            data class ProjectsCreateViewForUserBody(
                val name: String,
                val layout: Layout,
                val filter: String? = null,
                @SerialName("visible_fields") val visibleFields: List<Long>? = null,
            ) {
                @Serializable
                enum class Layout {
                    @SerialName("table") Table, @SerialName("board") Board, @SerialName("roadmap") Roadmap;
                }
            }

            sealed interface ProjectsCreateViewForUserResult {
                data class Created(val value: ProjectsV2View) : ProjectsCreateViewForUserResult

                data object NotModified : ProjectsCreateViewForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsCreateViewForUserResult

                data class Forbidden(val value: BasicError) : ProjectsCreateViewForUserResult

                data class NotFound(val value: BasicError) : ProjectsCreateViewForUserResult

                data class UnprocessableEntity(val value: ValidationError) : ProjectsCreateViewForUserResult

                data class ServiceUnavailable(val value: BasicError) : ProjectsCreateViewForUserResult
            }

            suspend fun projectsCreateViewForUser(
                userId: String,
                projectNumber: Long,
                body: ProjectsCreateViewForUserBody,
            ): ProjectsCreateViewForUserResult

            interface ItemsApi {
                @Serializable(with = ProjectsListViewItemsForUserFields.Serializer::class)
                sealed interface ProjectsListViewItemsForUserFields {
                    @Serializable
                    @JvmInline
                    value class CaseString(val value: String) : ProjectsListViewItemsForUserFields

                    @Serializable
                    @JvmInline
                    value class CaseStrings(val value: List<String>) : ProjectsListViewItemsForUserFields

                    object Serializer : KSerializer<ProjectsListViewItemsForUserFields> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.api.Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserFields", PolymorphicKind.SEALED) {
                                element("CaseString", String.serializer().descriptor)
                                element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                            }

                        override fun deserialize(decoder: Decoder): ProjectsListViewItemsForUserFields {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: ProjectsListViewItemsForUserFields) = when(value) {
                            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                        }
                    }
                }

                sealed interface ProjectsListViewItemsForUserResult {
                    data class OK(val value: List<ProjectsV2ItemWithContent>) : ProjectsListViewItemsForUserResult

                    data object NotModified : ProjectsListViewItemsForUserResult

                    data class Unauthorized(val value: BasicError) : ProjectsListViewItemsForUserResult

                    data class Forbidden(val value: BasicError) : ProjectsListViewItemsForUserResult

                    data class NotFound(val value: BasicError) : ProjectsListViewItemsForUserResult
                }

                suspend fun projectsListViewItemsForUser(
                    username: String,
                    projectNumber: Long,
                    viewNumber: Long,
                    perPage: Long = 30L,
                    after: String? = null,
                    before: String? = null,
                    fields: ProjectsListViewItemsForUserFields? = null,
                ): ProjectsListViewItemsForUserResult
            }
        }

        interface Fields {
            @Serializable(with = ProjectsAddFieldForUserBody.Serializer::class)
            sealed interface ProjectsAddFieldForUserBody {
                @Serializable
                data class NameAndDataType(
                    val name: String,
                    @SerialName("data_type") val dataType: DataType,
                ) : ProjectsAddFieldForUserBody {
                    @Serializable
                    enum class DataType {
                        @SerialName("text") Text, @SerialName("number") Number, @SerialName("date") Date;
                    }
                }

                @Serializable
                data class NameAndDataTypeAndSingleSelectOptions(
                    val name: String,
                    @SerialName("data_type") val dataType: DataType,
                    @SerialName("single_select_options") val singleSelectOptions: List<ProjectsV2FieldSingleSelectOption>,
                ) : ProjectsAddFieldForUserBody {
                    @Serializable
                    enum class DataType {
                        @SerialName("single_select") SingleSelect;
                    }
                }

                @Serializable
                data class NameAndDataTypeAndIterationConfiguration(
                    val name: String,
                    @SerialName("data_type") val dataType: DataType,
                    @SerialName("iteration_configuration") val iterationConfiguration: ProjectsV2FieldIterationConfiguration,
                ) : ProjectsAddFieldForUserBody {
                    @Serializable
                    enum class DataType {
                        @SerialName("iteration") Iteration;
                    }
                }

                object Serializer : KSerializer<ProjectsAddFieldForUserBody> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Users.ProjectsV2.Fields.ProjectsAddFieldForUserBody", PolymorphicKind.SEALED) {
                            element("NameAndDataType", ProjectsAddFieldForUserBody.NameAndDataType.serializer().descriptor)
                            element("NameAndDataTypeAndSingleSelectOptions", ProjectsAddFieldForUserBody.NameAndDataTypeAndSingleSelectOptions.serializer().descriptor)
                            element("NameAndDataTypeAndIterationConfiguration", ProjectsAddFieldForUserBody.NameAndDataTypeAndIterationConfiguration.serializer().descriptor)
                        }

                    override fun deserialize(decoder: Decoder): ProjectsAddFieldForUserBody {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            NameAndDataTypeAndSingleSelectOptions::class to { decodeFromJsonElement(ProjectsAddFieldForUserBody.NameAndDataTypeAndSingleSelectOptions.serializer(), it) },
                            NameAndDataTypeAndIterationConfiguration::class to { decodeFromJsonElement(ProjectsAddFieldForUserBody.NameAndDataTypeAndIterationConfiguration.serializer(), it) },
                            NameAndDataType::class to { decodeFromJsonElement(ProjectsAddFieldForUserBody.NameAndDataType.serializer(), it) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: ProjectsAddFieldForUserBody) = when(value) {
                        is NameAndDataType -> encoder.encodeSerializableValue(ProjectsAddFieldForUserBody.NameAndDataType.serializer(), value)
                        is NameAndDataTypeAndSingleSelectOptions -> encoder.encodeSerializableValue(ProjectsAddFieldForUserBody.NameAndDataTypeAndSingleSelectOptions.serializer(), value)
                        is NameAndDataTypeAndIterationConfiguration -> encoder.encodeSerializableValue(ProjectsAddFieldForUserBody.NameAndDataTypeAndIterationConfiguration.serializer(), value)
                    }
                }
            }

            sealed interface ProjectsListFieldsForUserResult {
                data class OK(val value: List<ProjectsV2Field>) : ProjectsListFieldsForUserResult

                data object NotModified : ProjectsListFieldsForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsListFieldsForUserResult

                data class Forbidden(val value: BasicError) : ProjectsListFieldsForUserResult
            }

            suspend fun projectsListFieldsForUser(
                username: String,
                projectNumber: Long,
                perPage: Long = 30L,
                after: String? = null,
                before: String? = null,
            ): ProjectsListFieldsForUserResult

            sealed interface ProjectsAddFieldForUserResult {
                data class Created(val value: ProjectsV2Field) : ProjectsAddFieldForUserResult

                data object NotModified : ProjectsAddFieldForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsAddFieldForUserResult

                data class Forbidden(val value: BasicError) : ProjectsAddFieldForUserResult

                data class UnprocessableEntity(val value: ValidationError) : ProjectsAddFieldForUserResult
            }

            suspend fun projectsAddFieldForUser(
                username: String,
                projectNumber: Long,
                body: ProjectsAddFieldForUserBody,
            ): ProjectsAddFieldForUserResult

            sealed interface ProjectsGetFieldForUserResult {
                data class OK(val value: ProjectsV2Field) : ProjectsGetFieldForUserResult

                data object NotModified : ProjectsGetFieldForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsGetFieldForUserResult

                data class Forbidden(val value: BasicError) : ProjectsGetFieldForUserResult
            }

            suspend fun projectsGetFieldForUser(
                username: String,
                projectNumber: Long,
                fieldId: Long,
            ): ProjectsGetFieldForUserResult
        }

        interface Items {
            @Serializable
            data class ProjectsAddItemForUserBody(
                val type: Type,
                val id: Long? = null,
                val owner: String? = null,
                val repo: String? = null,
                val number: Long? = null,
            ) {
                @Serializable
                enum class Type {
                    Issue, PullRequest;
                }
            }


            @Serializable(with = ProjectsGetUserItemFields.Serializer::class)
            sealed interface ProjectsGetUserItemFields {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : ProjectsGetUserItemFields

                @Serializable
                @JvmInline
                value class CaseStrings(val value: List<String>) : ProjectsGetUserItemFields

                object Serializer : KSerializer<ProjectsGetUserItemFields> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Users.ProjectsV2.Items.ProjectsGetUserItemFields", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): ProjectsGetUserItemFields {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: ProjectsGetUserItemFields) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    }
                }
            }


            @Serializable(with = ProjectsListItemsForUserFields.Serializer::class)
            sealed interface ProjectsListItemsForUserFields {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : ProjectsListItemsForUserFields

                @Serializable
                @JvmInline
                value class CaseStrings(val value: List<String>) : ProjectsListItemsForUserFields

                object Serializer : KSerializer<ProjectsListItemsForUserFields> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Users.ProjectsV2.Items.ProjectsListItemsForUserFields", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): ProjectsListItemsForUserFields {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: ProjectsListItemsForUserFields) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
                    }
                }
            }


            @Serializable
            @JvmInline
            value class ProjectsUpdateItemForUserBody(val fields: List<Fields>) {
                @Serializable
                data class Fields(val id: Long, val value: Value?) {
                    @Serializable(with = Value.Serializer::class)
                    sealed interface Value {
                        @Serializable
                        @JvmInline
                        value class CaseString(val value: String) : Value

                        @Serializable
                        @JvmInline
                        value class CaseDouble(val value: Double) : Value

                        object Serializer : KSerializer<Value> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Users.ProjectsV2.Items.ProjectsUpdateItemForUserBody.Fields.Value", PolymorphicKind.SEALED) {
                                    element("CaseString", String.serializer().descriptor)
                                    element("CaseDouble", Double.serializer().descriptor)
                                }

                            override fun deserialize(decoder: Decoder): Value {
                                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                                return json.attemptDeserialize(
                                    value,
                                    CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                                )
                            }

                            override fun serialize(encoder: Encoder, value: Value) = when(value) {
                                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                            }
                        }
                    }
                }
            }

            sealed interface ProjectsListItemsForUserResult {
                data class OK(val value: List<ProjectsV2ItemWithContent>) : ProjectsListItemsForUserResult

                data object NotModified : ProjectsListItemsForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsListItemsForUserResult

                data class Forbidden(val value: BasicError) : ProjectsListItemsForUserResult
            }

            suspend fun projectsListItemsForUser(
                username: String,
                projectNumber: Long,
                perPage: Long = 30L,
                after: String? = null,
                before: String? = null,
                fields: ProjectsListItemsForUserFields? = null,
                q: String? = null,
            ): ProjectsListItemsForUserResult

            sealed interface ProjectsAddItemForUserResult {
                data class Created(val value: ProjectsV2ItemSimple) : ProjectsAddItemForUserResult

                data object NotModified : ProjectsAddItemForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsAddItemForUserResult

                data class Forbidden(val value: BasicError) : ProjectsAddItemForUserResult
            }

            suspend fun projectsAddItemForUser(
                username: String,
                projectNumber: Long,
                body: ProjectsAddItemForUserBody,
            ): ProjectsAddItemForUserResult

            sealed interface ProjectsGetUserItemResult {
                data class OK(val value: ProjectsV2ItemWithContent) : ProjectsGetUserItemResult

                data object NotModified : ProjectsGetUserItemResult

                data class Unauthorized(val value: BasicError) : ProjectsGetUserItemResult

                data class Forbidden(val value: BasicError) : ProjectsGetUserItemResult
            }

            suspend fun projectsGetUserItem(
                username: String,
                projectNumber: Long,
                itemId: Long,
                fields: ProjectsGetUserItemFields? = null,
            ): ProjectsGetUserItemResult

            sealed interface ProjectsDeleteItemForUserResult {
                data object NoContent : ProjectsDeleteItemForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsDeleteItemForUserResult

                data class Forbidden(val value: BasicError) : ProjectsDeleteItemForUserResult
            }

            suspend fun projectsDeleteItemForUser(
                username: String,
                projectNumber: Long,
                itemId: Long,
            ): ProjectsDeleteItemForUserResult

            sealed interface ProjectsUpdateItemForUserResult {
                data class OK(val value: ProjectsV2ItemWithContent) : ProjectsUpdateItemForUserResult

                data class Unauthorized(val value: BasicError) : ProjectsUpdateItemForUserResult

                data class Forbidden(val value: BasicError) : ProjectsUpdateItemForUserResult

                data class NotFound(val value: BasicError) : ProjectsUpdateItemForUserResult

                data class UnprocessableEntity(val value: ValidationError) : ProjectsUpdateItemForUserResult
            }

            suspend fun projectsUpdateItemForUser(
                username: String,
                projectNumber: Long,
                itemId: Long,
                body: ProjectsUpdateItemForUserBody,
            ): ProjectsUpdateItemForUserResult
        }
    }

    interface Attestations {
        val bulkList: Users.Attestations.BulkList

        val deleteRequest: Users.Attestations.DeleteRequest

        val digest: Users.Attestations.Digest

        @Serializable
        @JvmInline
        value class UsersListAttestationsResponse(val attestations: List<Attestations>? = null) {
            @Serializable
            data class Attestations(
                val bundle: Bundle? = null,
                @SerialName("repository_id") val repositoryId: Long? = null,
                @SerialName("bundle_url") val bundleUrl: String? = null,
                val initiator: String? = null,
            ) {
                @Serializable
                data class Bundle(
                    val mediaType: String? = null,
                    val verificationMaterial: JsonElement? = null,
                    val dsseEnvelope: JsonElement? = null,
                )
            }
        }

        sealed interface UsersDeleteAttestationsByIdResult {
            data object OK : UsersDeleteAttestationsByIdResult

            data object NoContent : UsersDeleteAttestationsByIdResult

            data class Forbidden(val value: BasicError) : UsersDeleteAttestationsByIdResult

            data class NotFound(val value: BasicError) : UsersDeleteAttestationsByIdResult
        }

        suspend fun usersDeleteAttestationsById(
            username: String,
            attestationId: Long,
        ): UsersDeleteAttestationsByIdResult

        sealed interface UsersListAttestationsResult {
            data class OK(val value: UsersListAttestationsResponse) : UsersListAttestationsResult

            data class Created(val value: EmptyObject) : UsersListAttestationsResult

            data object NoContent : UsersListAttestationsResult

            data class NotFound(val value: BasicError) : UsersListAttestationsResult
        }

        suspend fun usersListAttestations(
            username: String,
            subjectDigest: String,
            perPage: Long = 30L,
            after: String? = null,
            before: String? = null,
            predicateType: String? = null,
        ): UsersListAttestationsResult

        interface BulkList {
            @Serializable
            data class UsersListAttestationsBulkBody(
                @SerialName("subject_digests") val subjectDigests: List<String>,
                @SerialName("predicate_type") val predicateType: String? = null,
            )


            @Serializable
            data class UsersListAttestationsBulkResponse(
                @SerialName("attestations_subject_digests") val attestationsSubjectDigests: List<List<AttestationsSubjectDigests>>? = null,
                @SerialName("page_info") val pageInfo: PageInfo? = null,
            ) {
                @Serializable
                data class AttestationsSubjectDigests(
                    val bundle: Bundle? = null,
                    @SerialName("repository_id") val repositoryId: Long? = null,
                    @SerialName("bundle_url") val bundleUrl: String? = null,
                ) {
                    @Serializable
                    data class Bundle(
                        val mediaType: String? = null,
                        val verificationMaterial: JsonElement? = null,
                        val dsseEnvelope: JsonElement? = null,
                    )
                }

                @Serializable
                data class PageInfo(
                    @SerialName("has_next") val hasNext: Boolean? = null,
                    @SerialName("has_previous") val hasPrevious: Boolean? = null,
                    val next: String? = null,
                    val previous: String? = null,
                )
            }

            suspend fun usersListAttestationsBulk(
                username: String,
                perPage: Long = 30L,
                body: UsersListAttestationsBulkBody,
                after: String? = null,
                before: String? = null,
            ): UsersListAttestationsBulkResponse
        }

        interface DeleteRequest {
            sealed interface UsersDeleteAttestationsBulkResult {
                data object OK : UsersDeleteAttestationsBulkResult

                data class NotFound(val value: BasicError) : UsersDeleteAttestationsBulkResult
            }

            suspend fun usersDeleteAttestationsBulk(
                username: String,
                body: JsonElement,
            ): UsersDeleteAttestationsBulkResult
        }

        interface Digest {
            sealed interface UsersDeleteAttestationsBySubjectDigestResult {
                data object OK : UsersDeleteAttestationsBySubjectDigestResult

                data object NoContent : UsersDeleteAttestationsBySubjectDigestResult

                data class NotFound(val value: BasicError) : UsersDeleteAttestationsBySubjectDigestResult
            }

            suspend fun usersDeleteAttestationsBySubjectDigest(
                username: String,
                subjectDigest: String,
            ): UsersDeleteAttestationsBySubjectDigestResult
        }
    }

    interface Docker {
        val conflicts: Users.Docker.Conflicts

        interface Conflicts {
            sealed interface PackagesListDockerMigrationConflictingPackagesForUserResult {
                data class OK(val value: List<Package>) : PackagesListDockerMigrationConflictingPackagesForUserResult

                data class Unauthorized(val value: BasicError) : PackagesListDockerMigrationConflictingPackagesForUserResult

                data class Forbidden(val value: BasicError) : PackagesListDockerMigrationConflictingPackagesForUserResult
            }

            suspend fun packagesListDockerMigrationConflictingPackagesForUser(
                username: String,
            ): PackagesListDockerMigrationConflictingPackagesForUserResult
        }
    }

    interface Events {
        val orgs: Users.Events.OrgsApi

        val public: Users.Events.Public

        suspend fun activityListEventsForAuthenticatedUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<Event>

        interface OrgsApi {
            suspend fun activityListOrgEventsForAuthenticatedUser(
                username: String,
                org: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<Event>
        }

        interface Public {
            suspend fun activityListPublicEventsForUser(
                username: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<Event>
        }
    }

    interface Followers {
        suspend fun usersListFollowersForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleUser>
    }

    interface Following {
        suspend fun usersListFollowingForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SimpleUser>

        sealed interface UsersCheckFollowingForUserResult {
            data object NoContent : UsersCheckFollowingForUserResult

            data object NotFound : UsersCheckFollowingForUserResult
        }

        suspend fun usersCheckFollowingForUser(
            username: String,
            targetUser: String,
        ): UsersCheckFollowingForUserResult
    }

    interface Gists {
        sealed interface GistsListForUserResult {
            data class OK(val value: List<BaseGist>) : GistsListForUserResult

            data class UnprocessableEntity(val value: ValidationError) : GistsListForUserResult
        }

        suspend fun gistsListForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
            since: LocalDateTime? = null,
        ): GistsListForUserResult
    }

    interface GpgKeys {
        suspend fun usersListGpgKeysForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<GpgKey>
    }

    interface Hovercard {
        @Serializable
        enum class SubjectType {
            @SerialName("organization")
            Organization,
            @SerialName("repository")
            Repository,
            @SerialName("issue")
            Issue,
            @SerialName("pull_request")
            PullRequest;
        }

        sealed interface UsersGetContextForUserResult {
            data class OK(val value: Hovercard) : UsersGetContextForUserResult

            data class NotFound(val value: BasicError) : UsersGetContextForUserResult

            data class UnprocessableEntity(val value: ValidationError) : UsersGetContextForUserResult
        }

        suspend fun usersGetContextForUser(
            username: String,
            subjectId: String? = null,
            subjectType: SubjectType? = null,
        ): UsersGetContextForUserResult
    }

    interface Installation {
        suspend fun appsGetUserInstallation(
            username: String,
        ): Installation
    }

    interface Keys {
        suspend fun usersListPublicKeysForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<KeySimple>
    }

    interface Orgs {
        suspend fun orgsListForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<OrganizationSimple>
    }

    interface Packages {
        val restore: Users.Packages.Restore

        val versions: Users.Packages.Versions

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
        enum class PackagesGetPackageForUserPackageType {
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
        enum class PackagesListPackagesForUserPackageType {
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

        sealed interface PackagesListPackagesForUserResult {
            data class OK(val value: List<Package>) : PackagesListPackagesForUserResult

            data object BadRequest : PackagesListPackagesForUserResult

            data class Unauthorized(val value: BasicError) : PackagesListPackagesForUserResult

            data class Forbidden(val value: BasicError) : PackagesListPackagesForUserResult
        }

        suspend fun packagesListPackagesForUser(
            username: String,
            packageType: PackagesListPackagesForUserPackageType,
            page: Long = 1L,
            perPage: Long = 30L,
            visibility: Visibility? = null,
        ): PackagesListPackagesForUserResult

        suspend fun packagesGetPackageForUser(
            username: String,
            packageType: PackagesGetPackageForUserPackageType,
            packageName: String,
        ): Package

        sealed interface PackagesDeletePackageForUserResult {
            data object NoContent : PackagesDeletePackageForUserResult

            data class Unauthorized(val value: BasicError) : PackagesDeletePackageForUserResult

            data class Forbidden(val value: BasicError) : PackagesDeletePackageForUserResult

            data class NotFound(val value: BasicError) : PackagesDeletePackageForUserResult
        }

        suspend fun packagesDeletePackageForUser(
            username: String,
            packageType: PackageType,
            packageName: String,
        ): PackagesDeletePackageForUserResult

        interface Restore {
            @Serializable
            enum class PackagesRestorePackageForUserPackageType {
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

            sealed interface PackagesRestorePackageForUserResult {
                data object NoContent : PackagesRestorePackageForUserResult

                data class Unauthorized(val value: BasicError) : PackagesRestorePackageForUserResult

                data class Forbidden(val value: BasicError) : PackagesRestorePackageForUserResult

                data class NotFound(val value: BasicError) : PackagesRestorePackageForUserResult
            }

            suspend fun packagesRestorePackageForUser(
                username: String,
                packageType: PackagesRestorePackageForUserPackageType,
                packageName: String,
                token: String? = null,
            ): PackagesRestorePackageForUserResult
        }

        interface Versions {
            val restore: Users.Packages.Versions.RestoreApi

            @Serializable
            enum class PackagesDeletePackageVersionForUserPackageType {
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
            enum class PackagesGetAllPackageVersionsForPackageOwnedByUserPackageType {
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
            enum class PackagesGetPackageVersionForUserPackageType {
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

            sealed interface PackagesGetAllPackageVersionsForPackageOwnedByUserResult {
                data class OK(val value: List<PackageVersion>) : PackagesGetAllPackageVersionsForPackageOwnedByUserResult

                data class Unauthorized(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByUserResult

                data class Forbidden(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByUserResult

                data class NotFound(val value: BasicError) : PackagesGetAllPackageVersionsForPackageOwnedByUserResult
            }

            suspend fun packagesGetAllPackageVersionsForPackageOwnedByUser(
                username: String,
                packageType: PackagesGetAllPackageVersionsForPackageOwnedByUserPackageType,
                packageName: String,
            ): PackagesGetAllPackageVersionsForPackageOwnedByUserResult

            suspend fun packagesGetPackageVersionForUser(
                username: String,
                packageType: PackagesGetPackageVersionForUserPackageType,
                packageName: String,
                packageVersionId: Long,
            ): PackageVersion

            sealed interface PackagesDeletePackageVersionForUserResult {
                data object NoContent : PackagesDeletePackageVersionForUserResult

                data class Unauthorized(val value: BasicError) : PackagesDeletePackageVersionForUserResult

                data class Forbidden(val value: BasicError) : PackagesDeletePackageVersionForUserResult

                data class NotFound(val value: BasicError) : PackagesDeletePackageVersionForUserResult
            }

            suspend fun packagesDeletePackageVersionForUser(
                username: String,
                packageType: PackagesDeletePackageVersionForUserPackageType,
                packageName: String,
                packageVersionId: Long,
            ): PackagesDeletePackageVersionForUserResult

            interface RestoreApi {
                @Serializable
                enum class PackagesRestorePackageVersionForUserPackageType {
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

                sealed interface PackagesRestorePackageVersionForUserResult {
                    data object NoContent : PackagesRestorePackageVersionForUserResult

                    data class Unauthorized(val value: BasicError) : PackagesRestorePackageVersionForUserResult

                    data class Forbidden(val value: BasicError) : PackagesRestorePackageVersionForUserResult

                    data class NotFound(val value: BasicError) : PackagesRestorePackageVersionForUserResult
                }

                suspend fun packagesRestorePackageVersionForUser(
                    username: String,
                    packageType: PackagesRestorePackageVersionForUserPackageType,
                    packageName: String,
                    packageVersionId: Long,
                ): PackagesRestorePackageVersionForUserResult
            }
        }
    }

    interface ReceivedEvents {
        val public: Users.ReceivedEvents.Public

        suspend fun activityListReceivedEventsForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<Event>

        interface Public {
            suspend fun activityListReceivedPublicEventsForUser(
                username: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<Event>
        }
    }

    interface Repos {
        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
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
            @SerialName("all") All, @SerialName("owner") Owner, @SerialName("member") Member;
        }

        suspend fun reposListForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.FullName,
            type: Type = Type.Owner,
            direction: Direction? = null,
        ): List<MinimalRepository>
    }

    interface Settings {
        val billing: Users.Settings.Billing

        interface Billing {
            val premiumRequest: Users.Settings.Billing.PremiumRequest

            val usage: Users.Settings.Billing.Usage

            interface PremiumRequest {
                val usage: Users.Settings.Billing.PremiumRequest.UsageApi

                interface UsageApi {
                    @Serializable
                    data class BillingGetGithubBillingPremiumRequestUsageReportUserResponse(
                        val code: String? = null,
                        val message: String? = null,
                        @SerialName("documentation_url") val documentationUrl: String? = null,
                    )

                    sealed interface BillingGetGithubBillingPremiumRequestUsageReportUserResult {
                        data class OK(val value: BillingPremiumRequestUsageReportUser) : BillingGetGithubBillingPremiumRequestUsageReportUserResult

                        data class BadRequest(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportUserResult

                        data class Forbidden(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportUserResult

                        data class NotFound(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportUserResult

                        data class InternalServerError(val value: BasicError) : BillingGetGithubBillingPremiumRequestUsageReportUserResult

                        data class ServiceUnavailable(val value: BillingGetGithubBillingPremiumRequestUsageReportUserResponse) : BillingGetGithubBillingPremiumRequestUsageReportUserResult
                    }

                    suspend fun billingGetGithubBillingPremiumRequestUsageReportUser(
                        username: String,
                        day: Long? = null,
                        model: String? = null,
                        month: Long? = null,
                        product: String? = null,
                        year: Long? = null,
                    ): BillingGetGithubBillingPremiumRequestUsageReportUserResult
                }
            }

            interface Usage {
                val summary: Users.Settings.Billing.Usage.Summary

                @Serializable
                data class BillingGetGithubBillingUsageReportUserResponse(
                    val code: String? = null,
                    val message: String? = null,
                    @SerialName("documentation_url") val documentationUrl: String? = null,
                )

                sealed interface BillingGetGithubBillingUsageReportUserResult {
                    data class OK(val value: BillingUsageReportUser) : BillingGetGithubBillingUsageReportUserResult

                    data class BadRequest(val value: BasicError) : BillingGetGithubBillingUsageReportUserResult

                    data class Forbidden(val value: BasicError) : BillingGetGithubBillingUsageReportUserResult

                    data class InternalServerError(val value: BasicError) : BillingGetGithubBillingUsageReportUserResult

                    data class ServiceUnavailable(val value: BillingGetGithubBillingUsageReportUserResponse) : BillingGetGithubBillingUsageReportUserResult
                }

                suspend fun billingGetGithubBillingUsageReportUser(
                    username: String,
                    day: Long? = null,
                    month: Long? = null,
                    year: Long? = null,
                ): BillingGetGithubBillingUsageReportUserResult

                interface Summary {
                    @Serializable
                    data class BillingGetGithubBillingUsageSummaryReportUserResponse(
                        val code: String? = null,
                        val message: String? = null,
                        @SerialName("documentation_url") val documentationUrl: String? = null,
                    )

                    sealed interface BillingGetGithubBillingUsageSummaryReportUserResult {
                        data class OK(val value: BillingUsageSummaryReportUser) : BillingGetGithubBillingUsageSummaryReportUserResult

                        data class BadRequest(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportUserResult

                        data class Forbidden(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportUserResult

                        data class NotFound(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportUserResult

                        data class InternalServerError(val value: BasicError) : BillingGetGithubBillingUsageSummaryReportUserResult

                        data class ServiceUnavailable(val value: BillingGetGithubBillingUsageSummaryReportUserResponse) : BillingGetGithubBillingUsageSummaryReportUserResult
                    }

                    suspend fun billingGetGithubBillingUsageSummaryReportUser(
                        username: String,
                        day: Long? = null,
                        month: Long? = null,
                        product: String? = null,
                        repository: String? = null,
                        sku: String? = null,
                        year: Long? = null,
                    ): BillingGetGithubBillingUsageSummaryReportUserResult
                }
            }
        }
    }

    interface SocialAccounts {
        suspend fun usersListSocialAccountsForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SocialAccount>
    }

    interface SshSigningKeys {
        suspend fun usersListSshSigningKeysForUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<SshSigningKey>
    }

    interface Starred {
        @Serializable(with = ActivityListReposStarredByUserResponse.Serializer::class)
        sealed interface ActivityListReposStarredByUserResponse {
            @Serializable
            @JvmInline
            value class CaseStarredRepositorys(val value: List<StarredRepository>) : ActivityListReposStarredByUserResponse

            @Serializable
            @JvmInline
            value class CaseRepositorys(val value: List<Repository>) : ActivityListReposStarredByUserResponse

            object Serializer : KSerializer<ActivityListReposStarredByUserResponse> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.Users.Starred.ActivityListReposStarredByUserResponse", PolymorphicKind.SEALED) {
                        element("CaseStarredRepositorys", ListSerializer(StarredRepository.serializer()).descriptor)
                        element("CaseRepositorys", ListSerializer(Repository.serializer()).descriptor)
                    }

                override fun deserialize(decoder: Decoder): ActivityListReposStarredByUserResponse {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        CaseStarredRepositorys::class to { CaseStarredRepositorys(decodeFromJsonElement(ListSerializer(StarredRepository.serializer()), it)) },
                        CaseRepositorys::class to { CaseRepositorys(decodeFromJsonElement(ListSerializer(Repository.serializer()), it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: ActivityListReposStarredByUserResponse) = when(value) {
                    is CaseStarredRepositorys -> encoder.encodeSerializableValue(ListSerializer(StarredRepository.serializer()), value.value)
                    is CaseRepositorys -> encoder.encodeSerializableValue(ListSerializer(Repository.serializer()), value.value)
                }
            }
        }


        @Serializable
        enum class Direction {
            @SerialName("asc") Asc, @SerialName("desc") Desc;
        }


        @Serializable
        enum class Sort {
            @SerialName("created") Created, @SerialName("updated") Updated;
        }

        suspend fun activityListReposStarredByUser(
            username: String,
            direction: Direction = Direction.Desc,
            page: Long = 1L,
            perPage: Long = 30L,
            sort: Sort = Sort.Created,
        ): ActivityListReposStarredByUserResponse
    }

    interface Subscriptions {
        suspend fun activityListReposWatchedByUser(
            username: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): List<MinimalRepository>
    }
}

internal class KtorUsers(private val client: HttpClient) : Users {
    override val projectsV2: Users.ProjectsV2 = KtorUsersProjectsV2(client)

    override val attestations: Users.Attestations = KtorUsersAttestations(client)

    override val docker: Users.Docker = KtorUsersDocker(client)

    override val events: Users.Events = KtorUsersEvents(client)

    override val followers: Users.Followers = KtorUsersFollowers(client)

    override val following: Users.Following = KtorUsersFollowing(client)

    override val gists: Users.Gists = KtorUsersGists(client)

    override val gpgKeys: Users.GpgKeys = KtorUsersGpgKeys(client)

    override val hovercard: Users.Hovercard = KtorUsersHovercard(client)

    override val installation: Users.Installation = KtorUsersInstallation(client)

    override val keys: Users.Keys = KtorUsersKeys(client)

    override val orgs: Users.Orgs = KtorUsersOrgs(client)

    override val packages: Users.Packages = KtorUsersPackages(client)

    override val receivedEvents: Users.ReceivedEvents = KtorUsersReceivedEvents(client)

    override val repos: Users.Repos = KtorUsersRepos(client)

    override val settings: Users.Settings = KtorUsersSettings(client)

    override val socialAccounts: Users.SocialAccounts = KtorUsersSocialAccounts(client)

    override val sshSigningKeys: Users.SshSigningKeys = KtorUsersSshSigningKeys(client)

    override val starred: Users.Starred = KtorUsersStarred(client)

    override val subscriptions: Users.Subscriptions = KtorUsersSubscriptions(client)

    override suspend fun usersList(perPage: Long, since: Long?): Users.UsersListResult {
        val response = client.get("/users") {
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.UsersListResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.UsersListResult.NotModified
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersGetByUsername(username: String): Users.UsersGetByUsernameResult {
        val response = client.get("/users/$username")
        return when (response.status) {
            HttpStatusCode.OK -> Users.UsersGetByUsernameResult.OK(response.body())
            HttpStatusCode.NotFound -> Users.UsersGetByUsernameResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersProjectsV2(private val client: HttpClient) : Users.ProjectsV2 {
    override val views: Users.ProjectsV2.Views = KtorUsersProjectsV2Views(client)

    override val fields: Users.ProjectsV2.Fields = KtorUsersProjectsV2Fields(client)

    override val items: Users.ProjectsV2.Items = KtorUsersProjectsV2Items(client)

    override suspend fun projectsListForUser(username: String, perPage: Long, after: String?, before: String?, q: String?): Users.ProjectsV2.ProjectsListForUserResult {
        val response = client.get("/users/$username/projectsV2") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            q?.let { parameter("q", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.ProjectsListForUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.ProjectsListForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.ProjectsListForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.ProjectsListForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsGetForUser(username: String, projectNumber: Long): Users.ProjectsV2.ProjectsGetForUserResult {
        val response = client.get("/users/$username/projectsV2/$projectNumber")
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.ProjectsGetForUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.ProjectsGetForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.ProjectsGetForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.ProjectsGetForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersProjectsV2Views(private val client: HttpClient) : Users.ProjectsV2.Views {
    override val items: Users.ProjectsV2.Views.ItemsApi = KtorUsersProjectsV2ViewsItemsApi(client)

    override suspend fun projectsCreateViewForUser(userId: String, projectNumber: Long, body: Users.ProjectsV2.Views.ProjectsCreateViewForUserBody): Users.ProjectsV2.Views.ProjectsCreateViewForUserResult {
        val response = client.post("/users/$userId/projectsV2/$projectNumber/views") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.Created(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Users.ProjectsV2.Views.ProjectsCreateViewForUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersProjectsV2ViewsItemsApi(private val client: HttpClient) : Users.ProjectsV2.Views.ItemsApi {
    override suspend fun projectsListViewItemsForUser(username: String, projectNumber: Long, viewNumber: Long, perPage: Long, after: String?, before: String?, fields: Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserFields?): Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserResult {
        val response = client.get("/users/$username/projectsV2/$projectNumber/views/$viewNumber/items") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            fields?.let { parameter("fields", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.ProjectsV2.Views.ItemsApi.ProjectsListViewItemsForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersProjectsV2Fields(private val client: HttpClient) : Users.ProjectsV2.Fields {
    override suspend fun projectsListFieldsForUser(username: String, projectNumber: Long, perPage: Long, after: String?, before: String?): Users.ProjectsV2.Fields.ProjectsListFieldsForUserResult {
        val response = client.get("/users/$username/projectsV2/$projectNumber/fields") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.Fields.ProjectsListFieldsForUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Fields.ProjectsListFieldsForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Fields.ProjectsListFieldsForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Fields.ProjectsListFieldsForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsAddFieldForUser(username: String, projectNumber: Long, body: Users.ProjectsV2.Fields.ProjectsAddFieldForUserBody): Users.ProjectsV2.Fields.ProjectsAddFieldForUserResult {
        val response = client.post("/users/$username/projectsV2/$projectNumber/fields") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Users.ProjectsV2.Fields.ProjectsAddFieldForUserResult.Created(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Fields.ProjectsAddFieldForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Fields.ProjectsAddFieldForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Fields.ProjectsAddFieldForUserResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Users.ProjectsV2.Fields.ProjectsAddFieldForUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsGetFieldForUser(username: String, projectNumber: Long, fieldId: Long): Users.ProjectsV2.Fields.ProjectsGetFieldForUserResult {
        val response = client.get("/users/$username/projectsV2/$projectNumber/fields/$fieldId")
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.Fields.ProjectsGetFieldForUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Fields.ProjectsGetFieldForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Fields.ProjectsGetFieldForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Fields.ProjectsGetFieldForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersProjectsV2Items(private val client: HttpClient) : Users.ProjectsV2.Items {
    override suspend fun projectsListItemsForUser(username: String, projectNumber: Long, perPage: Long, after: String?, before: String?, fields: Users.ProjectsV2.Items.ProjectsListItemsForUserFields?, q: String?): Users.ProjectsV2.Items.ProjectsListItemsForUserResult {
        val response = client.get("/users/$username/projectsV2/$projectNumber/items") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            fields?.let { parameter("fields", it) }
            q?.let { parameter("q", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.Items.ProjectsListItemsForUserResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Items.ProjectsListItemsForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Items.ProjectsListItemsForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Items.ProjectsListItemsForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsAddItemForUser(username: String, projectNumber: Long, body: Users.ProjectsV2.Items.ProjectsAddItemForUserBody): Users.ProjectsV2.Items.ProjectsAddItemForUserResult {
        val response = client.post("/users/$username/projectsV2/$projectNumber/items") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Users.ProjectsV2.Items.ProjectsAddItemForUserResult.Created(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Items.ProjectsAddItemForUserResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Items.ProjectsAddItemForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Items.ProjectsAddItemForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsGetUserItem(username: String, projectNumber: Long, itemId: Long, fields: Users.ProjectsV2.Items.ProjectsGetUserItemFields?): Users.ProjectsV2.Items.ProjectsGetUserItemResult {
        val response = client.get("/users/$username/projectsV2/$projectNumber/items/$itemId") {
            fields?.let { parameter("fields", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.Items.ProjectsGetUserItemResult.OK(response.body())
            HttpStatusCode.NotModified -> Users.ProjectsV2.Items.ProjectsGetUserItemResult.NotModified
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Items.ProjectsGetUserItemResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Items.ProjectsGetUserItemResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsDeleteItemForUser(username: String, projectNumber: Long, itemId: Long): Users.ProjectsV2.Items.ProjectsDeleteItemForUserResult {
        val response = client.delete("/users/$username/projectsV2/$projectNumber/items/$itemId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Users.ProjectsV2.Items.ProjectsDeleteItemForUserResult.NoContent
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Items.ProjectsDeleteItemForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Items.ProjectsDeleteItemForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun projectsUpdateItemForUser(username: String, projectNumber: Long, itemId: Long, body: Users.ProjectsV2.Items.ProjectsUpdateItemForUserBody): Users.ProjectsV2.Items.ProjectsUpdateItemForUserResult {
        val response = client.patch("/users/$username/projectsV2/$projectNumber/items/$itemId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.ProjectsV2.Items.ProjectsUpdateItemForUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Users.ProjectsV2.Items.ProjectsUpdateItemForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.ProjectsV2.Items.ProjectsUpdateItemForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.ProjectsV2.Items.ProjectsUpdateItemForUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Users.ProjectsV2.Items.ProjectsUpdateItemForUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersAttestations(private val client: HttpClient) : Users.Attestations {
    override val bulkList: Users.Attestations.BulkList = KtorUsersAttestationsBulkList(client)

    override val deleteRequest: Users.Attestations.DeleteRequest = KtorUsersAttestationsDeleteRequest(client)

    override val digest: Users.Attestations.Digest = KtorUsersAttestationsDigest(client)

    override suspend fun usersDeleteAttestationsById(username: String, attestationId: Long): Users.Attestations.UsersDeleteAttestationsByIdResult {
        val response = client.delete("/users/$username/attestations/$attestationId")
        return when (response.status) {
            HttpStatusCode.OK -> Users.Attestations.UsersDeleteAttestationsByIdResult.OK
            HttpStatusCode.NoContent -> Users.Attestations.UsersDeleteAttestationsByIdResult.NoContent
            HttpStatusCode.Forbidden -> Users.Attestations.UsersDeleteAttestationsByIdResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Attestations.UsersDeleteAttestationsByIdResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun usersListAttestations(username: String, subjectDigest: String, perPage: Long, after: String?, before: String?, predicateType: String?): Users.Attestations.UsersListAttestationsResult {
        val response = client.get("/users/$username/attestations/$subjectDigest") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            predicateType?.let { parameter("predicate_type", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Attestations.UsersListAttestationsResult.OK(response.body())
            HttpStatusCode.Created -> Users.Attestations.UsersListAttestationsResult.Created(response.body())
            HttpStatusCode.NoContent -> Users.Attestations.UsersListAttestationsResult.NoContent
            HttpStatusCode.NotFound -> Users.Attestations.UsersListAttestationsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersAttestationsBulkList(private val client: HttpClient) : Users.Attestations.BulkList {
    override suspend fun usersListAttestationsBulk(username: String, perPage: Long, body: Users.Attestations.BulkList.UsersListAttestationsBulkBody, after: String?, before: String?): Users.Attestations.BulkList.UsersListAttestationsBulkResponse =
        client.post("/users/$username/attestations/bulk-list") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorUsersAttestationsDeleteRequest(private val client: HttpClient) : Users.Attestations.DeleteRequest {
    override suspend fun usersDeleteAttestationsBulk(username: String, body: JsonElement): Users.Attestations.DeleteRequest.UsersDeleteAttestationsBulkResult {
        val response = client.post("/users/$username/attestations/delete-request") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Attestations.DeleteRequest.UsersDeleteAttestationsBulkResult.OK
            HttpStatusCode.NotFound -> Users.Attestations.DeleteRequest.UsersDeleteAttestationsBulkResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersAttestationsDigest(private val client: HttpClient) : Users.Attestations.Digest {
    override suspend fun usersDeleteAttestationsBySubjectDigest(username: String, subjectDigest: String): Users.Attestations.Digest.UsersDeleteAttestationsBySubjectDigestResult {
        val response = client.delete("/users/$username/attestations/digest/$subjectDigest")
        return when (response.status) {
            HttpStatusCode.OK -> Users.Attestations.Digest.UsersDeleteAttestationsBySubjectDigestResult.OK
            HttpStatusCode.NoContent -> Users.Attestations.Digest.UsersDeleteAttestationsBySubjectDigestResult.NoContent
            HttpStatusCode.NotFound -> Users.Attestations.Digest.UsersDeleteAttestationsBySubjectDigestResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersDocker(private val client: HttpClient) : Users.Docker {
    override val conflicts: Users.Docker.Conflicts = KtorUsersDockerConflicts(client)
}

internal class KtorUsersDockerConflicts(private val client: HttpClient) : Users.Docker.Conflicts {
    override suspend fun packagesListDockerMigrationConflictingPackagesForUser(username: String): Users.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForUserResult {
        val response = client.get("/users/$username/docker/conflicts")
        return when (response.status) {
            HttpStatusCode.OK -> Users.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Users.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Docker.Conflicts.PackagesListDockerMigrationConflictingPackagesForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersEvents(private val client: HttpClient) : Users.Events {
    override val orgs: Users.Events.OrgsApi = KtorUsersEventsOrgsApi(client)

    override val public: Users.Events.Public = KtorUsersEventsPublic(client)

    override suspend fun activityListEventsForAuthenticatedUser(username: String, page: Long, perPage: Long): List<Event> =
        client.get("/users/$username/events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersEventsOrgsApi(private val client: HttpClient) : Users.Events.OrgsApi {
    override suspend fun activityListOrgEventsForAuthenticatedUser(username: String, org: String, page: Long, perPage: Long): List<Event> =
        client.get("/users/$username/events/orgs/$org") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersEventsPublic(private val client: HttpClient) : Users.Events.Public {
    override suspend fun activityListPublicEventsForUser(username: String, page: Long, perPage: Long): List<Event> =
        client.get("/users/$username/events/public") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersFollowers(private val client: HttpClient) : Users.Followers {
    override suspend fun usersListFollowersForUser(username: String, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/users/$username/followers") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersFollowing(private val client: HttpClient) : Users.Following {
    override suspend fun usersListFollowingForUser(username: String, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/users/$username/following") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun usersCheckFollowingForUser(username: String, targetUser: String): Users.Following.UsersCheckFollowingForUserResult {
        val response = client.get("/users/$username/following/$targetUser")
        return when (response.status) {
            HttpStatusCode.NoContent -> Users.Following.UsersCheckFollowingForUserResult.NoContent
            HttpStatusCode.NotFound -> Users.Following.UsersCheckFollowingForUserResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersGists(private val client: HttpClient) : Users.Gists {
    override suspend fun gistsListForUser(username: String, page: Long, perPage: Long, since: LocalDateTime?): Users.Gists.GistsListForUserResult {
        val response = client.get("/users/$username/gists") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Gists.GistsListForUserResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Users.Gists.GistsListForUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersGpgKeys(private val client: HttpClient) : Users.GpgKeys {
    override suspend fun usersListGpgKeysForUser(username: String, page: Long, perPage: Long): List<GpgKey> =
        client.get("/users/$username/gpg_keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersHovercard(private val client: HttpClient) : Users.Hovercard {
    override suspend fun usersGetContextForUser(username: String, subjectId: String?, subjectType: Users.Hovercard.SubjectType?): Users.Hovercard.UsersGetContextForUserResult {
        val response = client.get("/users/$username/hovercard") {
            subjectId?.let { parameter("subject_id", it) }
            subjectType?.let { parameter("subject_type", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Hovercard.UsersGetContextForUserResult.OK(response.body())
            HttpStatusCode.NotFound -> Users.Hovercard.UsersGetContextForUserResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Users.Hovercard.UsersGetContextForUserResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersInstallation(private val client: HttpClient) : Users.Installation {
    override suspend fun appsGetUserInstallation(username: String): Installation =
        client.get("/users/$username/installation").body()
}

internal class KtorUsersKeys(private val client: HttpClient) : Users.Keys {
    override suspend fun usersListPublicKeysForUser(username: String, page: Long, perPage: Long): List<KeySimple> =
        client.get("/users/$username/keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersOrgs(private val client: HttpClient) : Users.Orgs {
    override suspend fun orgsListForUser(username: String, page: Long, perPage: Long): List<OrganizationSimple> =
        client.get("/users/$username/orgs") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersPackages(private val client: HttpClient) : Users.Packages {
    override val restore: Users.Packages.Restore = KtorUsersPackagesRestore(client)

    override val versions: Users.Packages.Versions = KtorUsersPackagesVersions(client)

    override suspend fun packagesListPackagesForUser(username: String, packageType: Users.Packages.PackagesListPackagesForUserPackageType, page: Long, perPage: Long, visibility: Users.Packages.Visibility?): Users.Packages.PackagesListPackagesForUserResult {
        val response = client.get("/users/$username/packages") {
            parameter("package_type", packageType)
            parameter("page", page)
            parameter("per_page", perPage)
            visibility?.let { parameter("visibility", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Packages.PackagesListPackagesForUserResult.OK(response.body())
            HttpStatusCode.BadRequest -> Users.Packages.PackagesListPackagesForUserResult.BadRequest
            HttpStatusCode.Unauthorized -> Users.Packages.PackagesListPackagesForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Packages.PackagesListPackagesForUserResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun packagesGetPackageForUser(username: String, packageType: Users.Packages.PackagesGetPackageForUserPackageType, packageName: String): Package =
        client.get("/users/$username/packages/$packageType/$packageName").body()

    override suspend fun packagesDeletePackageForUser(username: String, packageType: Users.Packages.PackageType, packageName: String): Users.Packages.PackagesDeletePackageForUserResult {
        val response = client.delete("/users/$username/packages/$packageType/$packageName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Users.Packages.PackagesDeletePackageForUserResult.NoContent
            HttpStatusCode.Unauthorized -> Users.Packages.PackagesDeletePackageForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Packages.PackagesDeletePackageForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Packages.PackagesDeletePackageForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersPackagesRestore(private val client: HttpClient) : Users.Packages.Restore {
    override suspend fun packagesRestorePackageForUser(username: String, packageType: Users.Packages.Restore.PackagesRestorePackageForUserPackageType, packageName: String, token: String?): Users.Packages.Restore.PackagesRestorePackageForUserResult {
        val response = client.post("/users/$username/packages/$packageType/$packageName/restore") {
            token?.let { parameter("token", it) }
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Users.Packages.Restore.PackagesRestorePackageForUserResult.NoContent
            HttpStatusCode.Unauthorized -> Users.Packages.Restore.PackagesRestorePackageForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Packages.Restore.PackagesRestorePackageForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Packages.Restore.PackagesRestorePackageForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersPackagesVersions(private val client: HttpClient) : Users.Packages.Versions {
    override val restore: Users.Packages.Versions.RestoreApi = KtorUsersPackagesVersionsRestoreApi(client)

    override suspend fun packagesGetAllPackageVersionsForPackageOwnedByUser(username: String, packageType: Users.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByUserPackageType, packageName: String): Users.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByUserResult {
        val response = client.get("/users/$username/packages/$packageType/$packageName/versions")
        return when (response.status) {
            HttpStatusCode.OK -> Users.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByUserResult.OK(response.body())
            HttpStatusCode.Unauthorized -> Users.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Packages.Versions.PackagesGetAllPackageVersionsForPackageOwnedByUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun packagesGetPackageVersionForUser(username: String, packageType: Users.Packages.Versions.PackagesGetPackageVersionForUserPackageType, packageName: String, packageVersionId: Long): PackageVersion =
        client.get("/users/$username/packages/$packageType/$packageName/versions/$packageVersionId").body()

    override suspend fun packagesDeletePackageVersionForUser(username: String, packageType: Users.Packages.Versions.PackagesDeletePackageVersionForUserPackageType, packageName: String, packageVersionId: Long): Users.Packages.Versions.PackagesDeletePackageVersionForUserResult {
        val response = client.delete("/users/$username/packages/$packageType/$packageName/versions/$packageVersionId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Users.Packages.Versions.PackagesDeletePackageVersionForUserResult.NoContent
            HttpStatusCode.Unauthorized -> Users.Packages.Versions.PackagesDeletePackageVersionForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Packages.Versions.PackagesDeletePackageVersionForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Packages.Versions.PackagesDeletePackageVersionForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersPackagesVersionsRestoreApi(private val client: HttpClient) : Users.Packages.Versions.RestoreApi {
    override suspend fun packagesRestorePackageVersionForUser(username: String, packageType: Users.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForUserPackageType, packageName: String, packageVersionId: Long): Users.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForUserResult {
        val response = client.post("/users/$username/packages/$packageType/$packageName/versions/$packageVersionId/restore")
        return when (response.status) {
            HttpStatusCode.NoContent -> Users.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForUserResult.NoContent
            HttpStatusCode.Unauthorized -> Users.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForUserResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Users.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Packages.Versions.RestoreApi.PackagesRestorePackageVersionForUserResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersReceivedEvents(private val client: HttpClient) : Users.ReceivedEvents {
    override val public: Users.ReceivedEvents.Public = KtorUsersReceivedEventsPublic(client)

    override suspend fun activityListReceivedEventsForUser(username: String, page: Long, perPage: Long): List<Event> =
        client.get("/users/$username/received_events") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersReceivedEventsPublic(private val client: HttpClient) : Users.ReceivedEvents.Public {
    override suspend fun activityListReceivedPublicEventsForUser(username: String, page: Long, perPage: Long): List<Event> =
        client.get("/users/$username/received_events/public") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersRepos(private val client: HttpClient) : Users.Repos {
    override suspend fun reposListForUser(username: String, page: Long, perPage: Long, sort: Users.Repos.Sort, type: Users.Repos.Type, direction: Users.Repos.Direction?): List<MinimalRepository> =
        client.get("/users/$username/repos") {
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("type", type)
            direction?.let { parameter("direction", it) }
        }.body()
}

internal class KtorUsersSettings(private val client: HttpClient) : Users.Settings {
    override val billing: Users.Settings.Billing = KtorUsersSettingsBilling(client)
}

internal class KtorUsersSettingsBilling(private val client: HttpClient) : Users.Settings.Billing {
    override val premiumRequest: Users.Settings.Billing.PremiumRequest = KtorUsersSettingsBillingPremiumRequest(client)

    override val usage: Users.Settings.Billing.Usage = KtorUsersSettingsBillingUsage(client)
}

internal class KtorUsersSettingsBillingPremiumRequest(private val client: HttpClient) : Users.Settings.Billing.PremiumRequest {
    override val usage: Users.Settings.Billing.PremiumRequest.UsageApi = KtorUsersSettingsBillingPremiumRequestUsageApi(client)
}

internal class KtorUsersSettingsBillingPremiumRequestUsageApi(private val client: HttpClient) : Users.Settings.Billing.PremiumRequest.UsageApi {
    override suspend fun billingGetGithubBillingPremiumRequestUsageReportUser(username: String, day: Long?, model: String?, month: Long?, product: String?, year: Long?): Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult {
        val response = client.get("/users/$username/settings/billing/premium_request/usage") {
            day?.let { parameter("day", it) }
            model?.let { parameter("model", it) }
            month?.let { parameter("month", it) }
            product?.let { parameter("product", it) }
            year?.let { parameter("year", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult.OK(response.body())
            HttpStatusCode.BadRequest -> Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Users.Settings.Billing.PremiumRequest.UsageApi.BillingGetGithubBillingPremiumRequestUsageReportUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersSettingsBillingUsage(private val client: HttpClient) : Users.Settings.Billing.Usage {
    override val summary: Users.Settings.Billing.Usage.Summary = KtorUsersSettingsBillingUsageSummary(client)

    override suspend fun billingGetGithubBillingUsageReportUser(username: String, day: Long?, month: Long?, year: Long?): Users.Settings.Billing.Usage.BillingGetGithubBillingUsageReportUserResult {
        val response = client.get("/users/$username/settings/billing/usage") {
            day?.let { parameter("day", it) }
            month?.let { parameter("month", it) }
            year?.let { parameter("year", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Settings.Billing.Usage.BillingGetGithubBillingUsageReportUserResult.OK(response.body())
            HttpStatusCode.BadRequest -> Users.Settings.Billing.Usage.BillingGetGithubBillingUsageReportUserResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Users.Settings.Billing.Usage.BillingGetGithubBillingUsageReportUserResult.Forbidden(response.body())
            HttpStatusCode.InternalServerError -> Users.Settings.Billing.Usage.BillingGetGithubBillingUsageReportUserResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Users.Settings.Billing.Usage.BillingGetGithubBillingUsageReportUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersSettingsBillingUsageSummary(private val client: HttpClient) : Users.Settings.Billing.Usage.Summary {
    override suspend fun billingGetGithubBillingUsageSummaryReportUser(username: String, day: Long?, month: Long?, product: String?, repository: String?, sku: String?, year: Long?): Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult {
        val response = client.get("/users/$username/settings/billing/usage/summary") {
            day?.let { parameter("day", it) }
            month?.let { parameter("month", it) }
            product?.let { parameter("product", it) }
            repository?.let { parameter("repository", it) }
            sku?.let { parameter("sku", it) }
            year?.let { parameter("year", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult.OK(response.body())
            HttpStatusCode.BadRequest -> Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult.NotFound(response.body())
            HttpStatusCode.InternalServerError -> Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult.InternalServerError(response.body())
            HttpStatusCode.ServiceUnavailable -> Users.Settings.Billing.Usage.Summary.BillingGetGithubBillingUsageSummaryReportUserResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorUsersSocialAccounts(private val client: HttpClient) : Users.SocialAccounts {
    override suspend fun usersListSocialAccountsForUser(username: String, page: Long, perPage: Long): List<SocialAccount> =
        client.get("/users/$username/social_accounts") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersSshSigningKeys(private val client: HttpClient) : Users.SshSigningKeys {
    override suspend fun usersListSshSigningKeysForUser(username: String, page: Long, perPage: Long): List<SshSigningKey> =
        client.get("/users/$username/ssh_signing_keys") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}

internal class KtorUsersStarred(private val client: HttpClient) : Users.Starred {
    override suspend fun activityListReposStarredByUser(username: String, direction: Users.Starred.Direction, page: Long, perPage: Long, sort: Users.Starred.Sort): Users.Starred.ActivityListReposStarredByUserResponse =
        client.get("/users/$username/starred") {
            parameter("direction", direction)
            parameter("page", page)
            parameter("per_page", perPage)
            parameter("sort", sort)
        }.body()
}

internal class KtorUsersSubscriptions(private val client: HttpClient) : Users.Subscriptions {
    override suspend fun activityListReposWatchedByUser(username: String, page: Long, perPage: Long): List<MinimalRepository> =
        client.get("/users/$username/subscriptions") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
}
