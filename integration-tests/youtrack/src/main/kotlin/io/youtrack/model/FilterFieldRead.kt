package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Represents an issue property, which can be a predefined field, a custom field, a link, and so on.
 */
@Serializable(with = FilterFieldRead.Serializer::class)
public sealed interface FilterFieldRead {
  public val id: String?

  public val presentation: String?

  public val name: String?

  @SerialName("FilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val presentation: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : FilterFieldRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "FilterField")
  }

  @SerialName("PredefinedFilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = PredefinedFilterField.Serializer::class)
  public data class PredefinedFilterField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val presentation: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : FilterFieldRead {
    public object Serializer : TaggedCaseSerializer<PredefinedFilterField>(PredefinedFilterField.generatedSerializer(), "${'$'}type", "PredefinedFilterField")
  }

  @SerialName("CustomFilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CustomFilterField.Serializer::class)
  public data class CustomFilterField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val presentation: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val customField: CustomFieldRead? = null,
  ) : FilterFieldRead {
    public object Serializer : TaggedCaseSerializer<CustomFilterField>(CustomFilterField.generatedSerializer(), "${'$'}type", "CustomFilterField")
  }

  public object Serializer : JsonContentPolymorphicSerializer<FilterFieldRead>(FilterFieldRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<FilterFieldRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "FilterField" -> Default.serializer()
      "PredefinedFilterField" -> PredefinedFilterField.serializer()
      "CustomFilterField" -> CustomFilterField.serializer()
      else -> throw SerializationException("'${'$'}type' of FilterFieldRead selects none of FilterField, PredefinedFilterField, CustomFilterField: " + tag)
    }
  }
}
