package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
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
public sealed interface CustomFieldConditionRead {
  public val id: String?

  public val parent: ProjectCustomFieldRead?

  @SerialName("CustomFieldCondition")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: ProjectCustomFieldRead? = null,
  ) : CustomFieldConditionRead

  @SerialName("FieldBasedCondition")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class FieldBasedCondition(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `field`: ProjectCustomFieldRead.BundleProjectCustomField? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val showForNullValue: Boolean? = null,
  ) : CustomFieldConditionRead
}
