package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerReferenceParam(
  @Required
  public val type: Type = Type.ContainerReference,
  @SerialName("container_id")
  public val containerId: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("container_reference")
    ContainerReference("container_reference"),
    ;
  }
}
