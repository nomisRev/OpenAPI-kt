package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A screenshot action.
 */
@JvmInline
@Serializable
public value class ScreenshotParam(
  @Required
  public val type: Type = Type.Screenshot,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("screenshot")
    Screenshot("screenshot"),
    ;
  }
}
