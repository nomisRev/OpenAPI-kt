package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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
public sealed interface ColorCodingRead {
  public val id: String?

  @JvmInline
  @SerialName("ColorCoding")
  @Serializable
  public value class Default(
    override val id: String? = null,
  ) : ColorCodingRead

  @SerialName("FieldBasedColorCoding")
  @Serializable
  public data class FieldBasedColorCoding(
    override val id: String? = null,
    public val prototype: CustomFieldRead? = null,
  ) : ColorCodingRead

  @SerialName("ProjectBasedColorCoding")
  @Serializable
  public data class ProjectBasedColorCoding(
    override val id: String? = null,
    public val projectColors: List<ProjectColorRead>? = null,
  ) : ColorCodingRead
}
