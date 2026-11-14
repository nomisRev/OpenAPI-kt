package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CustomFieldRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val localizedName: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fieldType: FieldTypeRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isAutoAttached: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isDisplayedInIssueList: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ordinal: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val aliases: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fieldDefaults: CustomFieldDefaultsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasRunningJob: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isUpdateable: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instances: List<ProjectCustomFieldRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
