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

@Serializable(with = BundleRead.Serializer::class)
public sealed interface BundleRead {
  public val id: String?

  public val isUpdateable: Boolean?

  @SerialName("Bundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "Bundle")
  }

  @SerialName("BaseBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BaseBundle.Serializer::class)
  public data class BaseBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<BaseBundle>(BaseBundle.generatedSerializer(), "${'$'}type", "BaseBundle")
  }

  @SerialName("StateBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateBundle.Serializer::class)
  public data class StateBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.StateBundleElement>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<StateBundle>(StateBundle.generatedSerializer(), "${'$'}type", "StateBundle")
  }

  @SerialName("EnumBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = EnumBundle.Serializer::class)
  public data class EnumBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.EnumBundleElement>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<EnumBundle>(EnumBundle.generatedSerializer(), "${'$'}type", "EnumBundle")
  }

  @SerialName("UserBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = UserBundle.Serializer::class)
  public data class UserBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val groups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val individuals: List<UserRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val aggregatedUsers: List<UserRead>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<UserBundle>(UserBundle.generatedSerializer(), "${'$'}type", "UserBundle")
  }

  @SerialName("OwnedBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = OwnedBundle.Serializer::class)
  public data class OwnedBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<OwnedBundle>(OwnedBundle.generatedSerializer(), "${'$'}type", "OwnedBundle")
  }

  @SerialName("VersionBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VersionBundle.Serializer::class)
  public data class VersionBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.VersionBundleElement>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<VersionBundle>(VersionBundle.generatedSerializer(), "${'$'}type", "VersionBundle")
  }

  @SerialName("BuildBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BuildBundle.Serializer::class)
  public data class BuildBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.BuildBundleElement>? = null,
  ) : BundleRead {
    public object Serializer : TaggedCaseSerializer<BuildBundle>(BuildBundle.generatedSerializer(), "${'$'}type", "BuildBundle")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BundleRead>(BundleRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BundleRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "Bundle" -> Default.serializer()
      "BaseBundle" -> BaseBundle.serializer()
      "StateBundle" -> StateBundle.serializer()
      "EnumBundle" -> EnumBundle.serializer()
      "UserBundle" -> UserBundle.serializer()
      "OwnedBundle" -> OwnedBundle.serializer()
      "VersionBundle" -> VersionBundle.serializer()
      "BuildBundle" -> BuildBundle.serializer()
      else -> throw SerializationException("'${'$'}type' of BundleRead selects none of Bundle, BaseBundle, StateBundle, EnumBundle, UserBundle, OwnedBundle, VersionBundle, BuildBundle: " + tag)
    }
  }
}
