package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents the condition for showing a custom field.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface CustomFieldConditionWrite {
  public val parent: ProjectCustomFieldWrite?

  @SerialName("CustomFieldCondition")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: ProjectCustomFieldWrite? = null,
  ) : CustomFieldConditionWrite

  @SerialName("FieldBasedCondition")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class FieldBasedCondition(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: ProjectCustomFieldWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: ProjectCustomFieldWrite.BundleProjectCustomField? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val showForNullValue: Boolean? = null,
  ) : CustomFieldConditionWrite
}
