package io.openai.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Configuration for the reinforcement fine-tuning method.
 */
@Serializable
public data class FineTuneReinforcementMethod(
  public val grader: JsonElement,
  public val hyperparameters: FineTuneReinforcementHyperparameters? = null,
)
