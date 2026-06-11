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
public sealed interface BundleElementWrite {
  public val name: String?

  public val description: String?

  public val archived: Boolean?

  public val ordinal: Int?

  public val color: FieldStyleWrite?

  @SerialName("BundleElement")
  @Serializable
  public data class Default(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
  ) : BundleElementWrite

  @SerialName("LocalizableBundleElement")
  @Serializable
  public data class LocalizableBundleElement(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
    public val localizedName: String? = null,
  ) : BundleElementWrite

  @SerialName("StateBundleElement")
  @Serializable
  public data class StateBundleElement(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
    public val localizedName: String? = null,
    public val isResolved: Boolean? = null,
  ) : BundleElementWrite

  @SerialName("EnumBundleElement")
  @Serializable
  public data class EnumBundleElement(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
    public val localizedName: String? = null,
  ) : BundleElementWrite

  @SerialName("OwnedBundleElement")
  @Serializable
  public data class OwnedBundleElement(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
    public val owner: UserWrite? = null,
  ) : BundleElementWrite

  @SerialName("VersionBundleElement")
  @Serializable
  public data class VersionBundleElement(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
    public val released: Boolean? = null,
    public val releaseDate: Long? = null,
    public val startDate: Long? = null,
  ) : BundleElementWrite

  @SerialName("BuildBundleElement")
  @Serializable
  public data class BuildBundleElement(
    override val name: String? = null,
    override val description: String? = null,
    override val archived: Boolean? = null,
    override val ordinal: Int? = null,
    override val color: FieldStyleWrite? = null,
    public val assembleDate: Long? = null,
  ) : BundleElementWrite
}
