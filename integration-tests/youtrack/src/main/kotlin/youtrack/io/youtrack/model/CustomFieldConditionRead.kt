package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
public sealed interface CustomFieldConditionRead {
  public val id: String?

  public val parent: ProjectCustomFieldRead?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val parent: ProjectCustomFieldRead? = null,
  ) : CustomFieldConditionRead

  @SerialName("FieldBasedCondition")
  @Serializable
  public data class FieldBasedCondition(
    override val id: String? = null,
    override val parent: ProjectCustomFieldRead? = null,
    public val `field`: ProjectCustomFieldRead.BundleProjectCustomField? = null,
    public val values: List<BundleElementRead>? = null,
    public val showForNullValue: Boolean? = null,
  ) : CustomFieldConditionRead
}
