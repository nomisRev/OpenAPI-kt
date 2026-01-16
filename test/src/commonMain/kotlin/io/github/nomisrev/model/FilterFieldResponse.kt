package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface FilterFieldResponse {
    val id: String?
    val presentation: String?
    val name: String?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val presentation: String? = null,
        override val name: String? = null,
    ) : FilterFieldResponse

    @SerialName("PredefinedFilterField")
    @Serializable
    data class PredefinedFilterField(
        override val id: String? = null,
        override val presentation: String? = null,
        override val name: String? = null,
    ) : FilterFieldResponse

    @SerialName("CustomFilterField")
    @Serializable
    data class CustomFilterField(
        override val id: String? = null,
        override val presentation: String? = null,
        override val name: String? = null,
        val customField: CustomFieldResponse? = null,
    ) : FilterFieldResponse
}
