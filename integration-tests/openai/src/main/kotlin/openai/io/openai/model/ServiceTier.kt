package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ServiceTier(
  public val `value`: String,
) {
  @SerialName("auto")
  Auto("auto"),
  @SerialName("default")
  Default("default"),
  @SerialName("flex")
  Flex("flex"),
  @SerialName("scale")
  Scale("scale"),
  @SerialName("priority")
  Priority("priority"),
  ;
}
