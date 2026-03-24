package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A citation to a file.
 */
@Serializable
public data class FileCitationBody(
  @Required
  public val type: Type = Type.FileCitation,
  @SerialName("file_id")
  public val fileId: String,
  public val index: Long,
  public val filename: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file_citation")
    FileCitation("file_citation"),
    ;
  }
}
