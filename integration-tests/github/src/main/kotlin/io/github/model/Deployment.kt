package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

/**
 * A request for a specific ref(branch,sha,tag) to be deployed
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Deployment(
  public val url: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val sha: String,
  public val ref: String,
  public val task: String,
  public val payload: Payload,
  @SerialName("original_environment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val originalEnvironment: String? = null,
  public val environment: String,
  public val description: String?,
  public val creator: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("statuses_url")
  public val statusesUrl: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("transient_environment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val transientEnvironment: Boolean? = null,
  @SerialName("production_environment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val productionEnvironment: Boolean? = null,
  @SerialName("performed_via_github_app")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val performedViaGithubApp: Optional<NullableIntegration> = Optional.Absent,
) {
  @Serializable(with = Payload.Serializer::class)
  public sealed interface Payload {
    @Serializable
    @JvmInline
    public value class CaseJsonElement(
      public val `value`: JsonElement,
    ) : Payload

    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Payload

    public object Serializer : KSerializer<Payload> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Deployment.Payload", PolymorphicKind.SEALED) {
        element("CaseJsonElement", JsonElement.serializer().descriptor)
        element("CaseString", String.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Payload {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
          )
          else -> json.attemptDeserialize(
            value,
            CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
          )
        }
      }

      override fun serialize(encoder: Encoder, `value`: Payload) {
        when(value) {
          is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
      }
    }
  }
}
