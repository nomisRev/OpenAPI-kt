package io.youtrack.model

import kotlin.Boolean
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
public sealed interface CustomFieldDefaultsWrite {
  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val isPublic: Boolean?

  @SerialName("CustomFieldDefaults")
  @Serializable
  public data class Default(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("BundleCustomFieldDefaults")
  @Serializable
  public data class BundleCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("UserCustomFieldDefaults")
  @Serializable
  public data class UserCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    public val bundle: BundleWrite.UserBundle? = null,
    public val defaultValues: List<UserWrite>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("BuildBundleCustomFieldDefaults")
  @Serializable
  public data class BuildBundleCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    public val bundle: BundleWrite.BuildBundle? = null,
    public val defaultValues: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("StateBundleCustomFieldDefaults")
  @Serializable
  public data class StateBundleCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    public val bundle: BundleWrite.StateBundle? = null,
    public val defaultValues: List<BundleElementWrite.StateBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("VersionBundleCustomFieldDefaults")
  @Serializable
  public data class VersionBundleCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    public val bundle: BundleWrite.VersionBundle? = null,
    public val defaultValues: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("EnumBundleCustomFieldDefaults")
  @Serializable
  public data class EnumBundleCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    public val bundle: BundleWrite.EnumBundle? = null,
    public val defaultValues: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("OwnedBundleCustomFieldDefaults")
  @Serializable
  public data class OwnedBundleCustomFieldDefaults(
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    public val bundle: BundleWrite.OwnedBundle? = null,
    public val defaultValues: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : CustomFieldDefaultsWrite
}
