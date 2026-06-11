package io.github.nomisrev.render.test.union.protection.rules

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
import kotlinx.serialization.json.jsonObject

@Serializable(with = ProtectionRules.Serializer::class)
public sealed interface ProtectionRules {
  @Serializable
  public data class IdAndNodeIdAndTypeAndWaitTimer(
    public val id: Int,
    @SerialName("node_id")
    public val nodeId: String,
    public val type: String,
    @SerialName("wait_timer")
    public val waitTimer: Int? = null,
  ) : ProtectionRules

  @Serializable
  public data class IdAndNodeIdAndTypeAndPreventSelfReviewAndReviewers(
    public val id: Int,
    @SerialName("node_id")
    public val nodeId: String,
    public val type: String,
    @SerialName("prevent_self_review")
    public val preventSelfReview: Boolean? = null,
    public val reviewers: List<String>? = null,
  ) : ProtectionRules

  @Serializable
  public data class IdAndNodeIdAndType(
    public val id: Int,
    @SerialName("node_id")
    public val nodeId: String,
    public val type: String,
  ) : ProtectionRules

  public object Serializer : KSerializer<ProtectionRules> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.protection.rules.ProtectionRules", PolymorphicKind.SEALED) {
      element("IdAndNodeIdAndTypeAndWaitTimer", IdAndNodeIdAndTypeAndWaitTimer.serializer().descriptor)
      element("IdAndNodeIdAndTypeAndPreventSelfReviewAndReviewers", IdAndNodeIdAndTypeAndPreventSelfReviewAndReviewers.serializer().descriptor)
      element("IdAndNodeIdAndType", IdAndNodeIdAndType.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ProtectionRules {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val keys = value.jsonObject.keys
      if ("prevent_self_review" in keys || "reviewers" in keys) {
        return json.decodeFromJsonElement(IdAndNodeIdAndTypeAndPreventSelfReviewAndReviewers.serializer(), value)
      }
      if ("wait_timer" in keys) {
        return json.decodeFromJsonElement(IdAndNodeIdAndTypeAndWaitTimer.serializer(), value)
      }
      return json.decodeFromJsonElement(IdAndNodeIdAndType.serializer(), value)
    }

    override fun serialize(encoder: Encoder, `value`: ProtectionRules) {
      when(value) {
        is IdAndNodeIdAndTypeAndWaitTimer -> encoder.encodeSerializableValue(IdAndNodeIdAndTypeAndWaitTimer.serializer(), value)
        is IdAndNodeIdAndTypeAndPreventSelfReviewAndReviewers -> encoder.encodeSerializableValue(IdAndNodeIdAndTypeAndPreventSelfReviewAndReviewers.serializer(), value)
        is IdAndNodeIdAndType -> encoder.encodeSerializableValue(IdAndNodeIdAndType.serializer(), value)
      }
    }
  }
}
