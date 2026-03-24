package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A tool that controls a virtual computer. Learn more about the [computer tool](https://platform.openai.com/docs/guides/tools-computer-use).
 */
@JvmInline
@Serializable
public value class ComputerTool(
  @Required
  public val type: Type = Type.Computer,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("computer")
    Computer("computer"),
    ;
  }
}
