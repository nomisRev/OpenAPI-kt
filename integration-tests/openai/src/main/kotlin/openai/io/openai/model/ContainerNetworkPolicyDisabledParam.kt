package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ContainerNetworkPolicyDisabledParam(
  @Required
  public val type: Type = Type.Disabled,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("disabled")
    Disabled("disabled"),
    ;
  }
}
