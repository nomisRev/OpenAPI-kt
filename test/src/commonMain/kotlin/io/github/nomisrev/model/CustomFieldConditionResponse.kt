package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface CustomFieldConditionResponse {
    val id: String?
    val parent: ProjectCustomFieldResponse?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val parent: ProjectCustomFieldResponse? = null,
    ) : CustomFieldConditionResponse

    @SerialName("FieldBasedCondition")
    @Serializable
    data class FieldBasedCondition(
        override val id: String? = null,
        override val parent: ProjectCustomFieldResponse? = null,
        val field: BundleProjectCustomField? = null,
        val values: List<BundleElementResponse>? = null,
        val showForNullValue: Boolean? = null,
    ) : CustomFieldConditionResponse
}
