package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueLinkTypeRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val localizedName: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sourceToTarget: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val localizedSourceToTarget: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val targetToSource: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val localizedTargetToSource: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val directed: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val aggregation: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val readOnly: Boolean? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
