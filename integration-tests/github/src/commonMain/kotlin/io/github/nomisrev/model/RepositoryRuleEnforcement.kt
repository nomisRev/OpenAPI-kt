package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class RepositoryRuleEnforcement {
    @SerialName("disabled") Disabled, @SerialName("active") Active, @SerialName("evaluate") Evaluate;
}
