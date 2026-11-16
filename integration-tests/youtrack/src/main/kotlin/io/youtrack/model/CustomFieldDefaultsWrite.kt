package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
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

@Serializable(with = CustomFieldDefaultsWrite.Serializer::class)
public sealed interface CustomFieldDefaultsWrite {
  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val isPublic: Boolean?

  @SerialName("CustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "CustomFieldDefaults")
  }

  @SerialName("BundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BundleCustomFieldDefaults.Serializer::class)
  public data class BundleCustomFieldDefaults(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<BundleCustomFieldDefaults>(BundleCustomFieldDefaults.generatedSerializer(), "${'$'}type", "BundleCustomFieldDefaults")
  }

  @SerialName("UserCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = UserCustomFieldDefaults.Serializer::class)
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
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<UserCustomFieldDefaults>(UserCustomFieldDefaults.generatedSerializer(), "${'$'}type", "UserCustomFieldDefaults")
  }

  @SerialName("BuildBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BuildBundleCustomFieldDefaults.Serializer::class)
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
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<BuildBundleCustomFieldDefaults>(BuildBundleCustomFieldDefaults.generatedSerializer(), "${'$'}type", "BuildBundleCustomFieldDefaults")
  }

  @SerialName("StateBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateBundleCustomFieldDefaults.Serializer::class)
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
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<StateBundleCustomFieldDefaults>(StateBundleCustomFieldDefaults.generatedSerializer(), "${'$'}type", "StateBundleCustomFieldDefaults")
  }

  @SerialName("VersionBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VersionBundleCustomFieldDefaults.Serializer::class)
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
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<VersionBundleCustomFieldDefaults>(VersionBundleCustomFieldDefaults.generatedSerializer(), "${'$'}type", "VersionBundleCustomFieldDefaults")
  }

  @SerialName("EnumBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = EnumBundleCustomFieldDefaults.Serializer::class)
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
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<EnumBundleCustomFieldDefaults>(EnumBundleCustomFieldDefaults.generatedSerializer(), "${'$'}type", "EnumBundleCustomFieldDefaults")
  }

  @SerialName("OwnedBundleCustomFieldDefaults")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = OwnedBundleCustomFieldDefaults.Serializer::class)
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
  ) : CustomFieldDefaultsWrite {
    public object Serializer : TaggedCaseSerializer<OwnedBundleCustomFieldDefaults>(OwnedBundleCustomFieldDefaults.generatedSerializer(), "${'$'}type", "OwnedBundleCustomFieldDefaults")
  }

  public object Serializer : JsonContentPolymorphicSerializer<CustomFieldDefaultsWrite>(CustomFieldDefaultsWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<CustomFieldDefaultsWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "CustomFieldDefaults" -> Default.serializer()
      "BundleCustomFieldDefaults" -> BundleCustomFieldDefaults.serializer()
      "UserCustomFieldDefaults" -> UserCustomFieldDefaults.serializer()
      "BuildBundleCustomFieldDefaults" -> BuildBundleCustomFieldDefaults.serializer()
      "StateBundleCustomFieldDefaults" -> StateBundleCustomFieldDefaults.serializer()
      "VersionBundleCustomFieldDefaults" -> VersionBundleCustomFieldDefaults.serializer()
      "EnumBundleCustomFieldDefaults" -> EnumBundleCustomFieldDefaults.serializer()
      "OwnedBundleCustomFieldDefaults" -> OwnedBundleCustomFieldDefaults.serializer()
      else -> throw SerializationException("'${'$'}type' of CustomFieldDefaultsWrite selects none of CustomFieldDefaults, BundleCustomFieldDefaults, UserCustomFieldDefaults, BuildBundleCustomFieldDefaults, StateBundleCustomFieldDefaults, VersionBundleCustomFieldDefaults, EnumBundleCustomFieldDefaults, OwnedBundleCustomFieldDefaults: " + tag)
    }
  }
}
