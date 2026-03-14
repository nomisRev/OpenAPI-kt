package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeploymentBranchPolicyNamePatternWithType(val name: String, val type: Type? = null) {
    @Serializable
    enum class Type {
        @SerialName("branch") Branch, @SerialName("tag") Tag;
    }
}
