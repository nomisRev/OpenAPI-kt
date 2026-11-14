package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents a value of a work item attribute.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WorkItemAttributeValueWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val autoAttach: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prototype: WorkItemAttributePrototypeWrite? = null,
)
