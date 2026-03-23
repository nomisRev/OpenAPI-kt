package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Installation
 */
@Serializable
public data class Installation(
  public val id: Long,
  public val account: Account?,
  @SerialName("repository_selection")
  public val repositorySelection: RepositorySelection,
  @SerialName("access_tokens_url")
  public val accessTokensUrl: String,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("app_id")
  public val appId: Long,
  @SerialName("client_id")
  public val clientId: String? = null,
  @SerialName("target_id")
  public val targetId: Long,
  @SerialName("target_type")
  public val targetType: String,
  public val permissions: AppPermissions,
  public val events: List<String>,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("single_file_name")
  public val singleFileName: String?,
  @SerialName("has_multiple_single_files")
  public val hasMultipleSingleFiles: Boolean? = null,
  @SerialName("single_file_paths")
  public val singleFilePaths: List<String>? = null,
  @SerialName("app_slug")
  public val appSlug: String,
  @SerialName("suspended_by")
  public val suspendedBy: NullableSimpleUser?,
  @SerialName("suspended_at")
  public val suspendedAt: Instant?,
  @SerialName("contact_email")
  public val contactEmail: String? = null,
) {
  @Serializable(with = Account.Serializer::class)
  public sealed interface Account {
    @Serializable
    @JvmInline
    public value class CaseSimpleUser(
      public val `value`: SimpleUser,
    ) : Account

    @Serializable
    @JvmInline
    public value class CaseEnterprise(
      public val `value`: Enterprise,
    ) : Account

    public object Serializer : KSerializer<Account> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Installation.Account", PolymorphicKind.SEALED) {
        element("CaseSimpleUser", SimpleUser.serializer().descriptor)
        element("CaseEnterprise", Enterprise.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Account {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
          CaseEnterprise::class to { CaseEnterprise(decodeFromJsonElement(Enterprise.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Account) {
        when(value) {
          is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
          is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class RepositorySelection(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
