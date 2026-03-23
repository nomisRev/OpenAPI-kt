package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The GitHub URL of the alert resource.
 */
@JvmInline
@Serializable
public value class AlertHtmlUrlWrite(
  public val `value`: String,
)
