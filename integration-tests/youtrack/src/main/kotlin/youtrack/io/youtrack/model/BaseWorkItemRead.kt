package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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
public sealed interface BaseWorkItemRead {
  public val id: String?

  @JvmInline
  @SerialName("Default")
  @Serializable
  public value class Default(
    override val id: String? = null,
  ) : BaseWorkItemRead

  @SerialName("IssueWorkItem")
  @Serializable
  public data class IssueWorkItem(
    override val id: String? = null,
    public val author: UserRead? = null,
    public val creator: UserRead? = null,
    public val text: String? = null,
    public val textPreview: String? = null,
    public val type: WorkItemTypeRead? = null,
    public val created: Long? = null,
    public val updated: Long? = null,
    public val duration: DurationValueRead? = null,
    public val date: Long? = null,
    public val issue: IssueRead? = null,
    public val attributes: List<WorkItemAttribute>? = null,
  ) : BaseWorkItemRead
}
