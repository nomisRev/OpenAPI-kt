package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface SwimlaneSettingsResponse {
    val id: String?
    val enabled: Boolean?

    @SerialName("Default")
    @Serializable
    data class Default(override val id: String? = null, override val enabled: Boolean? = null) : SwimlaneSettingsResponse

    @SerialName("AttributeBasedSwimlaneSettings")
    @Serializable
    data class AttributeBasedSwimlaneSettings(
        override val id: String? = null,
        override val enabled: Boolean? = null,
        val field: FilterFieldResponse? = null,
        val values: List<SwimlaneEntityAttributeValue>? = null,
    ) : SwimlaneSettingsResponse

    @SerialName("IssueBasedSwimlaneSettings")
    @Serializable
    data class IssueBasedSwimlaneSettings(
        override val id: String? = null,
        override val enabled: Boolean? = null,
        val field: FilterFieldResponse? = null,
        val defaultCardType: SwimlaneValueResponse? = null,
        val values: List<SwimlaneValueResponse>? = null,
    ) : SwimlaneSettingsResponse
}
