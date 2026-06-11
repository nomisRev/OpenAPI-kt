package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class GrammarSyntax1(
  public val `value`: String,
) {
  @SerialName("lark")
  Lark("lark"),
  @SerialName("regex")
  Regex("regex"),
  ;
}
