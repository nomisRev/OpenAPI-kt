package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ComputerAction {
  /**
   * A click action.
   */
  @SerialName("click")
  @Serializable
  public data class Click(
    public val button: ClickButtonType,
    public val x: Long,
    public val y: Long,
  ) : ComputerAction

  /**
   * A double click action.
   */
  @SerialName("double_click")
  @Serializable
  public data class DoubleClick(
    public val x: Long,
    public val y: Long,
  ) : ComputerAction

  /**
   * A drag action.
   */
  @JvmInline
  @SerialName("drag")
  @Serializable
  public value class Drag(
    public val path: List<CoordParam>,
  ) : ComputerAction

  /**
   * A collection of keypresses the model would like to perform.
   */
  @JvmInline
  @SerialName("keypress")
  @Serializable
  public value class Keypress(
    public val keys: List<String>,
  ) : ComputerAction

  /**
   * A mouse move action.
   */
  @SerialName("move")
  @Serializable
  public data class Move(
    public val x: Long,
    public val y: Long,
  ) : ComputerAction

  @Serializable
  @SerialName("screenshot")
  public data object Screenshot : ComputerAction

  /**
   * A scroll action.
   */
  @SerialName("scroll")
  @Serializable
  public data class Scroll(
    public val x: Long,
    public val y: Long,
    @SerialName("scroll_x")
    public val scrollX: Long,
    @SerialName("scroll_y")
    public val scrollY: Long,
  ) : ComputerAction

  /**
   * An action to type in text.
   */
  @JvmInline
  @SerialName("type")
  @Serializable
  public value class Type(
    public val text: String,
  ) : ComputerAction

  @Serializable
  @SerialName("wait")
  public data object Wait : ComputerAction
}
