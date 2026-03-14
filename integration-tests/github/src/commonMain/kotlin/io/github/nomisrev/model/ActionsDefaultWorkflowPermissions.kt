package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class ActionsDefaultWorkflowPermissions {
    @SerialName("read") Read, @SerialName("write") Write;
}
