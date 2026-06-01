package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
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

  @JvmInline
  @SerialName("DatabaseAttributeValue")
  @Serializable
  public value class Default(
    override val id: String? = null,
  ) : DatabaseAttributeValueRead

  @SerialName("SwimlaneEntityAttributeValue")
  @Serializable
  public data class SwimlaneEntityAttributeValue(
    override val id: String? = null,
    public val name: String? = null,
    public val isResolved: Boolean? = null,
  ) : DatabaseAttributeValueRead

  @SerialName("AgileColumnFieldValue")
  @Serializable
  public data class AgileColumnFieldValue(
    override val id: String? = null,
    public val name: String? = null,
    public val isResolved: Boolean? = null,
  ) : DatabaseAttributeValueRead
}
