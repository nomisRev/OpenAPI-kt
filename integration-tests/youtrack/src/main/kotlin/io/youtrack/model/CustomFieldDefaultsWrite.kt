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
public sealed interface CustomFieldDefaultsWrite {
  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val isPublic: Boolean?

  @SerialName("CustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("BundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("UserCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.UserBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<UserWrite>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("BuildBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BuildBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.BuildBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("StateBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class StateBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.StateBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.StateBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("VersionBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VersionBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.VersionBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("EnumBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class EnumBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.EnumBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : CustomFieldDefaultsWrite

  @SerialName("OwnedBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OwnedBundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.OwnedBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : CustomFieldDefaultsWrite
}
