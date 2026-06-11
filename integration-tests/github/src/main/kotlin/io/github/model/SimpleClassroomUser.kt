package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub user simplified for Classroom.
 */
@Serializable
public data class SimpleClassroomUser(
  public val id: Long,
  public val login: String,
  @SerialName("avatar_url")
  public val avatarUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
)
