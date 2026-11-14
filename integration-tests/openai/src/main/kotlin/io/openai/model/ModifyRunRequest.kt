package io.openai.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ModifyRunRequest(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Optional<Metadata> = Optional.Absent,
)
