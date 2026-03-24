package io.openai.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@JvmInline
@Serializable
public value class EvalGraderStringCheck(
  public val `value`: JsonElement,
)
