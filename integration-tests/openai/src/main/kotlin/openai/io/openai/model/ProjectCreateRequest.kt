package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectCreateRequest(
  public val name: String,
  public val geography: Geography? = null,
) {
  @Serializable
  public enum class Geography {
    US,
    EU,
    JP,
    IN,
    KR,
    CA,
    AU,
    SG,
  }
}
