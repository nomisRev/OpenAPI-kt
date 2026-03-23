package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.collections.List
import kotlin.jvm.JvmInline
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

  @JvmInline
  @SerialName("Default")
  @Serializable
  public value class Default(
    override val enabled: Boolean? = null,
  ) : SwimlaneSettingsWrite

  @SerialName("AttributeBasedSwimlaneSettings")
  @Serializable
  public data class AttributeBasedSwimlaneSettings(
    override val enabled: Boolean? = null,
    public val `field`: FilterFieldWrite? = null,
    public val values: List<DatabaseAttributeValueWrite.SwimlaneEntityAttributeValue>? = null,
  ) : SwimlaneSettingsWrite

  @SerialName("IssueBasedSwimlaneSettings")
  @Serializable
  public data class IssueBasedSwimlaneSettings(
    override val enabled: Boolean? = null,
    public val `field`: FilterFieldWrite? = null,
    public val defaultCardType: SwimlaneValueWrite? = null,
    public val values: List<SwimlaneValueWrite>? = null,
  ) : SwimlaneSettingsWrite
}
