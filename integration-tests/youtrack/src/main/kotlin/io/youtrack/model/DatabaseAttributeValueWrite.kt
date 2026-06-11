package io.youtrack.model

import kotlin.OptIn
import kotlin.String
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
public sealed interface DatabaseAttributeValueWrite {
  @SerialName("DatabaseAttributeValue")
  @Serializable
  public data object Default : DatabaseAttributeValueWrite

  @SerialName("SwimlaneEntityAttributeValue")
  @Serializable
  public data class SwimlaneEntityAttributeValue(
    public val name: String? = null,
  ) : DatabaseAttributeValueWrite

  @SerialName("AgileColumnFieldValue")
  @Serializable
  public data class AgileColumnFieldValue(
    public val name: String? = null,
  ) : DatabaseAttributeValueWrite
}
