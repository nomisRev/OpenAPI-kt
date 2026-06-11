package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub repository view for Classroom
 */
@Serializable
public data class SimpleClassroomRepository(
  public val id: Long,
  @SerialName("full_name")
  public val fullName: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("node_id")
  public val nodeId: String,
  public val `private`: Boolean,
  @SerialName("default_branch")
  public val defaultBranch: String,
)
