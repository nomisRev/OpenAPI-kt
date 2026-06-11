package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Configuration for the supervised fine-tuning method.
 */
@JvmInline
@Serializable
public value class FineTuneSupervisedMethod(
  public val hyperparameters: FineTuneSupervisedHyperparameters? = null,
)
