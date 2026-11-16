package io.youtrack.model

import kotlin.Boolean
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
@Serializable(with = DatabaseAttributeValueRead.Serializer::class)
public sealed interface DatabaseAttributeValueRead {
  public val id: String?

  @SerialName("DatabaseAttributeValue")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : DatabaseAttributeValueRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "DatabaseAttributeValue")
  }

  @SerialName("SwimlaneEntityAttributeValue")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SwimlaneEntityAttributeValue.Serializer::class)
  public data class SwimlaneEntityAttributeValue(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isResolved: Boolean? = null,
  ) : DatabaseAttributeValueRead {
    public object Serializer : TaggedCaseSerializer<SwimlaneEntityAttributeValue>(SwimlaneEntityAttributeValue.generatedSerializer(), "${'$'}type", "SwimlaneEntityAttributeValue")
  }

  @SerialName("AgileColumnFieldValue")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AgileColumnFieldValue.Serializer::class)
  public data class AgileColumnFieldValue(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isResolved: Boolean? = null,
  ) : DatabaseAttributeValueRead {
    public object Serializer : TaggedCaseSerializer<AgileColumnFieldValue>(AgileColumnFieldValue.generatedSerializer(), "${'$'}type", "AgileColumnFieldValue")
  }

  public object Serializer : JsonContentPolymorphicSerializer<DatabaseAttributeValueRead>(DatabaseAttributeValueRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<DatabaseAttributeValueRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "DatabaseAttributeValue" -> Default.serializer()
      "SwimlaneEntityAttributeValue" -> SwimlaneEntityAttributeValue.serializer()
      "AgileColumnFieldValue" -> AgileColumnFieldValue.serializer()
      else -> throw SerializationException("'${'$'}type' of DatabaseAttributeValueRead selects none of DatabaseAttributeValue, SwimlaneEntityAttributeValue, AgileColumnFieldValue: " + tag)
    }
  }
}
