package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class VectorStoreSearchRequest(
  public val query: Query,
  @SerialName("rewrite_query")
  public val rewriteQuery: Boolean? = null,
  @SerialName("max_num_results")
  public val maxNumResults: Long? = null,
  public val filters: Filters? = null,
  @SerialName("ranking_options")
  public val rankingOptions: RankingOptions? = null,
) {
  /**
   * A filter to apply based on file attributes.
   */
  @Serializable(with = Filters.Serializer::class)
  public sealed interface Filters {
    @Serializable
    @JvmInline
    public value class CaseComparisonFilter(
      public val `value`: ComparisonFilter,
    ) : Filters

    @Serializable
    @JvmInline
    public value class CaseCompoundFilter(
      public val `value`: CompoundFilter,
    ) : Filters

    public object Serializer : KSerializer<Filters> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters", PolymorphicKind.SEALED) {
        element("CaseComparisonFilter", ComparisonFilter.serializer().descriptor)
        element("CaseCompoundFilter", CompoundFilter.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Filters {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseComparisonFilter::class to { CaseComparisonFilter(decodeFromJsonElement(ComparisonFilter.serializer(), it)) },
          CaseCompoundFilter::class to { CaseCompoundFilter(decodeFromJsonElement(CompoundFilter.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Filters) {
        when(value) {
          is CaseComparisonFilter -> encoder.encodeSerializableValue(ComparisonFilter.serializer(), value.value)
          is CaseCompoundFilter -> encoder.encodeSerializableValue(CompoundFilter.serializer(), value.value)
        }
      }
    }
  }

  /**
   * A query string for a search
   */
  @Serializable(with = Query.Serializer::class)
  public sealed interface Query {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Query

    @Serializable
    @JvmInline
    public value class CaseStrings(
      public val `value`: List<String>,
    ) : Query

    public object Serializer : KSerializer<Query> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Query", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Query {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Query) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
        }
      }
    }
  }

  /**
   * Ranking options for search.
   */
  @Serializable
  public data class RankingOptions(
    public val ranker: Ranker? = null,
    @SerialName("score_threshold")
    public val scoreThreshold: Double? = null,
  ) {
    @Serializable
    public enum class Ranker(
      public val `value`: String,
    ) {
      @SerialName("none")
      None("none"),
      @SerialName("auto")
      Auto("auto"),
      @SerialName("default-2024-11-15")
      Default20241115("default-2024-11-15"),
      ;
    }
  }
}
