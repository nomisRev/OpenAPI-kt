package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface CustomFieldDefaultsRequest {
    val canBeEmpty: Boolean?
    val emptyFieldText: String?
    val isPublic: Boolean?
    val parent: CustomFieldRequest?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("BundleCustomFieldDefaults")
    @Serializable
    data class BundleCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("UserCustomFieldDefaults")
    @Serializable
    data class UserCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
        val bundle: UserBundle? = null,
        val defaultValues: List<UserRequest>? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("BuildBundleCustomFieldDefaults")
    @Serializable
    data class BuildBundleCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
        val bundle: BuildBundle? = null,
        val defaultValues: List<BuildBundleElement>? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("StateBundleCustomFieldDefaults")
    @Serializable
    data class StateBundleCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
        val bundle: StateBundle? = null,
        val defaultValues: List<StateBundleElement>? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("VersionBundleCustomFieldDefaults")
    @Serializable
    data class VersionBundleCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
        val bundle: VersionBundle? = null,
        val defaultValues: List<VersionBundleElement>? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("EnumBundleCustomFieldDefaults")
    @Serializable
    data class EnumBundleCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
        val bundle: EnumBundle? = null,
        val defaultValues: List<EnumBundleElement>? = null,
    ) : CustomFieldDefaultsRequest

    @SerialName("OwnedBundleCustomFieldDefaults")
    @Serializable
    data class OwnedBundleCustomFieldDefaults(
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val isPublic: Boolean? = null,
        override val parent: CustomFieldRequest? = null,
        val bundle: OwnedBundle? = null,
        val defaultValues: List<OwnedBundleElement>? = null,
    ) : CustomFieldDefaultsRequest
}
