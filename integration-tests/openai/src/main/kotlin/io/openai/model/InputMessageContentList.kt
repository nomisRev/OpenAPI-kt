package io.openai.model

import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A list of one or many input items to the model, containing different content 
 * types.
 *
 */
@JvmInline
@Serializable
public value class InputMessageContentList(
  public val items: List<InputContent>,
)
