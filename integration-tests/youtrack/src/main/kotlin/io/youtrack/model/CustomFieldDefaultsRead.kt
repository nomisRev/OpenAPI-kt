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
public sealed interface CustomFieldDefaultsRead {
  public val id: String?

  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val isPublic: Boolean?

  public val parent: CustomFieldRead?

  @SerialName("CustomFieldDefaults")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("BundleCustomFieldDefaults")
  @Serializable
  public data class BundleCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("UserCustomFieldDefaults")
  @Serializable
  public data class UserCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
    public val bundle: BundleRead.UserBundle? = null,
    public val defaultValues: List<UserRead>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("BuildBundleCustomFieldDefaults")
  @Serializable
  public data class BuildBundleCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
    public val bundle: BundleRead.BuildBundle? = null,
    public val defaultValues: List<BundleElementRead.BuildBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("StateBundleCustomFieldDefaults")
  @Serializable
  public data class StateBundleCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
    public val bundle: BundleRead.StateBundle? = null,
    public val defaultValues: List<BundleElementRead.StateBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("VersionBundleCustomFieldDefaults")
  @Serializable
  public data class VersionBundleCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
    public val bundle: BundleRead.VersionBundle? = null,
    public val defaultValues: List<BundleElementRead.VersionBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("EnumBundleCustomFieldDefaults")
  @Serializable
  public data class EnumBundleCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
    public val bundle: BundleRead.EnumBundle? = null,
    public val defaultValues: List<BundleElementRead.EnumBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("OwnedBundleCustomFieldDefaults")
  @Serializable
  public data class OwnedBundleCustomFieldDefaults(
    override val id: String? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val isPublic: Boolean? = null,
    override val parent: CustomFieldRead? = null,
    public val bundle: BundleRead.OwnedBundle? = null,
    public val defaultValues: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : CustomFieldDefaultsRead
}
