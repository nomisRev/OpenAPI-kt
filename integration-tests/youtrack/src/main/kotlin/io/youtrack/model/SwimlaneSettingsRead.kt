package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
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
public sealed interface SwimlaneSettingsRead {
  public val id: String?

  public val enabled: Boolean?

  @SerialName("SwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
  ) : SwimlaneSettingsRead

  @SerialName("AttributeBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AttributeBasedSwimlaneSettings(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<DatabaseAttributeValueRead.SwimlaneEntityAttributeValue>? = null,
  ) : SwimlaneSettingsRead

  @SerialName("IssueBasedSwimlaneSettings")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : SwimlaneSettingsRead
}
