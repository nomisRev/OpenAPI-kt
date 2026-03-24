package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Emitted when an annotation is added to output text content.
 *
 */
@Serializable
public data class ResponseOutputTextAnnotationAddedEvent(
  public val type: Type,
  @SerialName("item_id")
  public val itemId: String,
  @SerialName("output_index")
  public val outputIndex: Long,
  @SerialName("content_index")
  public val contentIndex: Long,
  @SerialName("annotation_index")
  public val annotationIndex: Long,
  @SerialName("sequence_number")
  public val sequenceNumber: Long,
  public val `annotation`: JsonElement,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("response.output_text.annotation.added")
    ResponseOutputTextAnnotationAdded("response.output_text.annotation.added"),
    ;
  }
}
