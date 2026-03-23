package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueLinkTypeRead(
  public val id: String? = null,
  public val name: String? = null,
  public val localizedName: String? = null,
  public val sourceToTarget: String? = null,
  public val localizedSourceToTarget: String? = null,
  public val targetToSource: String? = null,
  public val localizedTargetToSource: String? = null,
  public val directed: Boolean? = null,
  public val aggregation: Boolean? = null,
  public val readOnly: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
