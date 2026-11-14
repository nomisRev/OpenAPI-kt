package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("ColorCoding")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : ColorCodingRead

  @SerialName("FieldBasedColorCoding")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class FieldBasedColorCoding(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val prototype: CustomFieldRead? = null,
  ) : ColorCodingRead

  @SerialName("ProjectBasedColorCoding")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectBasedColorCoding(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val projectColors: List<ProjectColorRead>? = null,
  ) : ColorCodingRead
}
