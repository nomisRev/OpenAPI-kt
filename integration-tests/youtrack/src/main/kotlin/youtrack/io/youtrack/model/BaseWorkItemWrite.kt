package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a basic ancestor for work items.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface BaseWorkItemWrite {
  @SerialName("BaseWorkItem")
  @Serializable
  public data object Default : BaseWorkItemWrite

  @SerialName("IssueWorkItem")
  @Serializable
  public data class IssueWorkItem(
    public val author: UserWrite? = null,
    public val text: String? = null,
    public val type: WorkItemTypeWrite? = null,
    public val created: Long? = null,
    public val updated: Long? = null,
    public val duration: DurationValueWrite? = null,
    public val date: Long? = null,
  ) : BaseWorkItemWrite
}
