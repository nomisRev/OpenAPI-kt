package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A wait action.
 */
@JvmInline
@Serializable
public value class WaitParam(
  @Required
  public val type: Type = Type.Wait,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("wait")
    Wait("wait"),
    ;
  }
}
