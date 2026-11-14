package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents string reference to the value.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface DatabaseAttributeValueRead {
  public val id: String?

  @SerialName("DatabaseAttributeValue")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : DatabaseAttributeValueRead

  @SerialName("SwimlaneEntityAttributeValue")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SwimlaneEntityAttributeValue(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isResolved: Boolean? = null,
  ) : DatabaseAttributeValueRead

  @SerialName("AgileColumnFieldValue")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AgileColumnFieldValue(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isResolved: Boolean? = null,
  ) : DatabaseAttributeValueRead
}
