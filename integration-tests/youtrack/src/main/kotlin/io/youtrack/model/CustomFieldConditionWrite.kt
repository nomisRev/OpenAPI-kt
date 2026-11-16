package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
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
 * Represents the condition for showing a custom field.
 */
@Serializable(with = CustomFieldConditionWrite.Serializer::class)
public sealed interface CustomFieldConditionWrite {
  public val parent: ProjectCustomFieldWrite?

  @SerialName("CustomFieldCondition")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: ProjectCustomFieldWrite? = null,
  ) : CustomFieldConditionWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "CustomFieldCondition")
  }

  @SerialName("FieldBasedCondition")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = FieldBasedCondition.Serializer::class)
  public data class FieldBasedCondition(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: ProjectCustomFieldWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: ProjectCustomFieldWrite.BundleProjectCustomField? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val showForNullValue: Boolean? = null,
  ) : CustomFieldConditionWrite {
    public object Serializer : TaggedCaseSerializer<FieldBasedCondition>(FieldBasedCondition.generatedSerializer(), "${'$'}type", "FieldBasedCondition")
  }

  public object Serializer : JsonContentPolymorphicSerializer<CustomFieldConditionWrite>(CustomFieldConditionWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<CustomFieldConditionWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "CustomFieldCondition" -> Default.serializer()
      "FieldBasedCondition" -> FieldBasedCondition.serializer()
      else -> throw SerializationException("'${'$'}type' of CustomFieldConditionWrite selects none of CustomFieldCondition, FieldBasedCondition: " + tag)
    }
  }
}
