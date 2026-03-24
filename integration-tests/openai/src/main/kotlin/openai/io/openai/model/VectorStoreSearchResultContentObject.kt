package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VectorStoreSearchResultContentObject(
  public val type: Type,
  public val text: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    ;
  }
}
