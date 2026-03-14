package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class EnabledRepositories {
    @SerialName("all") All, @SerialName("none") None, @SerialName("selected") Selected;
}
