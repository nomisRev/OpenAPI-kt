package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface BundleElementResponse {
    val id: String?
    val name: String?
    val bundle: BundleResponse?
    val description: String?
    val archived: Boolean?
    val ordinal: Int?
    val color: FieldStyleResponse?
    val hasRunningJob: Boolean?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
    ) : BundleElementResponse

    @SerialName("LocalizableBundleElement")
    @Serializable
    data class LocalizableBundleElement(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
        val localizedName: String? = null,
    ) : BundleElementResponse

    @SerialName("StateBundleElement")
    @Serializable
    data class StateBundleElement(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
        val localizedName: String? = null,
        val isResolved: Boolean? = null,
    ) : BundleElementResponse

    @SerialName("EnumBundleElement")
    @Serializable
    data class EnumBundleElement(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
        val localizedName: String? = null,
    ) : BundleElementResponse

    @SerialName("OwnedBundleElement")
    @Serializable
    data class OwnedBundleElement(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
        val owner: UserResponse? = null,
    ) : BundleElementResponse

    @SerialName("VersionBundleElement")
    @Serializable
    data class VersionBundleElement(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
        val released: Boolean? = null,
        val releaseDate: Long? = null,
        val startDate: Long? = null,
    ) : BundleElementResponse

    @SerialName("BuildBundleElement")
    @Serializable
    data class BuildBundleElement(
        override val id: String? = null,
        override val name: String? = null,
        override val bundle: BundleResponse? = null,
        override val description: String? = null,
        override val archived: Boolean? = null,
        override val ordinal: Int? = null,
        override val color: FieldStyleResponse? = null,
        override val hasRunningJob: Boolean? = null,
        val assembleDate: Long? = null,
    ) : BundleElementResponse
}
