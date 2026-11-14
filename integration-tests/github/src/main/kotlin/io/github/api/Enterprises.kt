package io.github.api

import io.github.model.ActionsCacheRetentionLimitForEnterprise
import io.github.model.ActionsCacheStorageLimitForEnterprise
import io.github.model.BasicError
import io.github.model.CodeScanningDefaultSetupOptions
import io.github.model.CodeScanningOptions
import io.github.model.CodeSecurityConfiguration
import io.github.model.CodeSecurityConfigurationRepositories
import io.github.model.CodeSecurityDefaultConfigurations
import io.github.model.DependabotAlertWithRepository
import io.github.model.EnterpriseTeam
import io.github.model.OidcCustomPropertyInclusion
import io.github.model.OidcCustomPropertyInclusionInput
import io.github.model.Optional
import io.github.model.OrganizationSimple
import io.github.model.SimpleUser
import io.github.model.ValidationErrorSimple
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class Enterprises internal constructor(
  private val client: HttpClient,
) {
  public fun enterprise(enterprise: String): EnterprisePath = EnterprisePath(client, enterprise)

  public class EnterprisePath internal constructor(
    private val client: HttpClient,
    private val enterprise: String,
  ) {
    public val actions: Actions = Actions(client, enterprise)

    public val codeSecurity: CodeSecurity = CodeSecurity(client, enterprise)

    public val dependabot: Dependabot = Dependabot(client, enterprise)

    public val teams: Teams = Teams(client, enterprise)

    public class Actions internal constructor(
      private val client: HttpClient,
      private val enterprise: String,
    ) {
      public val cache: Cache = Cache(client, enterprise)

      public val oidc: Oidc = Oidc(client, enterprise)

      public class Cache internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
      ) {
        public val retentionLimit: RetentionLimit = RetentionLimit(client, enterprise)

        public val storageLimit: StorageLimit = StorageLimit(client, enterprise)

        public class RetentionLimit internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public val `get`: Get = Get(client, enterprise)

          public val put: Put = Put(client, enterprise)

          public class Get internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/actions/cache/retention-limit")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsCacheRetentionLimitForEnterprise,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
          ) {
            public suspend operator fun invoke(body: ActionsCacheRetentionLimitForEnterprise): Response {
              val response = client.put("enterprises/${this.enterprise.encodeURLParameter()}/actions/cache/retention-limit") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }

        public class StorageLimit internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public val `get`: Get = Get(client, enterprise)

          public val put: Put = Put(client, enterprise)

          public class Get internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/actions/cache/storage-limit")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: ActionsCacheStorageLimitForEnterprise,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
          ) {
            public suspend operator fun invoke(body: ActionsCacheStorageLimitForEnterprise): Response {
              val response = client.put("enterprises/${this.enterprise.encodeURLParameter()}/actions/cache/storage-limit") {
                contentType(ContentType.Application.Json)
                setBody(body)
              }
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }
        }
      }

      public class Oidc internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
      ) {
        public val customization: Customization = Customization(client, enterprise)

        public class Customization internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public val properties: Properties = Properties(client, enterprise)

          public class Properties internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
          ) {
            public val repo: Repo = Repo(client, enterprise)

            public class Repo internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
            ) {
              public val `get`: Get = Get(client, enterprise)

              public val post: Post = Post(client, enterprise)

              public fun customPropertyName(customPropertyName: String): CustomPropertyNamePath = CustomPropertyNamePath(client, enterprise, customPropertyName)

              public class Get internal constructor(
                private val client: HttpClient,
                private val enterprise: String,
              ) {
                public suspend operator fun invoke(): Response {
                  val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/actions/oidc/customization/properties/repo")
                  return when (response.status.value) {
                    200 -> Response.Ok(response.body())
                    403 -> Response.Forbidden(response.body())
                    404 -> Response.NotFound(response.body())
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Ok(
                    public val `value`: List<OidcCustomPropertyInclusion>,
                  ) : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data class NotFound(
                    public val `value`: BasicError,
                  ) : Response
                }
              }

              public class Post internal constructor(
                private val client: HttpClient,
                private val enterprise: String,
              ) {
                public suspend operator fun invoke(body: OidcCustomPropertyInclusionInput): Response {
                  val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/actions/oidc/customization/properties/repo") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                  }
                  return when (response.status.value) {
                    201 -> Response.Created(response.body())
                    400 -> Response.BadRequest
                    403 -> Response.Forbidden(response.body())
                    422 -> Response.UnprocessableEntity
                    else -> throw ResponseException(response, "")
                  }
                }

                public sealed interface Response {
                  public data class Created(
                    public val `value`: OidcCustomPropertyInclusion,
                  ) : Response

                  public data object BadRequest : Response

                  public data class Forbidden(
                    public val `value`: BasicError,
                  ) : Response

                  public data object UnprocessableEntity : Response
                }
              }

              public class CustomPropertyNamePath internal constructor(
                private val client: HttpClient,
                private val enterprise: String,
                private val customPropertyName: String,
              ) {
                public val delete: Delete = Delete(client, enterprise, customPropertyName)

                public class Delete internal constructor(
                  private val client: HttpClient,
                  private val enterprise: String,
                  private val customPropertyName: String,
                ) {
                  public suspend operator fun invoke(): Response {
                    val response = client.delete("enterprises/${this.enterprise.encodeURLParameter()}/actions/oidc/customization/properties/repo/${this.customPropertyName.encodeURLParameter()}")
                    return when (response.status.value) {
                      204 -> Response.NoContent
                      400 -> Response.BadRequest
                      403 -> Response.Forbidden(response.body())
                      404 -> Response.NotFound
                      else -> throw ResponseException(response, "")
                    }
                  }

                  public sealed interface Response {
                    public data object NoContent : Response

                    public data object BadRequest : Response

                    public data class Forbidden(
                      public val `value`: BasicError,
                    ) : Response

                    public data object NotFound : Response
                  }
                }
              }
            }
          }
        }
      }
    }

    public class CodeSecurity internal constructor(
      private val client: HttpClient,
      private val enterprise: String,
    ) {
      public val configurations: Configurations = Configurations(client, enterprise)

      public class Configurations internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
      ) {
        public val `get`: Get = Get(client, enterprise)

        public val post: Post = Post(client, enterprise)

        public val defaults: Defaults = Defaults(client, enterprise)

        public fun configurationId(configurationId: Long): ConfigurationIdPath = ConfigurationIdPath(client, enterprise, configurationId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public suspend operator fun invoke(
            perPage: Long? = 30L,
            before: String? = null,
            after: String? = null,
          ): Response {
            val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations") {
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<CodeSecurityConfiguration>,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public suspend operator fun invoke(
            name: String,
            description: String,
            advancedSecurity: AdvancedSecurity? = null,
            codeSecurity: CodeSecurity? = null,
            dependencyGraph: DependencyGraph? = null,
            dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
            dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
            dependabotAlerts: DependabotAlerts? = null,
            dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
            codeScanningOptions: Optional<CodeScanningOptions> = Optional.Absent,
            codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
            codeScanningDefaultSetupOptions: Optional<CodeScanningDefaultSetupOptions> = Optional.Absent,
            codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
            secretProtection: SecretProtection? = null,
            secretScanning: SecretScanning? = null,
            secretScanningPushProtection: SecretScanningPushProtection? = null,
            secretScanningValidityChecks: SecretScanningValidityChecks? = null,
            secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
            secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
            secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
            secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
            privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
            enforcement: Enforcement? = null,
          ): Response {
            val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, description = description, advancedSecurity = advancedSecurity, codeSecurity = codeSecurity, dependencyGraph = dependencyGraph, dependencyGraphAutosubmitAction = dependencyGraphAutosubmitAction, dependencyGraphAutosubmitActionOptions = dependencyGraphAutosubmitActionOptions, dependabotAlerts = dependabotAlerts, dependabotSecurityUpdates = dependabotSecurityUpdates, codeScanningOptions = codeScanningOptions, codeScanningDefaultSetup = codeScanningDefaultSetup, codeScanningDefaultSetupOptions = codeScanningDefaultSetupOptions, codeScanningDelegatedAlertDismissal = codeScanningDelegatedAlertDismissal, secretProtection = secretProtection, secretScanning = secretScanning, secretScanningPushProtection = secretScanningPushProtection, secretScanningValidityChecks = secretScanningValidityChecks, secretScanningNonProviderPatterns = secretScanningNonProviderPatterns, secretScanningGenericSecrets = secretScanningGenericSecrets, secretScanningDelegatedAlertDismissal = secretScanningDelegatedAlertDismissal, secretScanningExtendedMetadata = secretScanningExtendedMetadata, privateVulnerabilityReporting = privateVulnerabilityReporting, enforcement = enforcement))
            }
            return when (response.status.value) {
              201 -> Response.Created(response.body())
              400 -> Response.BadRequest(response.body())
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class AdvancedSecurity(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("code_security")
            CodeSecurity("code_security"),
            @SerialName("secret_protection")
            SecretProtection("secret_protection"),
            ;
          }

          @Serializable
          public enum class CodeSecurity(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependencyGraph(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependencyGraphAutosubmitAction(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          /**
           * Feature options for Automatic dependency submission
           */
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class DependencyGraphAutosubmitActionOptions(
            @SerialName("labeled_runners")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val labeledRunners: Boolean? = null,
          )

          @Serializable
          public enum class DependabotAlerts(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class DependabotSecurityUpdates(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class CodeScanningDefaultSetup(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class CodeScanningDelegatedAlertDismissal(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretProtection(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanning(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningPushProtection(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningValidityChecks(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningNonProviderPatterns(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningGenericSecrets(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningDelegatedAlertDismissal(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class SecretScanningExtendedMetadata(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class PrivateVulnerabilityReporting(
            public val `value`: String,
          ) {
            @SerialName("enabled")
            Enabled("enabled"),
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("not_set")
            NotSet("not_set"),
            ;
          }

          @Serializable
          public enum class Enforcement(
            public val `value`: String,
          ) {
            @SerialName("enforced")
            Enforced("enforced"),
            @SerialName("unenforced")
            Unenforced("unenforced"),
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            public val name: String,
            public val description: String,
            @SerialName("advanced_security")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val advancedSecurity: AdvancedSecurity? = null,
            @SerialName("code_security")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val codeSecurity: CodeSecurity? = null,
            @SerialName("dependency_graph")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dependencyGraph: DependencyGraph? = null,
            @SerialName("dependency_graph_autosubmit_action")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
            @SerialName("dependency_graph_autosubmit_action_options")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dependencyGraphAutosubmitActionOptions:
                DependencyGraphAutosubmitActionOptions? = null,
            @SerialName("dependabot_alerts")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dependabotAlerts: DependabotAlerts? = null,
            @SerialName("dependabot_security_updates")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
            @SerialName("code_scanning_options")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val codeScanningOptions: Optional<CodeScanningOptions> = Optional.Absent,
            @SerialName("code_scanning_default_setup")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
            @SerialName("code_scanning_default_setup_options")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val codeScanningDefaultSetupOptions:
                Optional<CodeScanningDefaultSetupOptions> = Optional.Absent,
            @SerialName("code_scanning_delegated_alert_dismissal")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val codeScanningDelegatedAlertDismissal:
                CodeScanningDelegatedAlertDismissal? = null,
            @SerialName("secret_protection")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretProtection: SecretProtection? = null,
            @SerialName("secret_scanning")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanning: SecretScanning? = null,
            @SerialName("secret_scanning_push_protection")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanningPushProtection: SecretScanningPushProtection? = null,
            @SerialName("secret_scanning_validity_checks")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
            @SerialName("secret_scanning_non_provider_patterns")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
            @SerialName("secret_scanning_generic_secrets")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
            @SerialName("secret_scanning_delegated_alert_dismissal")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanningDelegatedAlertDismissal:
                SecretScanningDelegatedAlertDismissal? = null,
            @SerialName("secret_scanning_extended_metadata")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
            @SerialName("private_vulnerability_reporting")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val enforcement: Enforcement? = null,
          )

          public sealed interface Response {
            public data class Created(
              public val `value`: CodeSecurityConfiguration,
            ) : Response

            public data class BadRequest(
              public val `value`: BasicError,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Defaults internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public val `get`: Get = Get(client, enterprise)

          public class Get internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
          ) {
            public suspend operator fun invoke(): CodeSecurityDefaultConfigurations {
              val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/defaults")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class ConfigurationIdPath internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
          private val configurationId: Long,
        ) {
          public val delete: Delete = Delete(client, enterprise, configurationId)

          public val `get`: Get = Get(client, enterprise, configurationId)

          public val patch: Patch = Patch(client, enterprise, configurationId)

          public val attach: Attach = Attach(client, enterprise, configurationId)

          public val defaults: Defaults = Defaults(client, enterprise, configurationId)

          public val repositories: Repositories = Repositories(client, enterprise, configurationId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val configurationId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.delete("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/${this.configurationId}")
              return when (response.status.value) {
                204 -> Response.NoContent
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data object NoContent : Response

              public data class BadRequest(
                public val `value`: BasicError,
              ) : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Get internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val configurationId: Long,
          ) {
            public suspend operator fun invoke(): Response {
              val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/${this.configurationId}")
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeSecurityConfiguration,
              ) : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Patch internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val configurationId: Long,
          ) {
            public suspend operator fun invoke(
              name: String? = null,
              description: String? = null,
              advancedSecurity: AdvancedSecurity? = null,
              codeSecurity: CodeSecurity? = null,
              dependencyGraph: DependencyGraph? = null,
              dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
              dependencyGraphAutosubmitActionOptions: DependencyGraphAutosubmitActionOptions? = null,
              dependabotAlerts: DependabotAlerts? = null,
              dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
              codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
              codeScanningDefaultSetupOptions: Optional<CodeScanningDefaultSetupOptions> = Optional.Absent,
              codeScanningOptions: Optional<CodeScanningOptions> = Optional.Absent,
              codeScanningDelegatedAlertDismissal: CodeScanningDelegatedAlertDismissal? = null,
              secretProtection: SecretProtection? = null,
              secretScanning: SecretScanning? = null,
              secretScanningPushProtection: SecretScanningPushProtection? = null,
              secretScanningValidityChecks: SecretScanningValidityChecks? = null,
              secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
              secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
              secretScanningDelegatedAlertDismissal: SecretScanningDelegatedAlertDismissal? = null,
              secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
              privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
              enforcement: Enforcement? = null,
            ): Response {
              val response = client.patch("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/${this.configurationId}") {
                contentType(ContentType.Application.Json)
                setBody(Body(name = name, description = description, advancedSecurity = advancedSecurity, codeSecurity = codeSecurity, dependencyGraph = dependencyGraph, dependencyGraphAutosubmitAction = dependencyGraphAutosubmitAction, dependencyGraphAutosubmitActionOptions = dependencyGraphAutosubmitActionOptions, dependabotAlerts = dependabotAlerts, dependabotSecurityUpdates = dependabotSecurityUpdates, codeScanningDefaultSetup = codeScanningDefaultSetup, codeScanningDefaultSetupOptions = codeScanningDefaultSetupOptions, codeScanningOptions = codeScanningOptions, codeScanningDelegatedAlertDismissal = codeScanningDelegatedAlertDismissal, secretProtection = secretProtection, secretScanning = secretScanning, secretScanningPushProtection = secretScanningPushProtection, secretScanningValidityChecks = secretScanningValidityChecks, secretScanningNonProviderPatterns = secretScanningNonProviderPatterns, secretScanningGenericSecrets = secretScanningGenericSecrets, secretScanningDelegatedAlertDismissal = secretScanningDelegatedAlertDismissal, secretScanningExtendedMetadata = secretScanningExtendedMetadata, privateVulnerabilityReporting = privateVulnerabilityReporting, enforcement = enforcement))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                304 -> Response.NotModified
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                409 -> Response.Conflict(response.body())
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public enum class AdvancedSecurity(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("code_security")
              CodeSecurity("code_security"),
              @SerialName("secret_protection")
              SecretProtection("secret_protection"),
              ;
            }

            @Serializable
            public enum class CodeSecurity(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependencyGraph(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependencyGraphAutosubmitAction(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            /**
             * Feature options for Automatic dependency submission
             */
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class DependencyGraphAutosubmitActionOptions(
              @SerialName("labeled_runners")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val labeledRunners: Boolean? = null,
            )

            @Serializable
            public enum class DependabotAlerts(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class DependabotSecurityUpdates(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class CodeScanningDefaultSetup(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class CodeScanningDelegatedAlertDismissal(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretProtection(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanning(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningPushProtection(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningValidityChecks(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningNonProviderPatterns(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningGenericSecrets(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningDelegatedAlertDismissal(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class SecretScanningExtendedMetadata(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class PrivateVulnerabilityReporting(
              public val `value`: String,
            ) {
              @SerialName("enabled")
              Enabled("enabled"),
              @SerialName("disabled")
              Disabled("disabled"),
              @SerialName("not_set")
              NotSet("not_set"),
              ;
            }

            @Serializable
            public enum class Enforcement(
              public val `value`: String,
            ) {
              @SerialName("enforced")
              Enforced("enforced"),
              @SerialName("unenforced")
              Unenforced("unenforced"),
              ;
            }

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            internal data class Body(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val name: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val description: String? = null,
              @SerialName("advanced_security")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val advancedSecurity: AdvancedSecurity? = null,
              @SerialName("code_security")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val codeSecurity: CodeSecurity? = null,
              @SerialName("dependency_graph")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val dependencyGraph: DependencyGraph? = null,
              @SerialName("dependency_graph_autosubmit_action")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val dependencyGraphAutosubmitAction: DependencyGraphAutosubmitAction? = null,
              @SerialName("dependency_graph_autosubmit_action_options")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val dependencyGraphAutosubmitActionOptions:
                  DependencyGraphAutosubmitActionOptions? = null,
              @SerialName("dependabot_alerts")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val dependabotAlerts: DependabotAlerts? = null,
              @SerialName("dependabot_security_updates")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val dependabotSecurityUpdates: DependabotSecurityUpdates? = null,
              @SerialName("code_scanning_default_setup")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val codeScanningDefaultSetup: CodeScanningDefaultSetup? = null,
              @SerialName("code_scanning_default_setup_options")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val codeScanningDefaultSetupOptions:
                  Optional<CodeScanningDefaultSetupOptions> = Optional.Absent,
              @SerialName("code_scanning_options")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val codeScanningOptions: Optional<CodeScanningOptions> = Optional.Absent,
              @SerialName("code_scanning_delegated_alert_dismissal")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val codeScanningDelegatedAlertDismissal:
                  CodeScanningDelegatedAlertDismissal? = null,
              @SerialName("secret_protection")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretProtection: SecretProtection? = null,
              @SerialName("secret_scanning")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanning: SecretScanning? = null,
              @SerialName("secret_scanning_push_protection")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanningPushProtection: SecretScanningPushProtection? = null,
              @SerialName("secret_scanning_validity_checks")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanningValidityChecks: SecretScanningValidityChecks? = null,
              @SerialName("secret_scanning_non_provider_patterns")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanningNonProviderPatterns:
                  SecretScanningNonProviderPatterns? = null,
              @SerialName("secret_scanning_generic_secrets")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanningGenericSecrets: SecretScanningGenericSecrets? = null,
              @SerialName("secret_scanning_delegated_alert_dismissal")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanningDelegatedAlertDismissal:
                  SecretScanningDelegatedAlertDismissal? = null,
              @SerialName("secret_scanning_extended_metadata")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val secretScanningExtendedMetadata: SecretScanningExtendedMetadata? = null,
              @SerialName("private_vulnerability_reporting")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val privateVulnerabilityReporting: PrivateVulnerabilityReporting? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val enforcement: Enforcement? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: CodeSecurityConfiguration,
              ) : Response

              public data object NotModified : Response

              public data class Forbidden(
                public val `value`: BasicError,
              ) : Response

              public data class NotFound(
                public val `value`: BasicError,
              ) : Response

              public data class Conflict(
                public val `value`: BasicError,
              ) : Response
            }
          }

          public class Attach internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val configurationId: Long,
          ) {
            public val post: Post = Post(client, enterprise, configurationId)

            public class Post internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val configurationId: Long,
            ) {
              public suspend operator fun invoke(scope: Scope): Response {
                val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/${this.configurationId}/attach") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(scope = scope))
                }
                return when (response.status.value) {
                  202 -> Response.Accepted(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  409 -> Response.Conflict(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class Scope(
                public val `value`: String,
              ) {
                @SerialName("all")
                All("all"),
                @SerialName("all_without_configurations")
                AllWithoutConfigurations("all_without_configurations"),
                ;
              }

              @Serializable
              internal data class Body(
                public val scope: Scope,
              )

              public sealed interface Response {
                public data class Accepted(
                  public val `value`: JsonElement,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class Conflict(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Defaults internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val configurationId: Long,
          ) {
            public val put: Put = Put(client, enterprise, configurationId)

            public class Put internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val configurationId: Long,
            ) {
              public suspend operator fun invoke(defaultForNewRepos: DefaultForNewRepos? = null): Response {
                val response = client.put("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/${this.configurationId}/defaults") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(defaultForNewRepos = defaultForNewRepos))
                }
                return when (response.status.value) {
                  200 -> response.body<Response.Ok>()
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              @Serializable
              public enum class DefaultForNewRepos(
                public val `value`: String,
              ) {
                @SerialName("all")
                All("all"),
                @SerialName("none")
                None("none"),
                @SerialName("private_and_internal")
                PrivateAndInternal("private_and_internal"),
                @SerialName("public")
                Public("public"),
                ;
              }

              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              internal data class Body(
                @SerialName("default_for_new_repos")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val defaultForNewRepos: DefaultForNewRepos? = null,
              )

              public sealed interface Response {
                @OptIn(ExperimentalSerializationApi::class)
                @Serializable
                public data class Ok(
                  @SerialName("default_for_new_repos")
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val defaultForNewRepos: DefaultForNewRepos? = null,
                  @EncodeDefault(EncodeDefault.Mode.NEVER)
                  public val configuration: CodeSecurityConfiguration? = null,
                ) : Response {
                  @Serializable
                  public enum class DefaultForNewRepos(
                    public val `value`: String,
                  ) {
                    @SerialName("all")
                    All("all"),
                    @SerialName("none")
                    None("none"),
                    @SerialName("private_and_internal")
                    PrivateAndInternal("private_and_internal"),
                    @SerialName("public")
                    Public("public"),
                    ;
                  }
                }

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }

          public class Repositories internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val configurationId: Long,
          ) {
            public val `get`: Get = Get(client, enterprise, configurationId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val configurationId: Long,
            ) {
              public suspend operator fun invoke(
                perPage: Long? = 30L,
                before: String? = null,
                after: String? = null,
                status: String? = "all",
              ): Response {
                val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/code-security/configurations/${this.configurationId}/repositories") {
                  if (perPage != null) {
                    this.url.encodedParameters.append("per_page", perPage.toString())
                  }
                  if (before != null) {
                    this.url.encodedParameters.append("before", before.encodeURLParameter())
                  }
                  if (after != null) {
                    this.url.encodedParameters.append("after", after.encodeURLParameter())
                  }
                  if (status != null) {
                    this.url.encodedParameters.append("status", status.encodeURLParameter())
                  }
                }
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: List<CodeSecurityConfigurationRepositories>,
                ) : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response
              }
            }
          }
        }
      }
    }

    public class Dependabot internal constructor(
      private val client: HttpClient,
      private val enterprise: String,
    ) {
      public val alerts: Alerts = Alerts(client, enterprise)

      public class Alerts internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
      ) {
        public val `get`: Get = Get(client, enterprise)

        public class Get internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
        ) {
          public suspend operator fun invoke(
            state: String? = null,
            severity: String? = null,
            ecosystem: String? = null,
            `package`: String? = null,
            epssPercentage: String? = null,
            has: Has? = null,
            assignee: String? = null,
            scope: Scope? = null,
            sort: Sort? = Sort.Created,
            direction: Direction? = Direction.Desc,
            before: String? = null,
            after: String? = null,
            perPage: Long? = 30L,
          ): Response {
            val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/dependabot/alerts") {
              if (state != null) {
                this.url.encodedParameters.append("state", state.encodeURLParameter())
              }
              if (severity != null) {
                this.url.encodedParameters.append("severity", severity.encodeURLParameter())
              }
              if (ecosystem != null) {
                this.url.encodedParameters.append("ecosystem", ecosystem.encodeURLParameter())
              }
              if (`package` != null) {
                this.url.encodedParameters.append("package", `package`.encodeURLParameter())
              }
              if (epssPercentage != null) {
                this.url.encodedParameters.append("epss_percentage", epssPercentage.encodeURLParameter())
              }
              if (has != null) {
                when (has) {
                  is Has.CaseString -> {
                    this.url.encodedParameters.append("has", has.value.encodeURLParameter())
                  }
                  is Has.CasePatchList -> {
                    has.value.forEach { this.url.encodedParameters.append("has", it.value.encodeURLParameter()) }
                  }
                }
              }
              if (assignee != null) {
                this.url.encodedParameters.append("assignee", assignee.encodeURLParameter())
              }
              if (scope != null) {
                this.url.encodedParameters.append("scope", scope.value.encodeURLParameter())
              }
              if (sort != null) {
                this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
              }
              if (direction != null) {
                this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
              }
              if (before != null) {
                this.url.encodedParameters.append("before", before.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (perPage != null) {
                this.url.encodedParameters.append("per_page", perPage.toString())
              }
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              422 -> Response.UnprocessableEntity(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable(with = Has.Serializer::class)
          public sealed interface Has {
            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Has

            @Serializable
            @JvmInline
            public value class CasePatchList(
              public val `value`: List<Patch>,
            ) : Has

            @Serializable
            public enum class Patch(
              public val `value`: String,
            ) {
              @SerialName("patch")
              Patch("patch"),
              ;
            }

            public object Serializer : KSerializer<Has> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.github.api.Enterprises.EnterprisePath.Dependabot.Alerts.Get.Has", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CasePatchList", ListSerializer(Patch.serializer()).descriptor)
              }

              override fun deserialize(decoder: Decoder): Has {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                  value is JsonArray -> json.attemptDeserialize(
                    value,
                    CasePatchList::class to { CasePatchList(decodeFromJsonElement(ListSerializer(Patch.serializer()), it)) },
                  )
                  else -> throw UnionSerializationException(value, emptyMap())
                }
              }

              override fun serialize(encoder: Encoder, `value`: Has) {
                when(value) {
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  is CasePatchList -> encoder.encodeSerializableValue(ListSerializer(Patch.serializer()), value.value)
                }
              }
            }
          }

          @Serializable
          public enum class Scope(
            public val `value`: String,
          ) {
            @SerialName("development")
            Development("development"),
            @SerialName("runtime")
            Runtime("runtime"),
            ;
          }

          @Serializable
          public enum class Sort(
            public val `value`: String,
          ) {
            @SerialName("created")
            Created("created"),
            @SerialName("updated")
            Updated("updated"),
            @SerialName("epss_percentage")
            EpssPercentage("epss_percentage"),
            ;
          }

          @Serializable
          public enum class Direction(
            public val `value`: String,
          ) {
            @SerialName("asc")
            Asc("asc"),
            @SerialName("desc")
            Desc("desc"),
            ;
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: List<DependabotAlertWithRepository>,
            ) : Response

            public data object NotModified : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response

            public data class UnprocessableEntity(
              public val `value`: ValidationErrorSimple,
            ) : Response
          }
        }
      }
    }

    public class Teams internal constructor(
      private val client: HttpClient,
      private val enterprise: String,
    ) {
      public val `get`: Get = Get(client, enterprise)

      public val post: Post = Post(client, enterprise)

      public fun enterpriseTeam(enterpriseTeam: String): EnterpriseTeamPath = EnterpriseTeamPath(client, enterprise, enterpriseTeam)

      public fun teamSlug(teamSlug: String): TeamSlugPath = TeamSlugPath(client, enterprise, teamSlug)

      public class Get internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/teams") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            403 -> Response.Forbidden(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<EnterpriseTeam>,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
      ) {
        public suspend operator fun invoke(
          name: String,
          description: Optional<String> = Optional.Absent,
          syncToOrganizations: SyncToOrganizations? = null,
          organizationSelectionType: OrganizationSelectionType? = null,
          groupId: Optional<String> = Optional.Absent,
        ): EnterpriseTeam {
          val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/teams") {
            contentType(ContentType.Application.Json)
            setBody(Body(name = name, description = description, syncToOrganizations = syncToOrganizations, organizationSelectionType = organizationSelectionType, groupId = groupId))
          }
          if (response.status.value != 201) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        public enum class SyncToOrganizations(
          public val `value`: String,
        ) {
          @SerialName("all")
          All("all"),
          @SerialName("disabled")
          Disabled("disabled"),
          ;
        }

        @Serializable
        public enum class OrganizationSelectionType(
          public val `value`: String,
        ) {
          @SerialName("disabled")
          Disabled("disabled"),
          @SerialName("selected")
          Selected("selected"),
          @SerialName("all")
          All("all"),
          ;
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        internal data class Body(
          public val name: String,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val description: Optional<String> = Optional.Absent,
          @SerialName("sync_to_organizations")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val syncToOrganizations: SyncToOrganizations? = null,
          @SerialName("organization_selection_type")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val organizationSelectionType: OrganizationSelectionType? = null,
          @SerialName("group_id")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val groupId: Optional<String> = Optional.Absent,
        )
      }

      public class EnterpriseTeamPath internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
        private val enterpriseTeam: String,
      ) {
        public val memberships: Memberships = Memberships(client, enterprise, enterpriseTeam)

        public val organizations: Organizations = Organizations(client, enterprise, enterpriseTeam)

        public class Memberships internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
          private val enterpriseTeam: String,
        ) {
          public val `get`: Get = Get(client, enterprise, enterpriseTeam)

          public val add: Add = Add(client, enterprise, enterpriseTeam)

          public val remove: Remove = Remove(client, enterprise, enterpriseTeam)

          public fun username(username: String): UsernamePath = UsernamePath(client, enterprise, enterpriseTeam, username)

          public class Get internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<SimpleUser> {
              val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/memberships") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Add internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
          ) {
            public val post: Post = Post(client, enterprise, enterpriseTeam)

            public class Post internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
            ) {
              public suspend operator fun invoke(usernames: List<String>): List<SimpleUser> {
                val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/memberships/add") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(usernames = usernames))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              internal data class Body(
                public val usernames: List<String>,
              )
            }
          }

          public class Remove internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
          ) {
            public val post: Post = Post(client, enterprise, enterpriseTeam)

            public class Post internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
            ) {
              public suspend operator fun invoke(usernames: List<String>): List<SimpleUser> {
                val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/memberships/remove") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(usernames = usernames))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              internal data class Body(
                public val usernames: List<String>,
              )
            }
          }

          public class UsernamePath internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
            private val username: String,
          ) {
            public val delete: Delete = Delete(client, enterprise, enterpriseTeam, username)

            public val `get`: Get = Get(client, enterprise, enterpriseTeam, username)

            public val put: Put = Put(client, enterprise, enterpriseTeam, username)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/memberships/${this.username.encodeURLParameter()}")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  403 -> Response.Forbidden(response.body())
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(): SimpleUser {
                val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/memberships/${this.username.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
              private val username: String,
            ) {
              public suspend operator fun invoke(): SimpleUser {
                val response = client.put("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/memberships/${this.username.encodeURLParameter()}")
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }

        public class Organizations internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
          private val enterpriseTeam: String,
        ) {
          public val `get`: Get = Get(client, enterprise, enterpriseTeam)

          public val add: Add = Add(client, enterprise, enterpriseTeam)

          public val remove: Remove = Remove(client, enterprise, enterpriseTeam)

          public fun org(org: String): OrgPath = OrgPath(client, enterprise, enterpriseTeam, org)

          public class Get internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
          ) {
            public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): List<OrganizationSimple> {
              val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/organizations") {
                if (perPage != null) {
                  this.url.encodedParameters.append("per_page", perPage.toString())
                }
                if (page != null) {
                  this.url.encodedParameters.append("page", page.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Add internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
          ) {
            public val post: Post = Post(client, enterprise, enterpriseTeam)

            public class Post internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
            ) {
              public suspend operator fun invoke(organizationSlugs: List<String>): List<OrganizationSimple> {
                val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/organizations/add") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(organizationSlugs = organizationSlugs))
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              internal data class Body(
                @SerialName("organization_slugs")
                public val organizationSlugs: List<String>,
              )
            }
          }

          public class Remove internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
          ) {
            public val post: Post = Post(client, enterprise, enterpriseTeam)

            public class Post internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
            ) {
              public suspend operator fun invoke(organizationSlugs: List<String>) {
                val response = client.post("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/organizations/remove") {
                  contentType(ContentType.Application.Json)
                  setBody(Body(organizationSlugs = organizationSlugs))
                }
                if (response.status.value != 204) throw ResponseException(response, "")
              }

              @Serializable
              internal data class Body(
                @SerialName("organization_slugs")
                public val organizationSlugs: List<String>,
              )
            }
          }

          public class OrgPath internal constructor(
            private val client: HttpClient,
            private val enterprise: String,
            private val enterpriseTeam: String,
            private val org: String,
          ) {
            public val delete: Delete = Delete(client, enterprise, enterpriseTeam, org)

            public val `get`: Get = Get(client, enterprise, enterpriseTeam, org)

            public val put: Put = Put(client, enterprise, enterpriseTeam, org)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
              private val org: String,
            ) {
              public suspend operator fun invoke() {
                val response = client.delete("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/organizations/${this.org.encodeURLParameter()}")
                if (response.status.value != 204) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
              private val org: String,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/organizations/${this.org.encodeURLParameter()}")
                return when (response.status.value) {
                  200 -> Response.Ok(response.body())
                  404 -> Response.NotFound
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data class Ok(
                  public val `value`: OrganizationSimple,
                ) : Response

                public data object NotFound : Response
              }
            }

            public class Put internal constructor(
              private val client: HttpClient,
              private val enterprise: String,
              private val enterpriseTeam: String,
              private val org: String,
            ) {
              public suspend operator fun invoke(): OrganizationSimple {
                val response = client.put("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.enterpriseTeam.encodeURLParameter()}/organizations/${this.org.encodeURLParameter()}")
                if (response.status.value != 201) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }

      public class TeamSlugPath internal constructor(
        private val client: HttpClient,
        private val enterprise: String,
        private val teamSlug: String,
      ) {
        public val delete: Delete = Delete(client, enterprise, teamSlug)

        public val `get`: Get = Get(client, enterprise, teamSlug)

        public val patch: Patch = Patch(client, enterprise, teamSlug)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
          private val teamSlug: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.teamSlug.encodeURLParameter()}")
            return when (response.status.value) {
              204 -> Response.NoContent
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
          private val teamSlug: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.teamSlug.encodeURLParameter()}")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: EnterpriseTeam,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val enterprise: String,
          private val teamSlug: String,
        ) {
          public suspend operator fun invoke(
            name: Optional<String> = Optional.Absent,
            description: Optional<String> = Optional.Absent,
            syncToOrganizations: SyncToOrganizations? = null,
            organizationSelectionType: OrganizationSelectionType? = null,
            groupId: Optional<String> = Optional.Absent,
          ): Response {
            val response = client.patch("enterprises/${this.enterprise.encodeURLParameter()}/teams/${this.teamSlug.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(Body(name = name, description = description, syncToOrganizations = syncToOrganizations, organizationSelectionType = organizationSelectionType, groupId = groupId))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              403 -> Response.Forbidden(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          public enum class SyncToOrganizations(
            public val `value`: String,
          ) {
            @SerialName("all")
            All("all"),
            @SerialName("disabled")
            Disabled("disabled"),
            ;
          }

          @Serializable
          public enum class OrganizationSelectionType(
            public val `value`: String,
          ) {
            @SerialName("disabled")
            Disabled("disabled"),
            @SerialName("selected")
            Selected("selected"),
            @SerialName("all")
            All("all"),
            ;
          }

          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          internal data class Body(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val name: Optional<String> = Optional.Absent,
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val description: Optional<String> = Optional.Absent,
            @SerialName("sync_to_organizations")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val syncToOrganizations: SyncToOrganizations? = null,
            @SerialName("organization_selection_type")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val organizationSelectionType: OrganizationSelectionType? = null,
            @SerialName("group_id")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val groupId: Optional<String> = Optional.Absent,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: EnterpriseTeam,
            ) : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }
  }
}
