package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SearchContentType(
  public val `value`: String,
) {
  @SerialName("text")
  Text("text"),
  @SerialName("image")
  Image("image"),
  ;
}
