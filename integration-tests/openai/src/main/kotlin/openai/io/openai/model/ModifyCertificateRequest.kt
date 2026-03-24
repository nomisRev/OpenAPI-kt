package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ModifyCertificateRequest(
  public val name: String,
)
