package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueType(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    val description: String?,
    val color: Color? = null,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    @SerialName("is_enabled") val isEnabled: Boolean? = null,
) {
    @Serializable
    enum class Color {
        @SerialName("gray")
        Gray,
        @SerialName("blue")
        Blue,
        @SerialName("green")
        Green,
        @SerialName("yellow")
        Yellow,
        @SerialName("orange")
        Orange,
        @SerialName("red")
        Red,
        @SerialName("pink")
        Pink,
        @SerialName("purple")
        Purple;
    }
}
