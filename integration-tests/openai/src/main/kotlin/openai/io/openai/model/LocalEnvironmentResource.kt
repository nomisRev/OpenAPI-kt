package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the use of a local environment to perform shell actions.
 */
@JvmInline
@Serializable
public value class LocalEnvironmentResource(
  @Required
  public val type: Type = Type.Local,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("local")
    Local("local"),
    ;
  }
}
