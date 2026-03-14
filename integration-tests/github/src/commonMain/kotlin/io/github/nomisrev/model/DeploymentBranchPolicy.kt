package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeploymentBranchPolicy(
    val id: Long? = null,
    @SerialName("node_id") val nodeId: String? = null,
    val name: String? = null,
    val type: Type? = null,
) {
    @Serializable
    enum class Type {
        @SerialName("branch") Branch, @SerialName("tag") Tag;
    }
}
