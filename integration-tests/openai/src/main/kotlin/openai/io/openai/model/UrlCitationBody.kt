package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A citation for a web resource used to generate a model response.
 */
@Serializable
public data class UrlCitationBody(
  @Required
  public val type: Type = Type.UrlCitation,
  public val url: String,
  @SerialName("start_index")
  public val startIndex: Long,
  @SerialName("end_index")
  public val endIndex: Long,
  public val title: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("url_citation")
    UrlCitation("url_citation"),
    ;
  }
}
