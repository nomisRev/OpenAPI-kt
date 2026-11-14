package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Base entity for different swimlane settings
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface SwimlaneSettingsWrite {
  public val enabled: Boolean?

  @SerialName("SwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
  ) : SwimlaneSettingsWrite

  @SerialName("AttributeBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AttributeBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<DatabaseAttributeValueWrite.SwimlaneEntityAttributeValue>? = null,
  ) : SwimlaneSettingsWrite

  @SerialName("IssueBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IssueBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultCardType: SwimlaneValueWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<SwimlaneValueWrite>? = null,
  ) : SwimlaneSettingsWrite
}
