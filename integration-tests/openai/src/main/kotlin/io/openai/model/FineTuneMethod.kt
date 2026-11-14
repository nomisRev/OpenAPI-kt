package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The method used for fine-tuning.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class FineTuneMethod(
  public val type: Type,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val supervised: FineTuneSupervisedMethod? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dpo: FineTuneDPOMethod? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
