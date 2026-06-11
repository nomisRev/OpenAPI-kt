package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Describes rules that define which colors are used for cards on the agile board.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface ColorCodingWrite {
  @SerialName("ColorCoding")
  @Serializable
  public class Default() : ColorCodingWrite

  @SerialName("FieldBasedColorCoding")
  @Serializable
  public data class FieldBasedColorCoding(
    public val prototype: CustomFieldWrite? = null,
  ) : ColorCodingWrite

  @SerialName("ProjectBasedColorCoding")
  @Serializable
  public data class ProjectBasedColorCoding(
    public val projectColors: List<ProjectColorWrite>? = null,
  ) : ColorCodingWrite
}
