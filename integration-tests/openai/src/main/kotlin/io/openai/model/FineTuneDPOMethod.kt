package io.openai.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Configuration for the DPO fine-tuning method.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class FineTuneDPOMethod(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hyperparameters: FineTuneDPOHyperparameters? = null,
)
