package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Tool call objects
 */
@Serializable
public data class RunToolCallObject(
  public val id: String,
  public val type: Type,
  public val function: Function,
) {
  /**
   * The function definition.
   */
  @Serializable
  public data class Function(
    public val name: String,
    public val arguments: String,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("function")
    Function("function"),
    ;
  }
}
