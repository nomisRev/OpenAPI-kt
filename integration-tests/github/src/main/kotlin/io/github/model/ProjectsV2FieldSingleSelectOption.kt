package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectsV2FieldSingleSelectOption(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val color: Color? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
