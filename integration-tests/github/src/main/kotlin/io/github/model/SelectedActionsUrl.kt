package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The API URL to use to get or set the actions and reusable workflows that are allowed to run, when `allowed_actions` is set to `selected`.
 */
@JvmInline
@Serializable
public value class SelectedActionsUrl(
  public val `value`: String,
)
