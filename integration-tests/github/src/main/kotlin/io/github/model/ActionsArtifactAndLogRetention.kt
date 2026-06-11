package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ActionsArtifactAndLogRetention(
  public val days: Long,
)
