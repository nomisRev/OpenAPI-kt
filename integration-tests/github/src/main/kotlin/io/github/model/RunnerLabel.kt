package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A label for a self hosted runner
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RunnerLabel(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
