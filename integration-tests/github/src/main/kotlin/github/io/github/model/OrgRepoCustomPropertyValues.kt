package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * List of custom property values for a repository
 */
@Serializable
public data class OrgRepoCustomPropertyValues(
  @SerialName("repository_id")
  public val repositoryId: Long,
  @SerialName("repository_name")
  public val repositoryName: String,
  @SerialName("repository_full_name")
  public val repositoryFullName: String,
  public val properties: List<CustomPropertyValue>,
)
