package io.github.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Timeline Event
 */
@JvmInline
@Serializable
public value class TimelineIssueEvents(
  public val `value`: JsonElement,
)
