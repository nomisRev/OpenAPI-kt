package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteModelResponse(
  public val id: String,
  public val deleted: Boolean,
  public val `object`: String,
)
