package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Reference to a prompt template and its variables.
 * [Learn more](/docs/guides/text?api-mode=responses#reusable-prompts).
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Prompt(
  public val id: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val version: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val variables: Optional<ResponsePromptVariables> = Optional.Absent,
)
