package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface DatabaseAttributeValueResponse {
    val id: String?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val id: String? = null) : DatabaseAttributeValueResponse

    @SerialName("SwimlaneEntityAttributeValue")
    @Serializable
    data class SwimlaneEntityAttributeValue(
        override val id: String? = null,
        val name: String? = null,
        val isResolved: Boolean? = null,
    ) : DatabaseAttributeValueResponse

    @SerialName("AgileColumnFieldValue")
    @Serializable
    data class AgileColumnFieldValue(
        override val id: String? = null,
        val name: String? = null,
        val isResolved: Boolean? = null,
    ) : DatabaseAttributeValueResponse
}
