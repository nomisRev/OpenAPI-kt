package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A citation for a container file used to generate a model response.
 */
@Serializable
public data class ContainerFileCitationBody(
  @Required
  public val type: Type = Type.ContainerFileCitation,
  @SerialName("container_id")
  public val containerId: String,
  @SerialName("file_id")
  public val fileId: String,
  @SerialName("start_index")
  public val startIndex: Long,
  @SerialName("end_index")
  public val endIndex: Long,
  public val filename: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("container_file_citation")
    ContainerFileCitation("container_file_citation"),
    ;
  }
}
