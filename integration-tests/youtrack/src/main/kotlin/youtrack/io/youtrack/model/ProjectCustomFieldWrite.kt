package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface ProjectCustomFieldWrite {
  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val ordinal: Int?

  public val isPublic: Boolean?

  public val condition: CustomFieldConditionWrite?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("GroupProjectCustomField")
  @Serializable
  public data class GroupProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val defaultValues: List<UserGroupWrite>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("BundleProjectCustomField")
  @Serializable
  public data class BundleProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("UserProjectCustomField")
  @Serializable
  public data class UserProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val bundle: BundleWrite.UserBundle? = null,
    public val defaultValues: List<UserWrite>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("BuildProjectCustomField")
  @Serializable
  public data class BuildProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val bundle: BundleWrite.BuildBundle? = null,
    public val defaultValues: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("StateProjectCustomField")
  @Serializable
  public data class StateProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val bundle: BundleWrite.StateBundle? = null,
    public val defaultValues: List<BundleElementWrite.StateBundleElement>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("VersionProjectCustomField")
  @Serializable
  public data class VersionProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val bundle: BundleWrite.VersionBundle? = null,
    public val defaultValues: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("OwnedProjectCustomField")
  @Serializable
  public data class OwnedProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val bundle: BundleWrite.OwnedBundle? = null,
    public val defaultValues: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("EnumProjectCustomField")
  @Serializable
  public data class EnumProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
    public val bundle: BundleWrite.EnumBundle? = null,
    public val defaultValues: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("SimpleProjectCustomField")
  @Serializable
  public data class SimpleProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("PeriodProjectCustomField")
  @Serializable
  public data class PeriodProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite

  @SerialName("TextProjectCustomField")
  @Serializable
  public data class TextProjectCustomField(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite
}
