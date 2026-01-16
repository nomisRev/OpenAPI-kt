package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface ProjectCustomFieldRequest {
    val field: CustomFieldRequest?
    val project: Project?
    val canBeEmpty: Boolean?
    val emptyFieldText: String?
    val ordinal: Int?
    val isPublic: Boolean?
    val condition: CustomFieldConditionRequest?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("GroupProjectCustomField")
    @Serializable
    data class GroupProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val defaultValues: List<UserGroupRequest>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("BundleProjectCustomField")
    @Serializable
    data class BundleProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("UserProjectCustomField")
    @Serializable
    data class UserProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val bundle: UserBundle? = null,
        val defaultValues: List<UserRequest>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("BuildProjectCustomField")
    @Serializable
    data class BuildProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val bundle: BuildBundle? = null,
        val defaultValues: List<BuildBundleElement>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("StateProjectCustomField")
    @Serializable
    data class StateProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val bundle: StateBundle? = null,
        val defaultValues: List<StateBundleElement>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("VersionProjectCustomField")
    @Serializable
    data class VersionProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val bundle: VersionBundle? = null,
        val defaultValues: List<VersionBundleElement>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("OwnedProjectCustomField")
    @Serializable
    data class OwnedProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val bundle: OwnedBundle? = null,
        val defaultValues: List<OwnedBundleElement>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("EnumProjectCustomField")
    @Serializable
    data class EnumProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
        val bundle: EnumBundle? = null,
        val defaultValues: List<EnumBundleElement>? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("SimpleProjectCustomField")
    @Serializable
    data class SimpleProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("PeriodProjectCustomField")
    @Serializable
    data class PeriodProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
    ) : ProjectCustomFieldRequest

    @SerialName("TextProjectCustomField")
    @Serializable
    data class TextProjectCustomField(
        override val field: CustomFieldRequest? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val condition: CustomFieldConditionRequest? = null,
    ) : ProjectCustomFieldRequest
}
