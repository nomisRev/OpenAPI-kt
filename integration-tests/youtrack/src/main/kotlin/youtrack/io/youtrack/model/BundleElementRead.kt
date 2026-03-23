package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents a field value in YouTrack.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface BundleElementRead {
  public val id: String?

  public val name: String?

  public val bundle: BundleRead?

  public val description: String?

  public val archived: Boolean?

  public val ordinal: Int?

  public val color: FieldStyleRead?

  public val hasRunningJob: Boolean?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
  ) : BundleElementRead

  @SerialName("LocalizableBundleElement")
  @Serializable
  public data class LocalizableBundleElement(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
    public val localizedName: String? = null,
  ) : BundleElementRead

  @SerialName("StateBundleElement")
  @Serializable
  public data class StateBundleElement(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
    public val localizedName: String? = null,
    public val isResolved: Boolean? = null,
  ) : BundleElementRead

  @SerialName("EnumBundleElement")
  @Serializable
  public data class EnumBundleElement(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
    public val localizedName: String? = null,
  ) : BundleElementRead

  @SerialName("OwnedBundleElement")
  @Serializable
  public data class OwnedBundleElement(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
    public val owner: UserRead? = null,
  ) : BundleElementRead

  @SerialName("VersionBundleElement")
  @Serializable
  public data class VersionBundleElement(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
    public val released: Boolean? = null,
    public val releaseDate: Long? = null,
    public val startDate: Long? = null,
  ) : BundleElementRead

  @SerialName("BuildBundleElement")
  @Serializable
  public data class BuildBundleElement(
    override val id: String? = null,
    override val name: String? = null,
    override val bundle: BundleRead? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleRead? = null,
    override val hasRunningJob: Boolean? = null,
    public val assembleDate: Long? = null,
  ) : BundleElementRead
}
