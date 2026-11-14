package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface BundleWrite {
  @SerialName("Bundle")
  @Serializable
  public data object Default : BundleWrite

  @SerialName("BaseBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BaseBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite>? = null,
  ) : BundleWrite

  @SerialName("StateBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class StateBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.StateBundleElement>? = null,
  ) : BundleWrite

  @SerialName("EnumBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class EnumBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : BundleWrite

  @SerialName("UserBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val groups: List<UserGroupWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val individuals: List<UserWrite>? = null,
  ) : BundleWrite

  @SerialName("OwnedBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OwnedBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : BundleWrite

  @SerialName("VersionBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VersionBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : BundleWrite

  @SerialName("BuildBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BuildBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : BundleWrite
}
