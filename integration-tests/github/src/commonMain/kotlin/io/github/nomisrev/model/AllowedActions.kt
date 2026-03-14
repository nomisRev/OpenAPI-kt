package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class AllowedActions {
    @SerialName("all") All, @SerialName("local_only") LocalOnly, @SerialName("selected") Selected;
}
