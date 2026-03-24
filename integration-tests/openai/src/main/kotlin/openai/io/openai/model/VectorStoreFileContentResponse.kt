package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the parsed content of a vector store file.
 */
@Serializable
public data class VectorStoreFileContentResponse(
  public val `object`: Object,
  public val `data`: List<Data>,
  @SerialName("has_more")
  public val hasMore: Boolean,
  @SerialName("next_page")
  public val nextPage: String?,
) {
  @Serializable
  public data class Data(
    public val type: String? = null,
    public val text: String? = null,
  )

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("vector_store.file_content.page")
    VectorStoreFileContentPage("vector_store.file_content.page"),
    ;
  }
}
