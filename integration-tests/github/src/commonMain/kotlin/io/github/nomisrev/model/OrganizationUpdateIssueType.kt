package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationUpdateIssueType(
    val name: String,
    @SerialName("is_enabled") val isEnabled: Boolean,
    val description: String? = null,
    val color: Color? = null,
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
