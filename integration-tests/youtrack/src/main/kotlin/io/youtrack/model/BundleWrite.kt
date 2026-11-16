package io.youtrack.model

import kotlin.OptIn
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

@Serializable(with = BundleWrite.Serializer::class)
public sealed interface BundleWrite {
  @SerialName("Bundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : BundleWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "Bundle")
  }

  @SerialName("BaseBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BaseBundle.Serializer::class)
  public data class BaseBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<BaseBundle>(BaseBundle.generatedSerializer(), "${'$'}type", "BaseBundle")
  }

  @SerialName("StateBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateBundle.Serializer::class)
  public data class StateBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.StateBundleElement>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<StateBundle>(StateBundle.generatedSerializer(), "${'$'}type", "StateBundle")
  }

  @SerialName("EnumBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = EnumBundle.Serializer::class)
  public data class EnumBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<EnumBundle>(EnumBundle.generatedSerializer(), "${'$'}type", "EnumBundle")
  }

  @SerialName("UserBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = UserBundle.Serializer::class)
  public data class UserBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val groups: List<UserGroupWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val individuals: List<UserWrite>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<UserBundle>(UserBundle.generatedSerializer(), "${'$'}type", "UserBundle")
  }

  @SerialName("OwnedBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = OwnedBundle.Serializer::class)
  public data class OwnedBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<OwnedBundle>(OwnedBundle.generatedSerializer(), "${'$'}type", "OwnedBundle")
  }

  @SerialName("VersionBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VersionBundle.Serializer::class)
  public data class VersionBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<VersionBundle>(VersionBundle.generatedSerializer(), "${'$'}type", "VersionBundle")
  }

  @SerialName("BuildBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BuildBundle.Serializer::class)
  public data class BuildBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : BundleWrite {
    public object Serializer : TaggedCaseSerializer<BuildBundle>(BuildBundle.generatedSerializer(), "${'$'}type", "BuildBundle")
  }

  public object Serializer : JsonContentPolymorphicSerializer<BundleWrite>(BundleWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<BundleWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "Bundle" -> Default.serializer()
      "BaseBundle" -> BaseBundle.serializer()
      "StateBundle" -> StateBundle.serializer()
      "EnumBundle" -> EnumBundle.serializer()
      "UserBundle" -> UserBundle.serializer()
      "OwnedBundle" -> OwnedBundle.serializer()
      "VersionBundle" -> VersionBundle.serializer()
      "BuildBundle" -> BuildBundle.serializer()
      else -> throw SerializationException("'${'$'}type' of BundleWrite selects none of Bundle, BaseBundle, StateBundle, EnumBundle, UserBundle, OwnedBundle, VersionBundle, BuildBundle: " + tag)
    }
  }
}
