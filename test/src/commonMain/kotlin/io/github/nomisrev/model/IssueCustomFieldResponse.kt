package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface IssueCustomFieldResponse {
    val id: String?
    val name: String?
    val projectCustomField: ProjectCustomFieldResponse?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
    ) : IssueCustomFieldResponse

    @SerialName("PeriodIssueCustomField")
    @Serializable
    data class PeriodIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: PeriodValueResponse? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SimpleIssueCustomField")
    @Serializable
    data class SimpleIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: JsonElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("DateIssueCustomField")
    @Serializable
    data class DateIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: JsonElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleValueIssueCustomField")
    @Serializable
    data class SingleValueIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: JsonElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("StateIssueCustomField")
    @Serializable
    data class StateIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: StateBundleElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleBuildIssueCustomField")
    @Serializable
    data class SingleBuildIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: BuildBundleElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleUserIssueCustomField")
    @Serializable
    data class SingleUserIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: UserResponse? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleGroupIssueCustomField")
    @Serializable
    data class SingleGroupIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: UserGroupResponse? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleVersionIssueCustomField")
    @Serializable
    data class SingleVersionIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: VersionBundleElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleOwnedIssueCustomField")
    @Serializable
    data class SingleOwnedIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: OwnedBundleElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("SingleEnumIssueCustomField")
    @Serializable
    data class SingleEnumIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: EnumBundleElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiValueIssueCustomField")
    @Serializable
    data class MultiValueIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: JsonElement? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiBuildIssueCustomField")
    @Serializable
    data class MultiBuildIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: List<BuildBundleElement>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiGroupIssueCustomField")
    @Serializable
    data class MultiGroupIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: List<UserGroupResponse>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiVersionIssueCustomField")
    @Serializable
    data class MultiVersionIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: List<VersionBundleElement>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiOwnedIssueCustomField")
    @Serializable
    data class MultiOwnedIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: List<OwnedBundleElement>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiEnumIssueCustomField")
    @Serializable
    data class MultiEnumIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: List<EnumBundleElement>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("MultiUserIssueCustomField")
    @Serializable
    data class MultiUserIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: List<UserResponse>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("StateMachineIssueCustomField")
    @Serializable
    data class StateMachineIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: JsonElement? = null,
        val event: EventResponse? = null,
        val possibleEvents: List<EventResponse>? = null,
    ) : IssueCustomFieldResponse

    @SerialName("TextIssueCustomField")
    @Serializable
    data class TextIssueCustomField(
        override val id: String? = null,
        override val name: String? = null,
        override val projectCustomField: ProjectCustomFieldResponse? = null,
        val value: TextFieldValueResponse? = null,
    ) : IssueCustomFieldResponse
}
