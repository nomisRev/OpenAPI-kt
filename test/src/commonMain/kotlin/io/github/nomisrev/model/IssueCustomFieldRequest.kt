package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface IssueCustomFieldRequest {
    val projectCustomField: ProjectCustomFieldRequest?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val projectCustomField: ProjectCustomFieldRequest? = null) : IssueCustomFieldRequest

    @SerialName("PeriodIssueCustomField")
    @Serializable
    data class PeriodIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: PeriodValueRequest? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SimpleIssueCustomField")
    @Serializable
    @JvmInline
    value class SimpleIssueCustomField(override val projectCustomField: ProjectCustomFieldRequest? = null) : IssueCustomFieldRequest

    @SerialName("DateIssueCustomField")
    @Serializable
    @JvmInline
    value class DateIssueCustomField(override val projectCustomField: ProjectCustomFieldRequest? = null) : IssueCustomFieldRequest

    @SerialName("SingleValueIssueCustomField")
    @Serializable
    @JvmInline
    value class SingleValueIssueCustomField(override val projectCustomField: ProjectCustomFieldRequest? = null) : IssueCustomFieldRequest

    @SerialName("StateIssueCustomField")
    @Serializable
    data class StateIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: StateBundleElement? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SingleBuildIssueCustomField")
    @Serializable
    data class SingleBuildIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: BuildBundleElement? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SingleUserIssueCustomField")
    @Serializable
    data class SingleUserIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: UserRequest? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SingleGroupIssueCustomField")
    @Serializable
    data class SingleGroupIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: UserGroupRequest? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SingleVersionIssueCustomField")
    @Serializable
    data class SingleVersionIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: VersionBundleElement? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SingleOwnedIssueCustomField")
    @Serializable
    data class SingleOwnedIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: OwnedBundleElement? = null,
    ) : IssueCustomFieldRequest

    @SerialName("SingleEnumIssueCustomField")
    @Serializable
    data class SingleEnumIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: EnumBundleElement? = null,
    ) : IssueCustomFieldRequest

    @SerialName("MultiValueIssueCustomField")
    @Serializable
    @JvmInline
    value class MultiValueIssueCustomField(override val projectCustomField: ProjectCustomFieldRequest? = null) : IssueCustomFieldRequest

    @SerialName("MultiBuildIssueCustomField")
    @Serializable
    data class MultiBuildIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: List<BuildBundleElement>? = null,
    ) : IssueCustomFieldRequest

    @SerialName("MultiGroupIssueCustomField")
    @Serializable
    data class MultiGroupIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: List<UserGroupRequest>? = null,
    ) : IssueCustomFieldRequest

    @SerialName("MultiVersionIssueCustomField")
    @Serializable
    data class MultiVersionIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: List<VersionBundleElement>? = null,
    ) : IssueCustomFieldRequest

    @SerialName("MultiOwnedIssueCustomField")
    @Serializable
    data class MultiOwnedIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: List<OwnedBundleElement>? = null,
    ) : IssueCustomFieldRequest

    @SerialName("MultiEnumIssueCustomField")
    @Serializable
    data class MultiEnumIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: List<EnumBundleElement>? = null,
    ) : IssueCustomFieldRequest

    @SerialName("MultiUserIssueCustomField")
    @Serializable
    data class MultiUserIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: List<UserRequest>? = null,
    ) : IssueCustomFieldRequest

    @SerialName("StateMachineIssueCustomField")
    @Serializable
    data class StateMachineIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val event: EventRequest? = null,
    ) : IssueCustomFieldRequest

    @SerialName("TextIssueCustomField")
    @Serializable
    data class TextIssueCustomField(
        override val projectCustomField: ProjectCustomFieldRequest? = null,
        val value: TextFieldValueRequest? = null,
    ) : IssueCustomFieldRequest
}
