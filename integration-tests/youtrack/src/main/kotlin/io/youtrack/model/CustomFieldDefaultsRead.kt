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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("BundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("UserCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.UserBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<UserRead>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("BuildBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BuildBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.BuildBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.BuildBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("StateBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class StateBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.StateBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.StateBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("VersionBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VersionBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.VersionBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.VersionBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("EnumBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class EnumBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.EnumBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.EnumBundleElement>? = null,
  ) : CustomFieldDefaultsRead

  @SerialName("OwnedBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OwnedBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val parent: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.OwnedBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : CustomFieldDefaultsRead
}
