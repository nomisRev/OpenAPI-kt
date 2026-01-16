package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface CustomFieldDefaultsResponse {
    val id: String?
    val canBeEmpty: Boolean?
    val emptyFieldText: String?
    val isPublic: Boolean?
    val parent: CustomFieldResponse?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("BundleCustomFieldDefaults")
    @Serializable
    data class BundleCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("UserCustomFieldDefaults")
    @Serializable
    data class UserCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
        val bundle: UserBundle? = null,
        val defaultValues: List<UserResponse>? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("BuildBundleCustomFieldDefaults")
    @Serializable
    data class BuildBundleCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
        val bundle: BuildBundle? = null,
        val defaultValues: List<BuildBundleElement>? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("StateBundleCustomFieldDefaults")
    @Serializable
    data class StateBundleCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
        val bundle: StateBundle? = null,
        val defaultValues: List<StateBundleElement>? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("VersionBundleCustomFieldDefaults")
    @Serializable
    data class VersionBundleCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
        val bundle: VersionBundle? = null,
        val defaultValues: List<VersionBundleElement>? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("EnumBundleCustomFieldDefaults")
    @Serializable
    data class EnumBundleCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
        val bundle: EnumBundle? = null,
        val defaultValues: List<EnumBundleElement>? = null,
    ) : CustomFieldDefaultsResponse

    @SerialName("OwnedBundleCustomFieldDefaults")
    @Serializable
    data class OwnedBundleCustomFieldDefaults(
        override val id: String? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldResponse? = null,
        val bundle: OwnedBundle? = null,
        val defaultValues: List<OwnedBundleElement>? = null,
    ) : CustomFieldDefaultsResponse
}
