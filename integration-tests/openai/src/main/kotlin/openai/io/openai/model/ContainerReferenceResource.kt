package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a container created with /v1/containers.
 */
@Serializable
public data class ContainerReferenceResource(
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
