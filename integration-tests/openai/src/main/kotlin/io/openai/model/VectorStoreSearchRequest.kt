package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class VectorStoreSearchRequest(
  public val query: Query,
  @SerialName("rewrite_query")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rewriteQuery: Boolean? = null,
  @SerialName("max_num_results")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxNumResults: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val filters: Filters? = null,
  @SerialName("ranking_options")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rankingOptions: RankingOptions? = null,
) {
  /**
   * A filter to apply based on file attributes.
   */
  @Serializable(with = Filters.Serializer::class)
  public sealed interface Filters {
    /**
     * A filter used to compare a specified attribute key to a given value using a defined comparison operation.
     *
     */
    @Serializable
    public data class ComparisonFilter(
      @Required
      public val type: Type = Type.Eq,
      public val key: String,
      public val `value`: Value,
    ) : Filters {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("eq")
        Eq("eq"),
        @SerialName("ne")
        Ne("ne"),
        @SerialName("gt")
        Gt("gt"),
        @SerialName("gte")
        Gte("gte"),
        @SerialName("lt")
        Lt("lt"),
        @SerialName("lte")
        Lte("lte"),
        ;
      }

      /**
       * The value to compare against the attribute key; supports string, number, or boolean types.
       */
      @Serializable(with = Value.Serializer::class)
      public sealed interface Value {
        @Serializable
        @JvmInline
        public value class CaseString(
          public val `value`: String,
        ) : Value

        @Serializable
        @JvmInline
        public value class CaseDouble(
          public val `value`: Double,
        ) : Value

        @Serializable
        @JvmInline
        public value class CaseBoolean(
          public val `value`: Boolean,
        ) : Value

        @Serializable
        @JvmInline
        public value class CaseStringOrDoubleList(
          public val `value`: List<StringOrDouble>,
        ) : Value

        @Serializable(with = StringOrDouble.Serializer::class)
        public sealed interface StringOrDouble {
          @Serializable
          @JvmInline
          public value class CaseString(
            public val `value`: String,
          ) : StringOrDouble

          @Serializable
          @JvmInline
          public value class CaseDouble(
            public val `value`: Double,
          ) : StringOrDouble

          public object Serializer : KSerializer<StringOrDouble> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters.ComparisonFilter.Value.StringOrDouble", PolymorphicKind.SEALED) {
              element("CaseString", String.serializer().descriptor)
              element("CaseDouble", Double.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): StringOrDouble {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return when {
                value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                  value,
                  CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
                value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                  value,
                  CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                )
                else -> throw UnionSerializationException(value, emptyMap())
              }
            }

            override fun serialize(encoder: Encoder, `value`: StringOrDouble) {
              when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
              }
            }
          }
        }

        public object Serializer : KSerializer<Value> {
          @OptIn(
            InternalSerializationApi::class,
            ExperimentalSerializationApi::class,
          )
          override val descriptor: SerialDescriptor =
              buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters.ComparisonFilter.Value", PolymorphicKind.SEALED) {
            element("CaseString", String.serializer().descriptor)
            element("CaseDouble", Double.serializer().descriptor)
            element("CaseBoolean", Boolean.serializer().descriptor)
            element("CaseStringOrDoubleList", ListSerializer(StringOrDouble.serializer()).descriptor)
          }

          override fun deserialize(decoder: Decoder): Value {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return when {
              value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                value,
                CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
              )
              value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                value,
                CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
              )
              value is JsonPrimitive && !value.isString && value.booleanOrNull != null -> json.attemptDeserialize(
                value,
                CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
              )
              value is JsonArray -> json.attemptDeserialize(
                value,
                CaseStringOrDoubleList::class to { CaseStringOrDoubleList(decodeFromJsonElement(ListSerializer(StringOrDouble.serializer()), it)) },
              )
              else -> throw UnionSerializationException(value, emptyMap())
            }
          }

          override fun serialize(encoder: Encoder, `value`: Value) {
            when(value) {
              is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
              is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
              is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
              is CaseStringOrDoubleList -> encoder.encodeSerializableValue(ListSerializer(StringOrDouble.serializer()), value.value)
            }
          }
        }
      }
    }

    /**
     * Combine multiple filters using `and` or `or`.
     */
    @Serializable
    public data class CompoundFilter(
      public val type: Type,
      public val filters: List<Filters>,
    ) : Filters {
      @Serializable(with = Filters.Serializer::class)
      public sealed interface Filters {
        /**
         * A filter used to compare a specified attribute key to a given value using a defined comparison operation.
         *
         */
        @Serializable
        public data class ComparisonFilter(
          @Required
          public val type: Type = Type.Eq,
          public val key: String,
          public val `value`: Value,
        ) : Filters {
          @Serializable
          public enum class Type(
            public val `value`: String,
          ) {
            @SerialName("eq")
            Eq("eq"),
            @SerialName("ne")
            Ne("ne"),
            @SerialName("gt")
            Gt("gt"),
            @SerialName("gte")
            Gte("gte"),
            @SerialName("lt")
            Lt("lt"),
            @SerialName("lte")
            Lte("lte"),
            ;
          }

          /**
           * The value to compare against the attribute key; supports string, number, or boolean types.
           */
          @Serializable(with = Value.Serializer::class)
          public sealed interface Value {
            @Serializable
            @JvmInline
            public value class CaseString(
              public val `value`: String,
            ) : Value

            @Serializable
            @JvmInline
            public value class CaseDouble(
              public val `value`: Double,
            ) : Value

            @Serializable
            @JvmInline
            public value class CaseBoolean(
              public val `value`: Boolean,
            ) : Value

            @Serializable
            @JvmInline
            public value class CaseStringOrDoubleList(
              public val `value`: List<StringOrDouble>,
            ) : Value

            @Serializable(with = StringOrDouble.Serializer::class)
            public sealed interface StringOrDouble {
              @Serializable
              @JvmInline
              public value class CaseString(
                public val `value`: String,
              ) : StringOrDouble

              @Serializable
              @JvmInline
              public value class CaseDouble(
                public val `value`: Double,
              ) : StringOrDouble

              public object Serializer : KSerializer<StringOrDouble> {
                @OptIn(
                  InternalSerializationApi::class,
                  ExperimentalSerializationApi::class,
                )
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters.CompoundFilter.Filters.ComparisonFilter.Value.StringOrDouble", PolymorphicKind.SEALED) {
                  element("CaseString", String.serializer().descriptor)
                  element("CaseDouble", Double.serializer().descriptor)
                }

                override fun deserialize(decoder: Decoder): StringOrDouble {
                  val value = decoder.decodeSerializableValue(JsonElement.serializer())
                  val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                  return when {
                    value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                      value,
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                    value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                      value,
                      CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                    )
                    else -> throw UnionSerializationException(value, emptyMap())
                  }
                }

                override fun serialize(encoder: Encoder, `value`: StringOrDouble) {
                  when(value) {
                    is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                    is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                  }
                }
              }
            }

            public object Serializer : KSerializer<Value> {
              @OptIn(
                InternalSerializationApi::class,
                ExperimentalSerializationApi::class,
              )
              override val descriptor: SerialDescriptor =
                  buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters.CompoundFilter.Filters.ComparisonFilter.Value", PolymorphicKind.SEALED) {
                element("CaseString", String.serializer().descriptor)
                element("CaseDouble", Double.serializer().descriptor)
                element("CaseBoolean", Boolean.serializer().descriptor)
                element("CaseStringOrDoubleList", ListSerializer(StringOrDouble.serializer()).descriptor)
              }

              override fun deserialize(decoder: Decoder): Value {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return when {
                  value is JsonPrimitive && value.isString -> json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                  )
                  value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
                    value,
                    CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                  )
                  value is JsonPrimitive && !value.isString && value.booleanOrNull != null -> json.attemptDeserialize(
                    value,
                    CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
                  )
                  value is JsonArray -> json.attemptDeserialize(
                    value,
                    CaseStringOrDoubleList::class to { CaseStringOrDoubleList(decodeFromJsonElement(ListSerializer(StringOrDouble.serializer()), it)) },
                  )
                  else -> throw UnionSerializationException(value, emptyMap())
                }
              }

              override fun serialize(encoder: Encoder, `value`: Value) {
                when(value) {
                  is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                  is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                  is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
                  is CaseStringOrDoubleList -> encoder.encodeSerializableValue(ListSerializer(StringOrDouble.serializer()), value.value)
                }
              }
            }
          }
        }

        @Serializable
        @JvmInline
        public value class CaseCompoundFilter(
          public val `value`: io.openai.model.CompoundFilter,
        ) : Filters

        public object Serializer : KSerializer<Filters> {
          @OptIn(
            InternalSerializationApi::class,
            ExperimentalSerializationApi::class,
          )
          override val descriptor: SerialDescriptor =
              buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters.CompoundFilter.Filters", PolymorphicKind.SEALED) {
            element("ComparisonFilter", ComparisonFilter.serializer().descriptor)
            element("CaseCompoundFilter", io.openai.model.CompoundFilter.serializer().descriptor)
          }

          override fun deserialize(decoder: Decoder): Filters {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            val obj = value as? JsonObject
            val tag = (obj?.get("type") as? JsonPrimitive)?.content
            when(tag) {
              "eq", "ne", "gt", "gte", "lt", "lte" -> return json.decodeFromJsonElement(ComparisonFilter.serializer(), value)
              "and", "or" -> return CaseCompoundFilter(json.decodeFromJsonElement(io.openai.model.CompoundFilter.serializer(), value))
              else -> throw SerializationException("Unknown tag: " + tag + " for io.openai.model.VectorStoreSearchRequest.Filters.CompoundFilter.Filters")
            }
          }

          override fun serialize(encoder: Encoder, `value`: Filters) {
            when(value) {
              is ComparisonFilter -> encoder.encodeSerializableValue(ComparisonFilter.serializer(), value)
              is CaseCompoundFilter -> encoder.encodeSerializableValue(io.openai.model.CompoundFilter.serializer(), value.value)
            }
          }
        }
      }

      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("and")
        And("and"),
        @SerialName("or")
        Or("or"),
        ;
      }
    }

    public object Serializer : KSerializer<Filters> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.VectorStoreSearchRequest.Filters", PolymorphicKind.SEALED) {
        element("ComparisonFilter", ComparisonFilter.serializer().descriptor)
        element("CompoundFilter", CompoundFilter.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Filters {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        val obj = value as? JsonObject
        val tag = (obj?.get("type") as? JsonPrimitive)?.content
        when(tag) {
          "eq", "ne", "gt", "gte", "lt", "lte" -> return json.decodeFromJsonElement(ComparisonFilter.serializer(), value)
          "and", "or" -> return json.decodeFromJsonElement(CompoundFilter.serializer(), value)
          else -> throw SerializationException("Unknown tag: " + tag + " for io.openai.model.VectorStoreSearchRequest.Filters")
        }
      }

      override fun serialize(encoder: Encoder, `value`: Filters) {
        when(value) {
          is ComparisonFilter -> encoder.encodeSerializableValue(ComparisonFilter.serializer(), value)
          is CompoundFilter -> encoder.encodeSerializableValue(CompoundFilter.serializer(), value)
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
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonArray -> json.attemptDeserialize(
            value,
            CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RankingOptions(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ranker: Ranker? = null,
    @SerialName("score_threshold")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
