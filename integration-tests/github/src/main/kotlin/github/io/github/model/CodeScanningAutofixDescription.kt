package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The description of an autofix.
 */
@JvmInline
@Serializable
public value class CodeScanningAutofixDescription(
  public val `value`: String,
)
