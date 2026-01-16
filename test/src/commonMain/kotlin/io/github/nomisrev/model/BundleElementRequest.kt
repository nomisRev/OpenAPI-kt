package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BundleElementRequest {
    val name: String?
    val bundle: BundleRequest?
    val description: String?
    val archived: Boolean?
    val ordinal: Int?
    val color: FieldStyleRequest?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
    ) : BundleElementRequest

    @SerialName("LocalizableBundleElement")
    @Serializable
    data class LocalizableBundleElement(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
        val localizedName: String? = null,
    ) : BundleElementRequest

    @SerialName("StateBundleElement")
    @Serializable
    data class StateBundleElement(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
        val localizedName: String? = null,
        val isResolved: Boolean? = null,
    ) : BundleElementRequest

    @SerialName("EnumBundleElement")
    @Serializable
    data class EnumBundleElement(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
        val localizedName: String? = null,
    ) : BundleElementRequest

    @SerialName("OwnedBundleElement")
    @Serializable
    data class OwnedBundleElement(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
        val owner: UserRequest? = null,
    ) : BundleElementRequest

    @SerialName("VersionBundleElement")
    @Serializable
    data class VersionBundleElement(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
        val released: Boolean? = null,
        val releaseDate: Long? = null,
        val startDate: Long? = null,
    ) : BundleElementRequest

    @SerialName("BuildBundleElement")
    @Serializable
    data class BuildBundleElement(
        override val name: String? = null,
        override val bundle: BundleRequest? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleRequest? = null,
        val assembleDate: Long? = null,
    ) : BundleElementRequest
}
