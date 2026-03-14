package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RunnerLabel(val id: Long? = null, val name: String, val type: Type? = null) {
    @Serializable
    enum class Type {
        @SerialName("read-only") ReadOnly, @SerialName("custom") Custom;
    }
}
