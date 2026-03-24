package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that a thread is active.
 */
@JvmInline
@Serializable
public value class ActiveStatus(
  @Required
  public val type: Type = Type.Active,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    ;
  }
}
