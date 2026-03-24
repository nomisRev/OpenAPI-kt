package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ComputerAction {
  @Serializable
  @JvmInline
  @SerialName("ClickParam")
  public value class ClickParam(
    public val `value`: io.openai.model.ClickParam,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("DoubleClickAction")
  public value class DoubleClickAction(
    public val `value`: io.openai.model.DoubleClickAction,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("DragParam")
  public value class DragParam(
    public val `value`: io.openai.model.DragParam,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("KeyPressAction")
  public value class KeyPressAction(
    public val `value`: io.openai.model.KeyPressAction,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("MoveParam")
  public value class MoveParam(
    public val `value`: io.openai.model.MoveParam,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("ScreenshotParam")
  public value class ScreenshotParam(
    public val `value`: io.openai.model.ScreenshotParam,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("ScrollParam")
  public value class ScrollParam(
    public val `value`: io.openai.model.ScrollParam,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("TypeParam")
  public value class TypeParam(
    public val `value`: io.openai.model.TypeParam,
  ) : ComputerAction

  @Serializable
  @JvmInline
  @SerialName("WaitParam")
  public value class WaitParam(
    public val `value`: io.openai.model.WaitParam,
  ) : ComputerAction
}
