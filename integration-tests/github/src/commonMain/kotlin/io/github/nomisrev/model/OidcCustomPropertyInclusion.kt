package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OidcCustomPropertyInclusion(
    @SerialName("custom_property_name") val customPropertyName: String,
    @SerialName("inclusion_source") val inclusionSource: InclusionSource,
) {
    @Serializable
    enum class InclusionSource {
        @SerialName("organization") Organization, @SerialName("enterprise") Enterprise;
    }
}
