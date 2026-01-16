package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface SwimlaneSettingsRequest {
    val enabled: Boolean?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val enabled: Boolean? = null) : SwimlaneSettingsRequest

    @SerialName("AttributeBasedSwimlaneSettings")
    @Serializable
    data class AttributeBasedSwimlaneSettings(
        override val enabled: Boolean? = null,
        val field: FilterFieldRequest? = null,
        val values: List<SwimlaneEntityAttributeValue>? = null,
    ) : SwimlaneSettingsRequest

    @SerialName("IssueBasedSwimlaneSettings")
    @Serializable
    data class IssueBasedSwimlaneSettings(
        override val enabled: Boolean? = null,
        val field: FilterFieldRequest? = null,
        val defaultCardType: SwimlaneValueRequest? = null,
        val values: List<SwimlaneValueRequest>? = null,
    ) : SwimlaneSettingsRequest
}
