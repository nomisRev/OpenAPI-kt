package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
public sealed interface SwimlaneSettingsRead {
  public val id: String?

  public val enabled: Boolean?

  @SerialName("SwimlaneSettings")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val enabled: Boolean? = null,
  ) : SwimlaneSettingsRead

  @SerialName("AttributeBasedSwimlaneSettings")
  @Serializable
  public data class AttributeBasedSwimlaneSettings(
    override val id: String? = null,
    override val enabled: Boolean? = null,
    public val `field`: FilterFieldRead? = null,
    public val values: List<DatabaseAttributeValueRead.SwimlaneEntityAttributeValue>? = null,
  ) : SwimlaneSettingsRead

  @SerialName("IssueBasedSwimlaneSettings")
  @Serializable
  public data class IssueBasedSwimlaneSettings(
    override val id: String? = null,
    override val enabled: Boolean? = null,
    public val `field`: FilterFieldRead? = null,
    public val defaultCardType: SwimlaneValueRead? = null,
    public val values: List<SwimlaneValueRead>? = null,
  ) : SwimlaneSettingsRead
}
