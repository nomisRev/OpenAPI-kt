package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class FunctionObject(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parameters: FunctionParameters? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val strict: Optional<Boolean> = Optional.Absent,
)
