package io.youtrack.model

import kotlin.Boolean
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
 * Base entity for different swimlane settings
 */
@Serializable(with = SwimlaneSettingsRead.Serializer::class)
public sealed interface SwimlaneSettingsRead {
  public val id: String?

  public val enabled: Boolean?

  @SerialName("SwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
  ) : SwimlaneSettingsRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "SwimlaneSettings")
  }

  @SerialName("AttributeBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AttributeBasedSwimlaneSettings.Serializer::class)
  public data class AttributeBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<DatabaseAttributeValueRead.SwimlaneEntityAttributeValue>? = null,
  ) : SwimlaneSettingsRead {
    public object Serializer : TaggedCaseSerializer<AttributeBasedSwimlaneSettings>(AttributeBasedSwimlaneSettings.generatedSerializer(), "${'$'}type", "AttributeBasedSwimlaneSettings")
  }

  @SerialName("IssueBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueBasedSwimlaneSettings.Serializer::class)
  public data class IssueBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultCardType: SwimlaneValueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<SwimlaneValueRead>? = null,
  ) : SwimlaneSettingsRead {
    public object Serializer : TaggedCaseSerializer<IssueBasedSwimlaneSettings>(IssueBasedSwimlaneSettings.generatedSerializer(), "${'$'}type", "IssueBasedSwimlaneSettings")
  }

  public object Serializer : JsonContentPolymorphicSerializer<SwimlaneSettingsRead>(SwimlaneSettingsRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SwimlaneSettingsRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "SwimlaneSettings" -> Default.serializer()
      "AttributeBasedSwimlaneSettings" -> AttributeBasedSwimlaneSettings.serializer()
      "IssueBasedSwimlaneSettings" -> IssueBasedSwimlaneSettings.serializer()
      else -> throw SerializationException("'${'$'}type' of SwimlaneSettingsRead selects none of SwimlaneSettings, AttributeBasedSwimlaneSettings, IssueBasedSwimlaneSettings: " + tag)
    }
  }
}
