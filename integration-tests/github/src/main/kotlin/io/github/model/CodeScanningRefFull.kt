package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The full Git reference, formatted as `refs/heads/<branch name>`,
 * `refs/tags/<tag>`, `refs/pull/<number>/merge`, or `refs/pull/<number>/head`.
 */
@JvmInline
@Serializable
public value class CodeScanningRefFull(
  public val `value`: String,
)
