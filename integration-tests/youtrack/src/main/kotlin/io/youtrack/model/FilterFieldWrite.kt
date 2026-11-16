package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.DeserializationStrategy
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
@Serializable(with = FilterFieldWrite.Serializer::class)
public sealed interface FilterFieldWrite {
  @SerialName("FilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : FilterFieldWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "FilterField")
  }

  @SerialName("PredefinedFilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = PredefinedFilterField.Serializer::class)
  public class PredefinedFilterField() : FilterFieldWrite {
    public object Serializer : TaggedCaseSerializer<PredefinedFilterField>(PredefinedFilterField.generatedSerializer(), "${'$'}type", "PredefinedFilterField")
  }

  @SerialName("CustomFilterField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CustomFilterField.Serializer::class)
  public class CustomFilterField() : FilterFieldWrite {
    public object Serializer : TaggedCaseSerializer<CustomFilterField>(CustomFilterField.generatedSerializer(), "${'$'}type", "CustomFilterField")
  }

  public object Serializer : JsonContentPolymorphicSerializer<FilterFieldWrite>(FilterFieldWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<FilterFieldWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "FilterField" -> Default.serializer()
      "PredefinedFilterField" -> PredefinedFilterField.serializer()
      "CustomFilterField" -> CustomFilterField.serializer()
      else -> throw SerializationException("'${'$'}type' of FilterFieldWrite selects none of FilterField, PredefinedFilterField, CustomFilterField: " + tag)
    }
  }
}
