package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface BundleWrite {
  @SerialName("Default")
  @Serializable
  public data object Default : BundleWrite

  @SerialName("BaseBundle")
  @Serializable
  public data class BaseBundle(
    public val values: List<BundleElementWrite>? = null,
  ) : BundleWrite

  @SerialName("StateBundle")
  @Serializable
  public data class StateBundle(
    public val values: List<BundleElementWrite.StateBundleElement>? = null,
  ) : BundleWrite

  @SerialName("EnumBundle")
  @Serializable
  public data class EnumBundle(
    public val values: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : BundleWrite

  @SerialName("UserBundle")
  @Serializable
  public data class UserBundle(
    public val groups: List<UserGroupWrite>? = null,
    public val individuals: List<UserWrite>? = null,
  ) : BundleWrite

  @SerialName("OwnedBundle")
  @Serializable
  public data class OwnedBundle(
    public val values: List<BundleElementWrite>? = null,
  ) : BundleWrite

  @SerialName("VersionBundle")
  @Serializable
  public data class VersionBundle(
    public val values: List<BundleElementWrite>? = null,
  ) : BundleWrite

  @SerialName("BuildBundle")
  @Serializable
  public data class BuildBundle(
    public val values: List<BundleElementWrite>? = null,
  ) : BundleWrite
}
