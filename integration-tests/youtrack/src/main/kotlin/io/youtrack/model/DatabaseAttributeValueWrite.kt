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
 * Represents string reference to the value.
 */
@Serializable(with = DatabaseAttributeValueWrite.Serializer::class)
public sealed interface DatabaseAttributeValueWrite {
  @SerialName("DatabaseAttributeValue")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : DatabaseAttributeValueWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "DatabaseAttributeValue")
  }

  @SerialName("SwimlaneEntityAttributeValue")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SwimlaneEntityAttributeValue.Serializer::class)
  public data class SwimlaneEntityAttributeValue(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
  ) : DatabaseAttributeValueWrite {
    public object Serializer : TaggedCaseSerializer<SwimlaneEntityAttributeValue>(SwimlaneEntityAttributeValue.generatedSerializer(), "${'$'}type", "SwimlaneEntityAttributeValue")
  }

  @SerialName("AgileColumnFieldValue")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AgileColumnFieldValue.Serializer::class)
  public data class AgileColumnFieldValue(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
  ) : DatabaseAttributeValueWrite {
    public object Serializer : TaggedCaseSerializer<AgileColumnFieldValue>(AgileColumnFieldValue.generatedSerializer(), "${'$'}type", "AgileColumnFieldValue")
  }

  public object Serializer : JsonContentPolymorphicSerializer<DatabaseAttributeValueWrite>(DatabaseAttributeValueWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<DatabaseAttributeValueWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "DatabaseAttributeValue" -> Default.serializer()
      "SwimlaneEntityAttributeValue" -> SwimlaneEntityAttributeValue.serializer()
      "AgileColumnFieldValue" -> AgileColumnFieldValue.serializer()
      else -> throw SerializationException("'${'$'}type' of DatabaseAttributeValueWrite selects none of DatabaseAttributeValue, SwimlaneEntityAttributeValue, AgileColumnFieldValue: " + tag)
    }
  }
}
