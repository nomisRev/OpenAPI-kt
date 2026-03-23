package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class CustomFieldWrite(
  public val name: String? = null,
  public val localizedName: String? = null,
  public val fieldType: FieldTypeWrite? = null,
  public val isAutoAttached: Boolean? = null,
  public val isDisplayedInIssueList: Boolean? = null,
  public val ordinal: Int? = null,
  public val aliases: String? = null,
  public val instances: List<ProjectCustomFieldWrite>? = null,
)
