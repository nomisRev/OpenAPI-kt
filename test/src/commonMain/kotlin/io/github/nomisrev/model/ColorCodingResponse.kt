package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface ColorCodingResponse {
    val id: String?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val id: String? = null) : ColorCodingResponse

    @SerialName("FieldBasedColorCoding")
    @Serializable
    data class FieldBasedColorCoding(
        override val id: String? = null,
        val prototype: CustomFieldResponse? = null,
    ) : ColorCodingResponse

    @SerialName("ProjectBasedColorCoding")
    @Serializable
    data class ProjectBasedColorCoding(
        override val id: String? = null,
        val projectColors: List<ProjectColorResponse>? = null,
    ) : ColorCodingResponse
}
