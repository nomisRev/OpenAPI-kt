package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
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
public sealed interface BaseWorkItemWrite {
  @SerialName("BaseWorkItem")
  @Serializable
  public data object Default : BaseWorkItemWrite

  @SerialName("IssueWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IssueWorkItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val author: UserWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val text: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: WorkItemTypeWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val created: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updated: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val duration: DurationValueWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val date: Long? = null,
  ) : BaseWorkItemWrite
}
