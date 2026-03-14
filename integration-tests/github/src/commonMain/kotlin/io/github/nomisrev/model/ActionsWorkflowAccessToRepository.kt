package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ActionsWorkflowAccessToRepository(@SerialName("access_level") val accessLevel: AccessLevel) {
    @Serializable
    enum class AccessLevel {
        @SerialName("none") None, @SerialName("user") User, @SerialName("organization") Organization;
    }
}
