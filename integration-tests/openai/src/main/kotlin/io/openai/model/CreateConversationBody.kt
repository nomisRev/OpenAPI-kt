package io.openai.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateConversationBody(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Optional<Metadata> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val items: Optional<List<InputItem>> = Optional.Absent,
)
