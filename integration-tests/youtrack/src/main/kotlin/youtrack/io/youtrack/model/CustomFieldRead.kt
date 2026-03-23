package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CustomFieldRead(
  public val id: String? = null,
  public val name: String? = null,
  public val localizedName: String? = null,
  public val fieldType: FieldTypeRead? = null,
  public val isAutoAttached: Boolean? = null,
  public val isDisplayedInIssueList: Boolean? = null,
  public val ordinal: Int? = null,
  public val aliases: String? = null,
  public val fieldDefaults: CustomFieldDefaultsRead? = null,
  public val hasRunningJob: Boolean? = null,
  public val isUpdateable: Boolean? = null,
  public val instances: List<ProjectCustomFieldRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
