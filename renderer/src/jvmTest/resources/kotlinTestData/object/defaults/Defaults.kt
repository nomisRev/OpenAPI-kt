package io.github.nomisrev.render.test.object_.defaults

import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Defaults(
  @Required
  public val text: String = "hello",
  @Required
  public val count: Int = 42,
  @Required
  public val big: Long = 9_007_199_254_740_991L,
  @Required
  public val ratio: Float = 1.5f,
  @Required
  public val score: Double = 2.75,
  @Required
  public val enabled: Boolean = true,
  @Required
  public val state: State = State.Off,
  @Required
  public val tags: List<String> = listOf("a", "b"),
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("on")
    On("on"),
    @SerialName("off")
    Off("off"),
    ;
  }
}
