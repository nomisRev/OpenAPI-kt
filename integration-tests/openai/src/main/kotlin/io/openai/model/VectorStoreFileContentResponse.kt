package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Data(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
