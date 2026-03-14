package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectsV2FieldSingleSelectOption(
    val name: String? = null,
    val color: Color? = null,
    val description: String? = null,
) {
    @Serializable
    enum class Color {
        BLUE, GRAY, GREEN, ORANGE, PINK, PURPLE, RED, YELLOW;
    }
}
