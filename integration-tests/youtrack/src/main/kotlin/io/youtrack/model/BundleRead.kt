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
public sealed interface BundleRead {
  public val id: String?

  public val isUpdateable: Boolean?

  @SerialName("Bundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
  ) : BundleRead

  @SerialName("BaseBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BaseBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead>? = null,
  ) : BundleRead

  @SerialName("StateBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class StateBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.StateBundleElement>? = null,
  ) : BundleRead

  @SerialName("EnumBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class EnumBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.EnumBundleElement>? = null,
  ) : BundleRead

  @SerialName("UserBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleRead

  @SerialName("OwnedBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OwnedBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : BundleRead

  @SerialName("VersionBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VersionBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.VersionBundleElement>? = null,
  ) : BundleRead

  @SerialName("BuildBundle")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BuildBundle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isUpdateable: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val values: List<BundleElementRead.BuildBundleElement>? = null,
  ) : BundleRead
}
