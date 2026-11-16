package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
 * Describes rules that define which colors are used for cards on the agile board.
 */
@Serializable(with = ColorCodingRead.Serializer::class)
public sealed interface ColorCodingRead {
  public val id: String?

  @SerialName("ColorCoding")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : ColorCodingRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "ColorCoding")
  }

  @SerialName("FieldBasedColorCoding")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = FieldBasedColorCoding.Serializer::class)
  public data class FieldBasedColorCoding(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val prototype: CustomFieldRead? = null,
  ) : ColorCodingRead {
    public object Serializer : TaggedCaseSerializer<FieldBasedColorCoding>(FieldBasedColorCoding.generatedSerializer(), "${'$'}type", "FieldBasedColorCoding")
  }

  @SerialName("ProjectBasedColorCoding")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = ProjectBasedColorCoding.Serializer::class)
  public data class ProjectBasedColorCoding(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val projectColors: List<ProjectColorRead>? = null,
  ) : ColorCodingRead {
    public object Serializer : TaggedCaseSerializer<ProjectBasedColorCoding>(ProjectBasedColorCoding.generatedSerializer(), "${'$'}type", "ProjectBasedColorCoding")
  }

  public object Serializer : JsonContentPolymorphicSerializer<ColorCodingRead>(ColorCodingRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ColorCodingRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "ColorCoding" -> Default.serializer()
      "FieldBasedColorCoding" -> FieldBasedColorCoding.serializer()
      "ProjectBasedColorCoding" -> ProjectBasedColorCoding.serializer()
      else -> throw SerializationException("'${'$'}type' of ColorCodingRead selects none of ColorCoding, FieldBasedColorCoding, ProjectBasedColorCoding: " + tag)
    }
  }
}
