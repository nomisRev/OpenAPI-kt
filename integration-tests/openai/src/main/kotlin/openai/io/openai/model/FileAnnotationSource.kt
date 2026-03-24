package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Attachment source referenced by an annotation.
 */
@Serializable
public data class FileAnnotationSource(
  @Required
  public val type: Type = Type.File,
  public val filename: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("file")
    File("file"),
    ;
  }
}
