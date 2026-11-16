package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Represents a basic ancestor for work items.
 */
@Serializable(with = BaseWorkItemRead.Serializer::class)
public sealed interface BaseWorkItemRead {
  public val id: String?

  @SerialName("BaseWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : BaseWorkItemRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "BaseWorkItem")
  }

  @SerialName("IssueWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueWorkItem.Serializer::class)
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
  ) : BaseWorkItemRead {
    public object Serializer : TaggedCaseSerializer<IssueWorkItem>(IssueWorkItem.generatedSerializer(), "${'$'}type", "IssueWorkItem")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BaseWorkItemRead>(BaseWorkItemRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BaseWorkItemRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "BaseWorkItem" -> Default.serializer()
      "IssueWorkItem" -> IssueWorkItem.serializer()
      else -> throw SerializationException("'${'$'}type' of BaseWorkItemRead selects none of BaseWorkItem, IssueWorkItem: " + tag)
    }
  }
}
