package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface ColorCodingRequest {


    @SerialName("Default")
    @Serializable
    data object Default : ColorCodingRequest

    @SerialName("FieldBasedColorCoding")
    @Serializable
    @JvmInline
    value class FieldBasedColorCoding(val prototype: CustomFieldRequest? = null) : ColorCodingRequest

    @SerialName("ProjectBasedColorCoding")
    @Serializable
    @JvmInline
    value class ProjectBasedColorCoding(val projectColors: List<ProjectColorRequest>? = null) : ColorCodingRequest
}
