package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface DatabaseAttributeValueRequest {


    @SerialName("Default")
    @Serializable
    data object Default : DatabaseAttributeValueRequest

    @SerialName("SwimlaneEntityAttributeValue")
    @Serializable
    @JvmInline
    value class SwimlaneEntityAttributeValue(val name: String? = null) : DatabaseAttributeValueRequest

    @SerialName("AgileColumnFieldValue")
    @Serializable
    @JvmInline
    value class AgileColumnFieldValue(val name: String? = null) : DatabaseAttributeValueRequest
}
