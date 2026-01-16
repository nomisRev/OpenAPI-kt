package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface FilterFieldRequest {


    @SerialName("Default")
    @Serializable
    data object Default : FilterFieldRequest

    @SerialName("PredefinedFilterField")
    @Serializable
    data object PredefinedFilterField : FilterFieldRequest

    @SerialName("CustomFilterField")
    @Serializable
    @JvmInline
    value class CustomFilterField(val customField: CustomFieldRequest? = null) : FilterFieldRequest
}
