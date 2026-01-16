package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface ProjectCustomFieldResponse {
    val id: String?
    val field: CustomFieldResponse?
    val project: Project?
    val canBeEmpty: Boolean?
    val emptyFieldText: String?
    val ordinal: Int?
    val isPublic: Boolean?
    val hasRunningJob: Boolean?
    val condition: CustomFieldConditionResponse?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("GroupProjectCustomField")
    @Serializable
    data class GroupProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val defaultValues: List<UserGroupResponse>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("BundleProjectCustomField")
    @Serializable
    data class BundleProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("UserProjectCustomField")
    @Serializable
    data class UserProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val bundle: UserBundle? = null,
        val defaultValues: List<UserResponse>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("BuildProjectCustomField")
    @Serializable
    data class BuildProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val bundle: BuildBundle? = null,
        val defaultValues: List<BuildBundleElement>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("StateProjectCustomField")
    @Serializable
    data class StateProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val bundle: StateBundle? = null,
        val defaultValues: List<StateBundleElement>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("VersionProjectCustomField")
    @Serializable
    data class VersionProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val bundle: VersionBundle? = null,
        val defaultValues: List<VersionBundleElement>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("OwnedProjectCustomField")
    @Serializable
    data class OwnedProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val bundle: OwnedBundle? = null,
        val defaultValues: List<OwnedBundleElement>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("EnumProjectCustomField")
    @Serializable
    data class EnumProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
        val bundle: EnumBundle? = null,
        val defaultValues: List<EnumBundleElement>? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("SimpleProjectCustomField")
    @Serializable
    data class SimpleProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("PeriodProjectCustomField")
    @Serializable
    data class PeriodProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
    ) : ProjectCustomFieldResponse

    @SerialName("TextProjectCustomField")
    @Serializable
    data class TextProjectCustomField(
        override val id: String? = null,
        override val field: CustomFieldResponse? = null,
        override val project: Project? = null,
        override val canBeEmpty: Boolean? = null,
        override val emptyFieldText: String? = null,
        override val ordinal: Int? = null,
        override val isPublic: Boolean? = null,
        override val hasRunningJob: Boolean? = null,
        override val condition: CustomFieldConditionResponse? = null,
    ) : ProjectCustomFieldResponse
}
