package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Annotation that references an uploaded file.
 */
@Serializable
public data class FileAnnotation(
  @Required
  public val type: Type = Type.File,
  public val source: FileAnnotationSource,
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
