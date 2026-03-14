package io.github.nomisrev.api

import io.github.nomisrev.model.ActionsCacheRetentionLimitForEnterprise
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.ActionsCacheStorageLimitForEnterprise
import io.github.nomisrev.model.OidcCustomPropertyInclusion
import io.github.nomisrev.model.OidcCustomPropertyInclusionInput
import io.github.nomisrev.model.CodeScanningOptions
import io.github.nomisrev.model.CodeScanningDefaultSetupOptions
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import io.github.nomisrev.model.CodeSecurityConfiguration
import io.github.nomisrev.model.CodeSecurityDefaultConfigurations
import kotlinx.serialization.json.JsonElement
import io.github.nomisrev.model.CodeSecurityConfigurationRepositories
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import io.github.nomisrev.model.DependabotAlertWithRepositoryResponse
import io.github.nomisrev.model.ValidationErrorSimple
import io.github.nomisrev.model.EnterpriseTeam
import io.github.nomisrev.model.SimpleUser
import io.github.nomisrev.model.OrganizationSimple
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

interface Enterprises {
    val actions: Enterprises.Actions

    val codeSecurity: Enterprises.CodeSecurity

    val dependabot: Enterprises.Dependabot

    val teams: Enterprises.Teams

    interface Actions {
        val cache: Enterprises.Actions.Cache

        val oidc: Enterprises.Actions.Oidc

        interface Cache {
            val retentionLimit: Enterprises.Actions.Cache.RetentionLimit

            val storageLimit: Enterprises.Actions.Cache.StorageLimit

            interface RetentionLimit {
                sealed interface ActionsGetActionsCacheRetentionLimitForEnterpriseResult {
                    data class OK(val value: ActionsCacheRetentionLimitForEnterprise) : ActionsGetActionsCacheRetentionLimitForEnterpriseResult

                    data class Forbidden(val value: BasicError) : ActionsGetActionsCacheRetentionLimitForEnterpriseResult

                    data class NotFound(val value: BasicError) : ActionsGetActionsCacheRetentionLimitForEnterpriseResult
                }

                suspend fun actionsGetActionsCacheRetentionLimitForEnterprise(
                    enterprise: String,
                ): ActionsGetActionsCacheRetentionLimitForEnterpriseResult

                sealed interface ActionsSetActionsCacheRetentionLimitForEnterpriseResult {
                    data object NoContent : ActionsSetActionsCacheRetentionLimitForEnterpriseResult

                    data class BadRequest(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForEnterpriseResult

                    data class Forbidden(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForEnterpriseResult

                    data class NotFound(val value: BasicError) : ActionsSetActionsCacheRetentionLimitForEnterpriseResult
                }

                suspend fun actionsSetActionsCacheRetentionLimitForEnterprise(
                    enterprise: String,
                    body: ActionsCacheRetentionLimitForEnterprise,
                ): ActionsSetActionsCacheRetentionLimitForEnterpriseResult
            }

            interface StorageLimit {
                sealed interface ActionsGetActionsCacheStorageLimitForEnterpriseResult {
                    data class OK(val value: ActionsCacheStorageLimitForEnterprise) : ActionsGetActionsCacheStorageLimitForEnterpriseResult

                    data class Forbidden(val value: BasicError) : ActionsGetActionsCacheStorageLimitForEnterpriseResult

                    data class NotFound(val value: BasicError) : ActionsGetActionsCacheStorageLimitForEnterpriseResult
                }

                suspend fun actionsGetActionsCacheStorageLimitForEnterprise(
                    enterprise: String,
                ): ActionsGetActionsCacheStorageLimitForEnterpriseResult

                sealed interface ActionsSetActionsCacheStorageLimitForEnterpriseResult {
                    data object NoContent : ActionsSetActionsCacheStorageLimitForEnterpriseResult

                    data class BadRequest(val value: BasicError) : ActionsSetActionsCacheStorageLimitForEnterpriseResult

                    data class Forbidden(val value: BasicError) : ActionsSetActionsCacheStorageLimitForEnterpriseResult

                    data class NotFound(val value: BasicError) : ActionsSetActionsCacheStorageLimitForEnterpriseResult
                }

                suspend fun actionsSetActionsCacheStorageLimitForEnterprise(
                    enterprise: String,
                    body: ActionsCacheStorageLimitForEnterprise,
                ): ActionsSetActionsCacheStorageLimitForEnterpriseResult
            }
        }

        interface Oidc {
            val customization: Enterprises.Actions.Oidc.Customization

            interface Customization {
                val properties: Enterprises.Actions.Oidc.Customization.Properties

                interface Properties {
                    val repo: Enterprises.Actions.Oidc.Customization.Properties.Repo

                    interface Repo {
                        sealed interface OidcListOidcCustomPropertyInclusionsForEnterpriseResult {
                            data class OK(val value: List<OidcCustomPropertyInclusion>) : OidcListOidcCustomPropertyInclusionsForEnterpriseResult

                            data class Forbidden(val value: BasicError) : OidcListOidcCustomPropertyInclusionsForEnterpriseResult

                            data class NotFound(val value: BasicError) : OidcListOidcCustomPropertyInclusionsForEnterpriseResult
                        }

                        suspend fun oidcListOidcCustomPropertyInclusionsForEnterprise(
                            enterprise: String,
                        ): OidcListOidcCustomPropertyInclusionsForEnterpriseResult

                        sealed interface OidcCreateOidcCustomPropertyInclusionForEnterpriseResult {
                            data class Created(val value: OidcCustomPropertyInclusion) : OidcCreateOidcCustomPropertyInclusionForEnterpriseResult

                            data object BadRequest : OidcCreateOidcCustomPropertyInclusionForEnterpriseResult

                            data class Forbidden(val value: BasicError) : OidcCreateOidcCustomPropertyInclusionForEnterpriseResult

                            data object UnprocessableEntity : OidcCreateOidcCustomPropertyInclusionForEnterpriseResult
                        }

                        suspend fun oidcCreateOidcCustomPropertyInclusionForEnterprise(
                            enterprise: String,
                            body: OidcCustomPropertyInclusionInput,
                        ): OidcCreateOidcCustomPropertyInclusionForEnterpriseResult

                        sealed interface OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult {
                            data object NoContent : OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult

                            data object BadRequest : OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult

                            data class Forbidden(val value: BasicError) : OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult

