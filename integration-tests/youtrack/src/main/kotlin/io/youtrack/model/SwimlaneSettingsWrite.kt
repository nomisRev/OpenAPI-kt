package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
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
@Serializable(with = SwimlaneSettingsWrite.Serializer::class)
public sealed interface SwimlaneSettingsWrite {
  public val enabled: Boolean?

  @SerialName("SwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
  ) : SwimlaneSettingsWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "SwimlaneSettings")
  }

  @SerialName("AttributeBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AttributeBasedSwimlaneSettings.Serializer::class)
  public data class AttributeBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<DatabaseAttributeValueWrite.SwimlaneEntityAttributeValue>? = null,
  ) : SwimlaneSettingsWrite {
    public object Serializer : TaggedCaseSerializer<AttributeBasedSwimlaneSettings>(AttributeBasedSwimlaneSettings.generatedSerializer(), "${'$'}type", "AttributeBasedSwimlaneSettings")
  }

  @SerialName("IssueBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueBasedSwimlaneSettings.Serializer::class)
  public data class IssueBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultCardType: SwimlaneValueWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<SwimlaneValueWrite>? = null,
  ) : SwimlaneSettingsWrite {
    public object Serializer : TaggedCaseSerializer<IssueBasedSwimlaneSettings>(IssueBasedSwimlaneSettings.generatedSerializer(), "${'$'}type", "IssueBasedSwimlaneSettings")
  }

  public object Serializer : JsonContentPolymorphicSerializer<SwimlaneSettingsWrite>(SwimlaneSettingsWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SwimlaneSettingsWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "SwimlaneSettings" -> Default.serializer()
      "AttributeBasedSwimlaneSettings" -> AttributeBasedSwimlaneSettings.serializer()
      "IssueBasedSwimlaneSettings" -> IssueBasedSwimlaneSettings.serializer()
      else -> throw SerializationException("'${'$'}type' of SwimlaneSettingsWrite selects none of SwimlaneSettings, AttributeBasedSwimlaneSettings, IssueBasedSwimlaneSettings: " + tag)
    }
  }
}
