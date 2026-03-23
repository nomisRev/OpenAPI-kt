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
public sealed interface BundleRead {
  public val id: String?

  public val isUpdateable: Boolean?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
  ) : BundleRead

  @SerialName("BaseBundle")
  @Serializable
  public data class BaseBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val values: List<BundleElementRead>? = null,
  ) : BundleRead

  @SerialName("StateBundle")
  @Serializable
  public data class StateBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val values: List<BundleElementRead.StateBundleElement>? = null,
  ) : BundleRead

  @SerialName("EnumBundle")
  @Serializable
  public data class EnumBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val values: List<BundleElementRead.EnumBundleElement>? = null,
  ) : BundleRead

  @SerialName("UserBundle")
  @Serializable
  public data class UserBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val groups: List<UserGroupRead>? = null,
    public val individuals: List<UserRead>? = null,
    public val aggregatedUsers: List<UserRead>? = null,
  ) : BundleRead

  @SerialName("OwnedBundle")
  @Serializable
  public data class OwnedBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val values: List<BundleElementRead>? = null,
  ) : BundleRead

  @SerialName("VersionBundle")
  @Serializable
  public data class VersionBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val values: List<BundleElementRead>? = null,
  ) : BundleRead

  @SerialName("BuildBundle")
  @Serializable
  public data class BuildBundle(
    override val id: String? = null,
    override val isUpdateable: Boolean? = null,
    public val values: List<BundleElementRead>? = null,
  ) : BundleRead
}
