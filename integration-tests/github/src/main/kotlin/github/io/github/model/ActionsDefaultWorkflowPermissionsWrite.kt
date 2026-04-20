package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ActionsDefaultWorkflowPermissionsWrite(
  public val `value`: String,
) {
  @SerialName("read")
  Read("read"),
  @SerialName("write")
  Write("write"),
  ;
}
