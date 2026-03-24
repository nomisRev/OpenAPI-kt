package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A grammar defined by the user.
 */
@Serializable
public data class CustomGrammarFormatParam(
  @Required
  public val type: Type = Type.Grammar,
  public val syntax: GrammarSyntax1,
  public val definition: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("grammar")
    Grammar("grammar"),
    ;
  }
}
