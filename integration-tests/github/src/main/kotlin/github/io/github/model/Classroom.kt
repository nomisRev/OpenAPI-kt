package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A GitHub Classroom classroom
 */
@Serializable
public data class Classroom(
  public val id: Long,
  public val name: String,
  public val archived: Boolean,
  public val organization: SimpleClassroomOrganization,
  public val url: String,
)
