package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the attribute of a specific work item.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkItemAttribute(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workItem: BaseWorkItemRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectAttribute: WorkItemProjectAttributeRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `value`: WorkItemAttributeValueRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
