package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * An annotation that applies to a span of output text.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface Annotation {
  /**
   * A citation to a file.
   */
  @SerialName("file_citation")
  @Serializable
  public data class FileCitation(
    @SerialName("file_id")
    public val fileId: String,
    public val index: Long,
    public val filename: String,
  ) : Annotation

  /**
   * A citation for a web resource used to generate a model response.
   */
  @SerialName("url_citation")
  @Serializable
  public data class UrlCitation(
    public val url: String,
    @SerialName("start_index")
    public val startIndex: Long,
    @SerialName("end_index")
    public val endIndex: Long,
    public val title: String,
  ) : Annotation

  /**
   * A citation for a container file used to generate a model response.
   */
  @SerialName("container_file_citation")
  @Serializable
  public data class ContainerFileCitation(
    @SerialName("container_id")
    public val containerId: String,
    @SerialName("file_id")
    public val fileId: String,
    @SerialName("start_index")
    public val startIndex: Long,
    @SerialName("end_index")
    public val endIndex: Long,
    public val filename: String,
  ) : Annotation

  /**
   * A path to a file.
   *
   */
  @SerialName("file_path")
  @Serializable
  public data class FilePath(
    @SerialName("file_id")
    public val fileId: String,
    public val index: Long,
  ) : Annotation
}
