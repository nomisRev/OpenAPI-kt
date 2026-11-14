package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CustomFieldWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val localizedName: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fieldType: FieldTypeWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isAutoAttached: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isDisplayedInIssueList: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ordinal: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val aliases: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instances: List<ProjectCustomFieldWrite>? = null,
)
