package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Reference to a prompt template and its variables.
 * [Learn more](/docs/guides/text?api-mode=responses#reusable-prompts).
 *
 */
@Serializable
public data class Prompt(
  public val id: String,
  public val version: String? = null,
  public val variables: ResponsePromptVariables? = null,
)
