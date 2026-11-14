package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectCreateRequest(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
