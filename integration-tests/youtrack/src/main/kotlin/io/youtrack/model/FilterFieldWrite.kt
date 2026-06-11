package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents an issue property, which can be a predefined field, a custom field, a link, and so on.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface FilterFieldWrite {
  @SerialName("FilterField")
  @Serializable
  public data object Default : FilterFieldWrite

  @SerialName("PredefinedFilterField")
  @Serializable
  public class PredefinedFilterField() : FilterFieldWrite

  @SerialName("CustomFilterField")
  @Serializable
  public class CustomFilterField() : FilterFieldWrite
}
