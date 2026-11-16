package io.youtrack.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
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
@Serializable(with = BaseWorkItemWrite.Serializer::class)
public sealed interface BaseWorkItemWrite {
  @SerialName("BaseWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : BaseWorkItemWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "BaseWorkItem")
  }

  @SerialName("IssueWorkItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueWorkItem.Serializer::class)
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
  ) : BaseWorkItemWrite {
    public object Serializer : TaggedCaseSerializer<IssueWorkItem>(IssueWorkItem.generatedSerializer(), "${'$'}type", "IssueWorkItem")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BaseWorkItemWrite>(BaseWorkItemWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BaseWorkItemWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "BaseWorkItem" -> Default.serializer()
      "IssueWorkItem" -> IssueWorkItem.serializer()
      else -> throw SerializationException("'${'$'}type' of BaseWorkItemWrite selects none of BaseWorkItem, IssueWorkItem: " + tag)
    }
  }
}
