package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.collections.List
import kotlin.jvm.JvmInline
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

  @JvmInline
  @SerialName("Default")
  @Serializable
  public value class Default(
    override val parent: ProjectCustomFieldWrite? = null,
  ) : CustomFieldConditionWrite

  @SerialName("FieldBasedCondition")
  @Serializable
  public data class FieldBasedCondition(
    override val parent: ProjectCustomFieldWrite? = null,
    public val `field`: ProjectCustomFieldWrite.BundleProjectCustomField? = null,
    public val values: List<BundleElementWrite>? = null,
    public val showForNullValue: Boolean? = null,
  ) : CustomFieldConditionWrite
}
