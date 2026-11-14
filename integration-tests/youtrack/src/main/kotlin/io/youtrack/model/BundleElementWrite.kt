package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite

  @SerialName("LocalizableBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite

  @SerialName("StateBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite

  @SerialName("EnumBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite

  @SerialName("OwnedBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite

  @SerialName("VersionBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite

  @SerialName("BuildBundleElement")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : BundleElementWrite
}
