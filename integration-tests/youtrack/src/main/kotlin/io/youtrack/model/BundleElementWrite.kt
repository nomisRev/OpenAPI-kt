package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Represents a field value in YouTrack.
 */
@Serializable(with = BundleElementWrite.Serializer::class)
public sealed interface BundleElementWrite {
  public val name: String?

  public val description: String?

  public val archived: Boolean?

  public val ordinal: Int?

  public val color: FieldStyleWrite?

  @SerialName("BundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "BundleElement")
  }

  @SerialName("LocalizableBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = LocalizableBundleElement.Serializer::class)
  public data class LocalizableBundleElement(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val localizedName: String? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<LocalizableBundleElement>(LocalizableBundleElement.generatedSerializer(), "${'$'}type", "LocalizableBundleElement")
  }

  @SerialName("StateBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateBundleElement.Serializer::class)
  public data class StateBundleElement(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val localizedName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isResolved: Boolean? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<StateBundleElement>(StateBundleElement.generatedSerializer(), "${'$'}type", "StateBundleElement")
  }

  @SerialName("EnumBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = EnumBundleElement.Serializer::class)
  public data class EnumBundleElement(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val localizedName: String? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<EnumBundleElement>(EnumBundleElement.generatedSerializer(), "${'$'}type", "EnumBundleElement")
  }

  @SerialName("OwnedBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = OwnedBundleElement.Serializer::class)
  public data class OwnedBundleElement(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: UserWrite? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<OwnedBundleElement>(OwnedBundleElement.generatedSerializer(), "${'$'}type", "OwnedBundleElement")
  }

  @SerialName("VersionBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VersionBundleElement.Serializer::class)
  public data class VersionBundleElement(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val released: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val releaseDate: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val startDate: Long? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<VersionBundleElement>(VersionBundleElement.generatedSerializer(), "${'$'}type", "VersionBundleElement")
  }

  @SerialName("BuildBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BuildBundleElement.Serializer::class)
  public data class BuildBundleElement(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val assembleDate: Long? = null,
  ) : BundleElementWrite {
    public object Serializer : TaggedCaseSerializer<BuildBundleElement>(BuildBundleElement.generatedSerializer(), "${'$'}type", "BuildBundleElement")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BundleElementWrite>(BundleElementWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BundleElementWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "BundleElement" -> Default.serializer()
      "LocalizableBundleElement" -> LocalizableBundleElement.serializer()
      "StateBundleElement" -> StateBundleElement.serializer()
      "EnumBundleElement" -> EnumBundleElement.serializer()
      "OwnedBundleElement" -> OwnedBundleElement.serializer()
      "VersionBundleElement" -> VersionBundleElement.serializer()
      "BuildBundleElement" -> BuildBundleElement.serializer()
      else -> throw SerializationException("'${'$'}type' of BundleElementWrite selects none of BundleElement, LocalizableBundleElement, StateBundleElement, EnumBundleElement, OwnedBundleElement, VersionBundleElement, BuildBundleElement: " + tag)
    }
  }
}
