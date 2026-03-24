package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Configuration for the DPO fine-tuning method.
 */
@JvmInline
@Serializable
public value class FineTuneDPOMethod(
  public val hyperparameters: FineTuneDPOHyperparameters? = null,
)
