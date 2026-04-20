package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The Git reference, formatted as `refs/pull/<number>/merge`, `refs/pull/<number>/head`,
 * `refs/heads/<branch name>` or simply `<branch name>`.
 */
@JvmInline
@Serializable
public value class CodeScanningRefWrite(
  public val `value`: String,
)