                            data object NotFound : OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult
                        }

                        suspend fun oidcDeleteOidcCustomPropertyInclusionForEnterprise(
                            enterprise: String,
                            customPropertyName: String,
                        ): OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult
                    }
                }
            }
        }
    }

    interface CodeSecurity {
        val configurations: Enterprises.CodeSecurity.Configurations

        interface Configurations {
            val defaults: Enterprises.CodeSecurity.Configurations.Defaults

            val attach: Enterprises.CodeSecurity.Configurations.Attach

            val repositories: Enterprises.CodeSecurity.Configurations.Repositories

            @Serializable
            data class CodeSecurityCreateConfigurationForEnterpriseBody(
                val name: String,
                val description: String,
                @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
                @SerialName("code_security") val codeSecurity: CodeSecurity? = null,
                @SerialName("dependency_graph") val dependencyGraph: DependencyGraph? = null,
                @SerialName("dependency_graph_autosubmit_action") val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
                @SerialName("dependency_graph_autosubmit_action_options") val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
                @SerialName("dependabot_alerts") val dependabotAlerts: DependabotAlerts? = null,
                @SerialName("dependabot_security_updates") val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
                @SerialName("code_scanning_options") val codeScanningOptions: CodeScanningOptions? = null,
                @SerialName("code_scanning_default_setup") val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
                @SerialName("code_scanning_default_setup_options") val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
                @SerialName("code_scanning_delegated_alert_dismissal") val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_protection") val secretProtection: SecretProtection? = null,
                @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
                @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
                @SerialName("secret_scanning_validity_checks") val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
                @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
                @SerialName("secret_scanning_generic_secrets") val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
                @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_scanning_extended_metadata") val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
                @SerialName("private_vulnerability_reporting") val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
                val enforcement: Enforcement? = null,
            ) {
                @Serializable
                enum class AdvancedSecurity {
                    @SerialName("enabled")
                    Enabled,
                    @SerialName("disabled")
                    Disabled,
                    @SerialName("code_security")
                    CodeSecurity,
                    @SerialName("secret_protection")
                    SecretProtection;
                }

                @Serializable
                enum class CodeSecurity {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraph {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraphAutosubmitAction {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                @JvmInline
                value class DependencyGraphAutosubmitActionOptions(@SerialName("labeled_runners") val labeledRunners: Boolean? = null)

                @Serializable
                enum class DependabotAlerts {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependabotSecurityUpdates {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDefaultSetup {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanning {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningPushProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningValidityChecks {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningNonProviderPatterns {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningGenericSecrets {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningExtendedMetadata {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class PrivateVulnerabilityReporting {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class Enforcement {
                    @SerialName("enforced") Enforced, @SerialName("unenforced") Unenforced;
                }
            }


            @Serializable
            data class CodeSecurityUpdateEnterpriseConfigurationBody(
                val name: String? = null,
                val description: String? = null,
                @SerialName("advanced_security") val advancedSecurity: AdvancedSecurity? = null,
                @SerialName("code_security") val codeSecurity: CodeSecurity? = null,
                @SerialName("dependency_graph") val dependencyGraph: DependencyGraph? = null,
                @SerialName("dependency_graph_autosubmit_action") val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
                @SerialName("dependency_graph_autosubmit_action_options") val dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
                @SerialName("dependabot_alerts") val dependabotAlerts: DependabotAlerts? = null,
                @SerialName("dependabot_security_updates") val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
                @SerialName("code_scanning_default_setup") val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
                @SerialName("code_scanning_default_setup_options") val codeScanningDefaultSetupOptions: CodeScanningDefaultSetupOptions? = null,
                @SerialName("code_scanning_options") val codeScanningOptions: CodeScanningOptions? = null,
                @SerialName("code_scanning_delegated_alert_dismissal") val codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_protection") val secretProtection: SecretProtection? = null,
                @SerialName("secret_scanning") val secretScanning: SecretScanning? = null,
                @SerialName("secret_scanning_push_protection") val secretScanningPushProtection: SecretScanningPushProtection? = null,
                @SerialName("secret_scanning_validity_checks") val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
                @SerialName("secret_scanning_non_provider_patterns") val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
                @SerialName("secret_scanning_generic_secrets") val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
                @SerialName("secret_scanning_delegated_alert_dismissal") val secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
                @SerialName("secret_scanning_extended_metadata") val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
                @SerialName("private_vulnerability_reporting") val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
                val enforcement: Enforcement? = null,
            ) {
                @Serializable
                enum class AdvancedSecurity {
                    @SerialName("enabled")
                    Enabled,
                    @SerialName("disabled")
                    Disabled,
                    @SerialName("code_security")
                    CodeSecurity,
                    @SerialName("secret_protection")
                    SecretProtection;
                }

                @Serializable
                enum class CodeSecurity {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraph {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependencyGraphAutosubmitAction {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                @JvmInline
                value class DependencyGraphAutosubmitActionOptions(@SerialName("labeled_runners") val labeledRunners: Boolean? = null)

                @Serializable
                enum class DependabotAlerts {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class DependabotSecurityUpdates {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDefaultSetup {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class CodeScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanning {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningPushProtection {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningValidityChecks {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningNonProviderPatterns {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningGenericSecrets {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningDelegatedAlertDismissal {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class SecretScanningExtendedMetadata {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class PrivateVulnerabilityReporting {
                    @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("not_set") NotSet;
                }

                @Serializable
                enum class Enforcement {
                    @SerialName("enforced") Enforced, @SerialName("unenforced") Unenforced;
                }
            }

            sealed interface CodeSecurityGetConfigurationsForEnterpriseResult {
                data class OK(val value: List<CodeSecurityConfiguration>) : CodeSecurityGetConfigurationsForEnterpriseResult

                data class Forbidden(val value: BasicError) : CodeSecurityGetConfigurationsForEnterpriseResult

                data class NotFound(val value: BasicError) : CodeSecurityGetConfigurationsForEnterpriseResult
            }

            suspend fun codeSecurityGetConfigurationsForEnterprise(
                enterprise: String,
                perPage: Long = 30L,
                after: String? = null,
                before: String? = null,
            ): CodeSecurityGetConfigurationsForEnterpriseResult

            sealed interface CodeSecurityCreateConfigurationForEnterpriseResult {
                data class Created(val value: CodeSecurityConfiguration) : CodeSecurityCreateConfigurationForEnterpriseResult

                data class BadRequest(val value: BasicError) : CodeSecurityCreateConfigurationForEnterpriseResult

                data class Forbidden(val value: BasicError) : CodeSecurityCreateConfigurationForEnterpriseResult

                data class NotFound(val value: BasicError) : CodeSecurityCreateConfigurationForEnterpriseResult
            }

            suspend fun codeSecurityCreateConfigurationForEnterprise(
                enterprise: String,
                body: CodeSecurityCreateConfigurationForEnterpriseBody,
            ): CodeSecurityCreateConfigurationForEnterpriseResult

            sealed interface CodeSecurityGetSingleConfigurationForEnterpriseResult {
                data class OK(val value: CodeSecurityConfiguration) : CodeSecurityGetSingleConfigurationForEnterpriseResult

                data object NotModified : CodeSecurityGetSingleConfigurationForEnterpriseResult

                data class Forbidden(val value: BasicError) : CodeSecurityGetSingleConfigurationForEnterpriseResult

                data class NotFound(val value: BasicError) : CodeSecurityGetSingleConfigurationForEnterpriseResult
            }

            suspend fun codeSecurityGetSingleConfigurationForEnterprise(
                enterprise: String,
                configurationId: Long,
            ): CodeSecurityGetSingleConfigurationForEnterpriseResult

            sealed interface CodeSecurityDeleteConfigurationForEnterpriseResult {
                data object NoContent : CodeSecurityDeleteConfigurationForEnterpriseResult

                data class BadRequest(val value: BasicError) : CodeSecurityDeleteConfigurationForEnterpriseResult

                data class Forbidden(val value: BasicError) : CodeSecurityDeleteConfigurationForEnterpriseResult

                data class NotFound(val value: BasicError) : CodeSecurityDeleteConfigurationForEnterpriseResult

                data class Conflict(val value: BasicError) : CodeSecurityDeleteConfigurationForEnterpriseResult
            }

            suspend fun codeSecurityDeleteConfigurationForEnterprise(
                enterprise: String,
                configurationId: Long,
            ): CodeSecurityDeleteConfigurationForEnterpriseResult

            sealed interface CodeSecurityUpdateEnterpriseConfigurationResult {
                data class OK(val value: CodeSecurityConfiguration) : CodeSecurityUpdateEnterpriseConfigurationResult

                data object NotModified : CodeSecurityUpdateEnterpriseConfigurationResult

                data class Forbidden(val value: BasicError) : CodeSecurityUpdateEnterpriseConfigurationResult

                data class NotFound(val value: BasicError) : CodeSecurityUpdateEnterpriseConfigurationResult

                data class Conflict(val value: BasicError) : CodeSecurityUpdateEnterpriseConfigurationResult
            }

            suspend fun codeSecurityUpdateEnterpriseConfiguration(
                enterprise: String,
                configurationId: Long,
                body: CodeSecurityUpdateEnterpriseConfigurationBody,
            ): CodeSecurityUpdateEnterpriseConfigurationResult

            interface Defaults {
                @Serializable
                @JvmInline
                value class CodeSecuritySetConfigurationAsDefaultForEnterpriseBody(@SerialName("default_for_new_repos") val defaultForNewRepos: DefaultForNewRepos? = null) {
                    @Serializable
                    enum class DefaultForNewRepos {
                        @SerialName("all")
                        All,
                        @SerialName("none")
                        None,
                        @SerialName("private_and_internal")
                        PrivateAndInternal,
                        @SerialName("public")
                        Public;
                    }
                }


                @Serializable
                data class CodeSecuritySetConfigurationAsDefaultForEnterpriseResponse(
                    @SerialName("default_for_new_repos") val defaultForNewRepos: DefaultForNewRepos? = null,
                    val configuration: CodeSecurityConfiguration? = null,
                ) {
                    @Serializable
                    enum class DefaultForNewRepos {
                        @SerialName("all")
                        All,
                        @SerialName("none")
                        None,
                        @SerialName("private_and_internal")
                        PrivateAndInternal,
                        @SerialName("public")
                        Public;
                    }
                }

                suspend fun codeSecurityGetDefaultConfigurationsForEnterprise(
                    enterprise: String,
                ): CodeSecurityDefaultConfigurations

                sealed interface CodeSecuritySetConfigurationAsDefaultForEnterpriseResult {
                    data class OK(val value: CodeSecuritySetConfigurationAsDefaultForEnterpriseResponse) : CodeSecuritySetConfigurationAsDefaultForEnterpriseResult

                    data class Forbidden(val value: BasicError) : CodeSecuritySetConfigurationAsDefaultForEnterpriseResult

                    data class NotFound(val value: BasicError) : CodeSecuritySetConfigurationAsDefaultForEnterpriseResult
                }

                suspend fun codeSecuritySetConfigurationAsDefaultForEnterprise(
                    enterprise: String,
                    configurationId: Long,
                    body: CodeSecuritySetConfigurationAsDefaultForEnterpriseBody,
                ): CodeSecuritySetConfigurationAsDefaultForEnterpriseResult
            }

            interface Attach {
                @Serializable
                @JvmInline
                value class CodeSecurityAttachEnterpriseConfigurationBody(val scope: Scope) {
                    @Serializable
                    enum class Scope {
                        @SerialName("all") All, @SerialName("all_without_configurations") AllWithoutConfigurations;
                    }
                }

                sealed interface CodeSecurityAttachEnterpriseConfigurationResult {
                    data class Accepted(val value: JsonElement) : CodeSecurityAttachEnterpriseConfigurationResult

                    data class Forbidden(val value: BasicError) : CodeSecurityAttachEnterpriseConfigurationResult

                    data class NotFound(val value: BasicError) : CodeSecurityAttachEnterpriseConfigurationResult

                    data class Conflict(val value: BasicError) : CodeSecurityAttachEnterpriseConfigurationResult
                }

                suspend fun codeSecurityAttachEnterpriseConfiguration(
                    enterprise: String,
                    configurationId: Long,
                    body: CodeSecurityAttachEnterpriseConfigurationBody,
                ): CodeSecurityAttachEnterpriseConfigurationResult
            }

            interface Repositories {
                sealed interface CodeSecurityGetRepositoriesForEnterpriseConfigurationResult {
                    data class OK(val value: List<CodeSecurityConfigurationRepositories>) : CodeSecurityGetRepositoriesForEnterpriseConfigurationResult

                    data class Forbidden(val value: BasicError) : CodeSecurityGetRepositoriesForEnterpriseConfigurationResult

                    data class NotFound(val value: BasicError) : CodeSecurityGetRepositoriesForEnterpriseConfigurationResult
                }

                suspend fun codeSecurityGetRepositoriesForEnterpriseConfiguration(
                    enterprise: String,
                    configurationId: Long,
                    perPage: Long = 30L,
                    status: String = "all",
                    after: String? = null,
                    before: String? = null,
                ): CodeSecurityGetRepositoriesForEnterpriseConfigurationResult
            }
        }
    }

    interface Dependabot {
        val alerts: Enterprises.Dependabot.Alerts

        interface Alerts {
            @Serializable
            enum class Direction {
                @SerialName("asc") Asc, @SerialName("desc") Desc;
            }


            @Serializable(with = Has.Serializer::class)
            sealed interface Has {
                @Serializable
                @JvmInline
                value class CaseString(val value: String) : Has

                @Serializable
                @JvmInline
                value class Patchs(val value: List<Patch>) : Has {
                    @Serializable
                    enum class Patch {
                        @SerialName("patch") Patch;
                    }
                }

                object Serializer : KSerializer<Has> {
                    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                    override val descriptor: SerialDescriptor =
                        buildSerialDescriptor("io.github.nomisrev.api.Enterprises.Dependabot.Alerts.Has", PolymorphicKind.SEALED) {
                            element("CaseString", String.serializer().descriptor)
                            element("Patchs", ListSerializer(Patchs.Patch.serializer()).descriptor)
                        }

                    override fun deserialize(decoder: Decoder): Has {
                        val value = decoder.decodeSerializableValue(JsonElement.serializer())
                        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                        return json.attemptDeserialize(
                            value,
                            Patchs::class to { Patchs(decodeFromJsonElement(ListSerializer(Patchs.Patch.serializer()), it)) },
                            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                        )
                    }

                    override fun serialize(encoder: Encoder, value: Has) = when(value) {
                        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                        is Patchs -> encoder.encodeSerializableValue(ListSerializer(Patchs.Patch.serializer()), value.value)
                    }
                }
            }


            @Serializable
            enum class Scope {
                @SerialName("development") Development, @SerialName("runtime") Runtime;
            }


            @Serializable
            enum class Sort {
                @SerialName("created") Created, @SerialName("updated") Updated, @SerialName("epss_percentage") EpssPercentage;
            }

            sealed interface DependabotListAlertsForEnterpriseResult {
                data class OK(val value: List<DependabotAlertWithRepositoryResponse>) : DependabotListAlertsForEnterpriseResult

                data object NotModified : DependabotListAlertsForEnterpriseResult

                data class Forbidden(val value: BasicError) : DependabotListAlertsForEnterpriseResult

                data class NotFound(val value: BasicError) : DependabotListAlertsForEnterpriseResult

                data class UnprocessableEntity(val value: ValidationErrorSimple) : DependabotListAlertsForEnterpriseResult
            }

            suspend fun dependabotListAlertsForEnterprise(
                enterprise: String,
                direction: Direction = Direction.Desc,
                perPage: Long = 30L,
                sort: Sort = Sort.Created,
                after: String? = null,
                assignee: String? = null,
                before: String? = null,
                ecosystem: String? = null,
                epssPercentage: String? = null,
                has: Has? = null,
                `package`: String? = null,
                scope: Scope? = null,
                severity: String? = null,
                state: String? = null,
            ): DependabotListAlertsForEnterpriseResult
        }
    }

    interface Teams {
        val memberships: Enterprises.Teams.Memberships

        val organizations: Enterprises.Teams.Organizations

        @Serializable
        data class EnterpriseTeamsCreateBody(
            val name: String,
            val description: String? = null,
            @SerialName("sync_to_organizations") val syncToOrganizations: SyncToOrganizations? = null,
            @SerialName("organization_selection_type") val organizationSelectionType: OrganizationSelectionType? = null,
            @SerialName("group_id") val groupId: String? = null,
        ) {
            @Serializable
            enum class SyncToOrganizations {
                @SerialName("all") All, @SerialName("disabled") Disabled;
            }

            @Serializable
            enum class OrganizationSelectionType {
                @SerialName("disabled") Disabled, @SerialName("selected") Selected, @SerialName("all") All;
            }
        }


        @Serializable
        data class EnterpriseTeamsUpdateBody(
            val name: String? = null,
            val description: String? = null,
            @SerialName("sync_to_organizations") val syncToOrganizations: SyncToOrganizations? = null,
            @SerialName("organization_selection_type") val organizationSelectionType: OrganizationSelectionType? = null,
            @SerialName("group_id") val groupId: String? = null,
        ) {
            @Serializable
            enum class SyncToOrganizations {
                @SerialName("all") All, @SerialName("disabled") Disabled;
            }

            @Serializable
            enum class OrganizationSelectionType {
                @SerialName("disabled") Disabled, @SerialName("selected") Selected, @SerialName("all") All;
            }
        }

        sealed interface EnterpriseTeamsListResult {
            data class OK(val value: List<EnterpriseTeam>) : EnterpriseTeamsListResult

            data class Forbidden(val value: BasicError) : EnterpriseTeamsListResult
        }

        suspend fun enterpriseTeamsList(
            enterprise: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): EnterpriseTeamsListResult

        suspend fun enterpriseTeamsCreate(
            enterprise: String,
            body: EnterpriseTeamsCreateBody,
        ): EnterpriseTeam

        sealed interface EnterpriseTeamsGetResult {
            data class OK(val value: EnterpriseTeam) : EnterpriseTeamsGetResult

            data class Forbidden(val value: BasicError) : EnterpriseTeamsGetResult
        }

        suspend fun enterpriseTeamsGet(
            enterprise: String,
            teamSlug: String,
        ): EnterpriseTeamsGetResult

        sealed interface EnterpriseTeamsDeleteResult {
            data object NoContent : EnterpriseTeamsDeleteResult

            data class Forbidden(val value: BasicError) : EnterpriseTeamsDeleteResult
        }

        suspend fun enterpriseTeamsDelete(
            enterprise: String,
            teamSlug: String,
        ): EnterpriseTeamsDeleteResult

        sealed interface EnterpriseTeamsUpdateResult {
            data class OK(val value: EnterpriseTeam) : EnterpriseTeamsUpdateResult

            data class Forbidden(val value: BasicError) : EnterpriseTeamsUpdateResult
        }

        suspend fun enterpriseTeamsUpdate(
            enterprise: String,
            teamSlug: String,
            body: EnterpriseTeamsUpdateBody,
        ): EnterpriseTeamsUpdateResult

        interface Memberships {
            val add: Enterprises.Teams.Memberships.Add

            val remove: Enterprises.Teams.Memberships.Remove

            suspend fun enterpriseTeamMembershipsList(
                enterprise: String,
                enterpriseTeam: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<SimpleUser>

            suspend fun enterpriseTeamMembershipsGet(
                enterprise: String,
                enterpriseTeam: String,
                username: String,
            ): SimpleUser

            suspend fun enterpriseTeamMembershipsAdd(
                enterprise: String,
                enterpriseTeam: String,
                username: String,
            ): SimpleUser

            sealed interface EnterpriseTeamMembershipsRemoveResult {
                data object NoContent : EnterpriseTeamMembershipsRemoveResult

                data class Forbidden(val value: BasicError) : EnterpriseTeamMembershipsRemoveResult
            }

            suspend fun enterpriseTeamMembershipsRemove(
                enterprise: String,
                enterpriseTeam: String,
                username: String,
            ): EnterpriseTeamMembershipsRemoveResult

            interface Add {
                @Serializable
                @JvmInline
                value class EnterpriseTeamMembershipsBulkAddBody(val usernames: List<String>)

                suspend fun enterpriseTeamMembershipsBulkAdd(
                    enterprise: String,
                    enterpriseTeam: String,
                    body: EnterpriseTeamMembershipsBulkAddBody,
                ): List<SimpleUser>
            }

            interface Remove {
                @Serializable
                @JvmInline
                value class EnterpriseTeamMembershipsBulkRemoveBody(val usernames: List<String>)

                suspend fun enterpriseTeamMembershipsBulkRemove(
                    enterprise: String,
                    enterpriseTeam: String,
                    body: EnterpriseTeamMembershipsBulkRemoveBody,
                ): List<SimpleUser>
            }
        }

        interface Organizations {
            val add: Enterprises.Teams.Organizations.Add

            val remove: Enterprises.Teams.Organizations.Remove

            suspend fun enterpriseTeamOrganizationsGetAssignments(
                enterprise: String,
                enterpriseTeam: String,
                page: Long = 1L,
                perPage: Long = 30L,
            ): List<OrganizationSimple>

            sealed interface EnterpriseTeamOrganizationsGetAssignmentResult {
                data class OK(val value: OrganizationSimple) : EnterpriseTeamOrganizationsGetAssignmentResult

                data object NotFound : EnterpriseTeamOrganizationsGetAssignmentResult
            }

            suspend fun enterpriseTeamOrganizationsGetAssignment(
                enterprise: String,
                enterpriseTeam: String,
                org: String,
            ): EnterpriseTeamOrganizationsGetAssignmentResult

            suspend fun enterpriseTeamOrganizationsAdd(
                enterprise: String,
                enterpriseTeam: String,
                org: String,
            ): OrganizationSimple

            suspend fun enterpriseTeamOrganizationsDelete(
                enterprise: String,
                enterpriseTeam: String,
                org: String,
            ): Unit

            interface Add {
                @Serializable
                @JvmInline
                value class EnterpriseTeamOrganizationsBulkAddBody(@SerialName("organization_slugs") val organizationSlugs: List<String>)

                suspend fun enterpriseTeamOrganizationsBulkAdd(
                    enterprise: String,
                    enterpriseTeam: String,
                    body: EnterpriseTeamOrganizationsBulkAddBody,
                ): List<OrganizationSimple>
            }

            interface Remove {
                @Serializable
                @JvmInline
                value class EnterpriseTeamOrganizationsBulkRemoveBody(@SerialName("organization_slugs") val organizationSlugs: List<String>)

                suspend fun enterpriseTeamOrganizationsBulkRemove(
                    enterprise: String,
                    enterpriseTeam: String,
                    body: EnterpriseTeamOrganizationsBulkRemoveBody,
                ): Unit
            }
        }
    }
}

internal class KtorEnterprises(private val client: HttpClient) : Enterprises {
    override val actions: Enterprises.Actions = KtorEnterprisesActions(client)

    override val codeSecurity: Enterprises.CodeSecurity = KtorEnterprisesCodeSecurity(client)

    override val dependabot: Enterprises.Dependabot = KtorEnterprisesDependabot(client)

    override val teams: Enterprises.Teams = KtorEnterprisesTeams(client)
}

internal class KtorEnterprisesActions(private val client: HttpClient) : Enterprises.Actions {
    override val cache: Enterprises.Actions.Cache = KtorEnterprisesActionsCache(client)

    override val oidc: Enterprises.Actions.Oidc = KtorEnterprisesActionsOidc(client)
}

internal class KtorEnterprisesActionsCache(private val client: HttpClient) : Enterprises.Actions.Cache {
    override val retentionLimit: Enterprises.Actions.Cache.RetentionLimit = KtorEnterprisesActionsCacheRetentionLimit(client)

    override val storageLimit: Enterprises.Actions.Cache.StorageLimit = KtorEnterprisesActionsCacheStorageLimit(client)
}

internal class KtorEnterprisesActionsCacheRetentionLimit(private val client: HttpClient) : Enterprises.Actions.Cache.RetentionLimit {
    override suspend fun actionsGetActionsCacheRetentionLimitForEnterprise(enterprise: String): Enterprises.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForEnterpriseResult {
        val response = client.get("/enterprises/$enterprise/actions/cache/retention-limit")
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForEnterpriseResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Actions.Cache.RetentionLimit.ActionsGetActionsCacheRetentionLimitForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetActionsCacheRetentionLimitForEnterprise(enterprise: String, body: ActionsCacheRetentionLimitForEnterprise): Enterprises.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForEnterpriseResult {
        val response = client.put("/enterprises/$enterprise/actions/cache/retention-limit") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Enterprises.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForEnterpriseResult.NoContent
            HttpStatusCode.BadRequest -> Enterprises.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForEnterpriseResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Actions.Cache.RetentionLimit.ActionsSetActionsCacheRetentionLimitForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesActionsCacheStorageLimit(private val client: HttpClient) : Enterprises.Actions.Cache.StorageLimit {
    override suspend fun actionsGetActionsCacheStorageLimitForEnterprise(enterprise: String): Enterprises.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForEnterpriseResult {
        val response = client.get("/enterprises/$enterprise/actions/cache/storage-limit")
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForEnterpriseResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Actions.Cache.StorageLimit.ActionsGetActionsCacheStorageLimitForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun actionsSetActionsCacheStorageLimitForEnterprise(enterprise: String, body: ActionsCacheStorageLimitForEnterprise): Enterprises.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForEnterpriseResult {
        val response = client.put("/enterprises/$enterprise/actions/cache/storage-limit") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.NoContent -> Enterprises.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForEnterpriseResult.NoContent
            HttpStatusCode.BadRequest -> Enterprises.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForEnterpriseResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Actions.Cache.StorageLimit.ActionsSetActionsCacheStorageLimitForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesActionsOidc(private val client: HttpClient) : Enterprises.Actions.Oidc {
    override val customization: Enterprises.Actions.Oidc.Customization = KtorEnterprisesActionsOidcCustomization(client)
}

internal class KtorEnterprisesActionsOidcCustomization(private val client: HttpClient) : Enterprises.Actions.Oidc.Customization {
    override val properties: Enterprises.Actions.Oidc.Customization.Properties = KtorEnterprisesActionsOidcCustomizationProperties(client)
}

internal class KtorEnterprisesActionsOidcCustomizationProperties(private val client: HttpClient) : Enterprises.Actions.Oidc.Customization.Properties {
    override val repo: Enterprises.Actions.Oidc.Customization.Properties.Repo = KtorEnterprisesActionsOidcCustomizationPropertiesRepo(client)
}

internal class KtorEnterprisesActionsOidcCustomizationPropertiesRepo(private val client: HttpClient) : Enterprises.Actions.Oidc.Customization.Properties.Repo {
    override suspend fun oidcListOidcCustomPropertyInclusionsForEnterprise(enterprise: String): Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcListOidcCustomPropertyInclusionsForEnterpriseResult {
        val response = client.get("/enterprises/$enterprise/actions/oidc/customization/properties/repo")
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcListOidcCustomPropertyInclusionsForEnterpriseResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcListOidcCustomPropertyInclusionsForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcListOidcCustomPropertyInclusionsForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun oidcCreateOidcCustomPropertyInclusionForEnterprise(enterprise: String, body: OidcCustomPropertyInclusionInput): Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcCreateOidcCustomPropertyInclusionForEnterpriseResult {
        val response = client.post("/enterprises/$enterprise/actions/oidc/customization/properties/repo") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcCreateOidcCustomPropertyInclusionForEnterpriseResult.Created(response.body())
            HttpStatusCode.BadRequest -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcCreateOidcCustomPropertyInclusionForEnterpriseResult.BadRequest
            HttpStatusCode.Forbidden -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcCreateOidcCustomPropertyInclusionForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcCreateOidcCustomPropertyInclusionForEnterpriseResult.UnprocessableEntity
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun oidcDeleteOidcCustomPropertyInclusionForEnterprise(enterprise: String, customPropertyName: String): Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult {
        val response = client.delete("/enterprises/$enterprise/actions/oidc/customization/properties/repo/$customPropertyName")
        return when (response.status) {
            HttpStatusCode.NoContent -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult.NoContent
            HttpStatusCode.BadRequest -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult.BadRequest
            HttpStatusCode.Forbidden -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Actions.Oidc.Customization.Properties.Repo.OidcDeleteOidcCustomPropertyInclusionForEnterpriseResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesCodeSecurity(private val client: HttpClient) : Enterprises.CodeSecurity {
    override val configurations: Enterprises.CodeSecurity.Configurations = KtorEnterprisesCodeSecurityConfigurations(client)
}

internal class KtorEnterprisesCodeSecurityConfigurations(private val client: HttpClient) : Enterprises.CodeSecurity.Configurations {
    override val defaults: Enterprises.CodeSecurity.Configurations.Defaults = KtorEnterprisesCodeSecurityConfigurationsDefaults(client)

    override val attach: Enterprises.CodeSecurity.Configurations.Attach = KtorEnterprisesCodeSecurityConfigurationsAttach(client)

    override val repositories: Enterprises.CodeSecurity.Configurations.Repositories = KtorEnterprisesCodeSecurityConfigurationsRepositories(client)

    override suspend fun codeSecurityGetConfigurationsForEnterprise(enterprise: String, perPage: Long, after: String?, before: String?): Enterprises.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForEnterpriseResult {
        val response = client.get("/enterprises/$enterprise/code-security/configurations") {
            parameter("per_page", perPage)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForEnterpriseResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetConfigurationsForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityCreateConfigurationForEnterprise(enterprise: String, body: Enterprises.CodeSecurity.Configurations.CodeSecurityCreateConfigurationForEnterpriseBody): Enterprises.CodeSecurity.Configurations.CodeSecurityCreateConfigurationForEnterpriseResult {
        val response = client.post("/enterprises/$enterprise/code-security/configurations") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Enterprises.CodeSecurity.Configurations.CodeSecurityCreateConfigurationForEnterpriseResult.Created(response.body())
            HttpStatusCode.BadRequest -> Enterprises.CodeSecurity.Configurations.CodeSecurityCreateConfigurationForEnterpriseResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.CodeSecurityCreateConfigurationForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.CodeSecurityCreateConfigurationForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityGetSingleConfigurationForEnterprise(enterprise: String, configurationId: Long): Enterprises.CodeSecurity.Configurations.CodeSecurityGetSingleConfigurationForEnterpriseResult {
        val response = client.get("/enterprises/$enterprise/code-security/configurations/$configurationId")
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetSingleConfigurationForEnterpriseResult.OK(response.body())
            HttpStatusCode.NotModified -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetSingleConfigurationForEnterpriseResult.NotModified
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetSingleConfigurationForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.CodeSecurityGetSingleConfigurationForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityDeleteConfigurationForEnterprise(enterprise: String, configurationId: Long): Enterprises.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationForEnterpriseResult {
        val response = client.delete("/enterprises/$enterprise/code-security/configurations/$configurationId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Enterprises.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationForEnterpriseResult.NoContent
            HttpStatusCode.BadRequest -> Enterprises.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationForEnterpriseResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationForEnterpriseResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Enterprises.CodeSecurity.Configurations.CodeSecurityDeleteConfigurationForEnterpriseResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun codeSecurityUpdateEnterpriseConfiguration(enterprise: String, configurationId: Long, body: Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationBody): Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationResult {
        val response = client.patch("/enterprises/$enterprise/code-security/configurations/$configurationId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationResult.OK(response.body())
            HttpStatusCode.NotModified -> Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationResult.NotModified
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Enterprises.CodeSecurity.Configurations.CodeSecurityUpdateEnterpriseConfigurationResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesCodeSecurityConfigurationsDefaults(private val client: HttpClient) : Enterprises.CodeSecurity.Configurations.Defaults {
    override suspend fun codeSecurityGetDefaultConfigurationsForEnterprise(enterprise: String): CodeSecurityDefaultConfigurations =
        client.get("/enterprises/$enterprise/code-security/configurations/defaults").body()

    override suspend fun codeSecuritySetConfigurationAsDefaultForEnterprise(enterprise: String, configurationId: Long, body: Enterprises.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultForEnterpriseBody): Enterprises.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultForEnterpriseResult {
        val response = client.put("/enterprises/$enterprise/code-security/configurations/$configurationId/defaults") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultForEnterpriseResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.Defaults.CodeSecuritySetConfigurationAsDefaultForEnterpriseResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesCodeSecurityConfigurationsAttach(private val client: HttpClient) : Enterprises.CodeSecurity.Configurations.Attach {
    override suspend fun codeSecurityAttachEnterpriseConfiguration(enterprise: String, configurationId: Long, body: Enterprises.CodeSecurity.Configurations.Attach.CodeSecurityAttachEnterpriseConfigurationBody): Enterprises.CodeSecurity.Configurations.Attach.CodeSecurityAttachEnterpriseConfigurationResult {
        val response = client.post("/enterprises/$enterprise/code-security/configurations/$configurationId/attach") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Accepted -> Enterprises.CodeSecurity.Configurations.Attach.CodeSecurityAttachEnterpriseConfigurationResult.Accepted(response.body())
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.Attach.CodeSecurityAttachEnterpriseConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.Attach.CodeSecurityAttachEnterpriseConfigurationResult.NotFound(response.body())
            HttpStatusCode.Conflict -> Enterprises.CodeSecurity.Configurations.Attach.CodeSecurityAttachEnterpriseConfigurationResult.Conflict(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesCodeSecurityConfigurationsRepositories(private val client: HttpClient) : Enterprises.CodeSecurity.Configurations.Repositories {
    override suspend fun codeSecurityGetRepositoriesForEnterpriseConfiguration(enterprise: String, configurationId: Long, perPage: Long, status: String, after: String?, before: String?): Enterprises.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForEnterpriseConfigurationResult {
        val response = client.get("/enterprises/$enterprise/code-security/configurations/$configurationId/repositories") {
            parameter("per_page", perPage)
            parameter("status", status)
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForEnterpriseConfigurationResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForEnterpriseConfigurationResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.CodeSecurity.Configurations.Repositories.CodeSecurityGetRepositoriesForEnterpriseConfigurationResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesDependabot(private val client: HttpClient) : Enterprises.Dependabot {
    override val alerts: Enterprises.Dependabot.Alerts = KtorEnterprisesDependabotAlerts(client)
}

internal class KtorEnterprisesDependabotAlerts(private val client: HttpClient) : Enterprises.Dependabot.Alerts {
    override suspend fun dependabotListAlertsForEnterprise(enterprise: String, direction: Enterprises.Dependabot.Alerts.Direction, perPage: Long, sort: Enterprises.Dependabot.Alerts.Sort, after: String?, assignee: String?, before: String?, ecosystem: String?, epssPercentage: String?, has: Enterprises.Dependabot.Alerts.Has?, `package`: String?, scope: Enterprises.Dependabot.Alerts.Scope?, severity: String?, state: String?): Enterprises.Dependabot.Alerts.DependabotListAlertsForEnterpriseResult {
        val response = client.get("/enterprises/$enterprise/dependabot/alerts") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("sort", sort)
            after?.let { parameter("after", it) }
            assignee?.let { parameter("assignee", it) }
            before?.let { parameter("before", it) }
            ecosystem?.let { parameter("ecosystem", it) }
            epssPercentage?.let { parameter("epss_percentage", it) }
            has?.let { parameter("has", it) }
            `package`?.let { parameter("package", it) }
            scope?.let { parameter("scope", it) }
            severity?.let { parameter("severity", it) }
            state?.let { parameter("state", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Dependabot.Alerts.DependabotListAlertsForEnterpriseResult.OK(response.body())
            HttpStatusCode.NotModified -> Enterprises.Dependabot.Alerts.DependabotListAlertsForEnterpriseResult.NotModified
            HttpStatusCode.Forbidden -> Enterprises.Dependabot.Alerts.DependabotListAlertsForEnterpriseResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Enterprises.Dependabot.Alerts.DependabotListAlertsForEnterpriseResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Enterprises.Dependabot.Alerts.DependabotListAlertsForEnterpriseResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesTeams(private val client: HttpClient) : Enterprises.Teams {
    override val memberships: Enterprises.Teams.Memberships = KtorEnterprisesTeamsMemberships(client)

    override val organizations: Enterprises.Teams.Organizations = KtorEnterprisesTeamsOrganizations(client)

    override suspend fun enterpriseTeamsList(enterprise: String, page: Long, perPage: Long): Enterprises.Teams.EnterpriseTeamsListResult {
        val response = client.get("/enterprises/$enterprise/teams") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Teams.EnterpriseTeamsListResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Teams.EnterpriseTeamsListResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun enterpriseTeamsCreate(enterprise: String, body: Enterprises.Teams.EnterpriseTeamsCreateBody): EnterpriseTeam =
        client.post("/enterprises/$enterprise/teams") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()

    override suspend fun enterpriseTeamsGet(enterprise: String, teamSlug: String): Enterprises.Teams.EnterpriseTeamsGetResult {
        val response = client.get("/enterprises/$enterprise/teams/$teamSlug")
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Teams.EnterpriseTeamsGetResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Teams.EnterpriseTeamsGetResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun enterpriseTeamsDelete(enterprise: String, teamSlug: String): Enterprises.Teams.EnterpriseTeamsDeleteResult {
        val response = client.delete("/enterprises/$enterprise/teams/$teamSlug")
        return when (response.status) {
            HttpStatusCode.NoContent -> Enterprises.Teams.EnterpriseTeamsDeleteResult.NoContent
            HttpStatusCode.Forbidden -> Enterprises.Teams.EnterpriseTeamsDeleteResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun enterpriseTeamsUpdate(enterprise: String, teamSlug: String, body: Enterprises.Teams.EnterpriseTeamsUpdateBody): Enterprises.Teams.EnterpriseTeamsUpdateResult {
        val response = client.patch("/enterprises/$enterprise/teams/$teamSlug") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Teams.EnterpriseTeamsUpdateResult.OK(response.body())
            HttpStatusCode.Forbidden -> Enterprises.Teams.EnterpriseTeamsUpdateResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesTeamsMemberships(private val client: HttpClient) : Enterprises.Teams.Memberships {
    override val add: Enterprises.Teams.Memberships.Add = KtorEnterprisesTeamsMembershipsAdd(client)

    override val remove: Enterprises.Teams.Memberships.Remove = KtorEnterprisesTeamsMembershipsRemove(client)

    override suspend fun enterpriseTeamMembershipsList(enterprise: String, enterpriseTeam: String, page: Long, perPage: Long): List<SimpleUser> =
        client.get("/enterprises/$enterprise/teams/$enterpriseTeam/memberships") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun enterpriseTeamMembershipsGet(enterprise: String, enterpriseTeam: String, username: String): SimpleUser =
        client.get("/enterprises/$enterprise/teams/$enterpriseTeam/memberships/$username").body()

    override suspend fun enterpriseTeamMembershipsAdd(enterprise: String, enterpriseTeam: String, username: String): SimpleUser =
        client.put("/enterprises/$enterprise/teams/$enterpriseTeam/memberships/$username").body()

    override suspend fun enterpriseTeamMembershipsRemove(enterprise: String, enterpriseTeam: String, username: String): Enterprises.Teams.Memberships.EnterpriseTeamMembershipsRemoveResult {
        val response = client.delete("/enterprises/$enterprise/teams/$enterpriseTeam/memberships/$username")
        return when (response.status) {
            HttpStatusCode.NoContent -> Enterprises.Teams.Memberships.EnterpriseTeamMembershipsRemoveResult.NoContent
            HttpStatusCode.Forbidden -> Enterprises.Teams.Memberships.EnterpriseTeamMembershipsRemoveResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorEnterprisesTeamsMembershipsAdd(private val client: HttpClient) : Enterprises.Teams.Memberships.Add {
    override suspend fun enterpriseTeamMembershipsBulkAdd(enterprise: String, enterpriseTeam: String, body: Enterprises.Teams.Memberships.Add.EnterpriseTeamMembershipsBulkAddBody): List<SimpleUser> =
        client.post("/enterprises/$enterprise/teams/$enterpriseTeam/memberships/add") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorEnterprisesTeamsMembershipsRemove(private val client: HttpClient) : Enterprises.Teams.Memberships.Remove {
    override suspend fun enterpriseTeamMembershipsBulkRemove(enterprise: String, enterpriseTeam: String, body: Enterprises.Teams.Memberships.Remove.EnterpriseTeamMembershipsBulkRemoveBody): List<SimpleUser> =
        client.post("/enterprises/$enterprise/teams/$enterpriseTeam/memberships/remove") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorEnterprisesTeamsOrganizations(private val client: HttpClient) : Enterprises.Teams.Organizations {
    override val add: Enterprises.Teams.Organizations.Add = KtorEnterprisesTeamsOrganizationsAdd(client)

    override val remove: Enterprises.Teams.Organizations.Remove = KtorEnterprisesTeamsOrganizationsRemove(client)

    override suspend fun enterpriseTeamOrganizationsGetAssignments(enterprise: String, enterpriseTeam: String, page: Long, perPage: Long): List<OrganizationSimple> =
        client.get("/enterprises/$enterprise/teams/$enterpriseTeam/organizations") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()

    override suspend fun enterpriseTeamOrganizationsGetAssignment(enterprise: String, enterpriseTeam: String, org: String): Enterprises.Teams.Organizations.EnterpriseTeamOrganizationsGetAssignmentResult {
        val response = client.get("/enterprises/$enterprise/teams/$enterpriseTeam/organizations/$org")
        return when (response.status) {
            HttpStatusCode.OK -> Enterprises.Teams.Organizations.EnterpriseTeamOrganizationsGetAssignmentResult.OK(response.body())
            HttpStatusCode.NotFound -> Enterprises.Teams.Organizations.EnterpriseTeamOrganizationsGetAssignmentResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun enterpriseTeamOrganizationsAdd(enterprise: String, enterpriseTeam: String, org: String): OrganizationSimple =
        client.put("/enterprises/$enterprise/teams/$enterpriseTeam/organizations/$org").body()

    override suspend fun enterpriseTeamOrganizationsDelete(enterprise: String, enterpriseTeam: String, org: String): Unit =
        client.delete("/enterprises/$enterprise/teams/$enterpriseTeam/organizations/$org").body()
}

internal class KtorEnterprisesTeamsOrganizationsAdd(private val client: HttpClient) : Enterprises.Teams.Organizations.Add {
    override suspend fun enterpriseTeamOrganizationsBulkAdd(enterprise: String, enterpriseTeam: String, body: Enterprises.Teams.Organizations.Add.EnterpriseTeamOrganizationsBulkAddBody): List<OrganizationSimple> =
        client.post("/enterprises/$enterprise/teams/$enterpriseTeam/organizations/add") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}

internal class KtorEnterprisesTeamsOrganizationsRemove(private val client: HttpClient) : Enterprises.Teams.Organizations.Remove {
    override suspend fun enterpriseTeamOrganizationsBulkRemove(enterprise: String, enterpriseTeam: String, body: Enterprises.Teams.Organizations.Remove.EnterpriseTeamOrganizationsBulkRemoveBody): Unit =
        client.post("/enterprises/$enterprise/teams/$enterpriseTeam/organizations/remove") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
}
