package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("BaseWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : BaseWorkItemRead

  @SerialName("IssueWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IssueWorkItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val creator: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val text: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val textPreview: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: WorkItemTypeRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val created: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updated: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val duration: DurationValueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val date: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issue: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val attributes: List<WorkItemAttribute>? = null,
  ) : BaseWorkItemRead
}
