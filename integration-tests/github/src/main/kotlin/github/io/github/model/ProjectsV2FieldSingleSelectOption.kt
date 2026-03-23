package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectsV2FieldSingleSelectOption(
  public val name: String? = null,
  public val color: Color? = null,
  public val description: String? = null,
) {
  @Serializable
  public enum class Color {
    BLUE,
    GRAY,
    GREEN,
    ORANGE,
    PINK,
    PURPLE,
    RED,
    YELLOW,
  }
}
