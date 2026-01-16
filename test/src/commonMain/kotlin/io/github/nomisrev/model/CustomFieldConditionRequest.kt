package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface CustomFieldConditionRequest {
    val parent: ProjectCustomFieldRequest?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val parent: ProjectCustomFieldRequest? = null) : CustomFieldConditionRequest

    @SerialName("FieldBasedCondition")
    @Serializable
    data class FieldBasedCondition(
        override val parent: ProjectCustomFieldRequest? = null,
        val field: BundleProjectCustomField? = null,
        val values: List<BundleElementRequest>? = null,
        val showForNullValue: Boolean? = null,
    ) : CustomFieldConditionRequest
}
