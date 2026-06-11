package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The method used for fine-tuning.
 */
@Serializable
public data class FineTuneMethod(
  public val type: Type,
  public val supervised: FineTuneSupervisedMethod? = null,
  public val dpo: FineTuneDPOMethod? = null,
  public val reinforcement: FineTuneReinforcementMethod? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("supervised")
    Supervised("supervised"),
    @SerialName("dpo")
    Dpo("dpo"),
    @SerialName("reinforcement")
    Reinforcement("reinforcement"),
    ;
  }
}
