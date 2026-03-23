package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A label for a self hosted runner
 */
@Serializable
public data class RunnerLabel(
  public val id: Long? = null,
  public val name: String,
  public val type: Type? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("read-only")
    ReadOnly("read-only"),
    @SerialName("custom")
    Custom("custom"),
    ;
  }
}
