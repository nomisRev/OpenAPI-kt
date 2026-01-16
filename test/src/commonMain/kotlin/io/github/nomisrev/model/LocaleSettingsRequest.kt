package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class LocaleSettingsRequest(val locale: LocaleDescriptorRequest? = null, val isRTL: Boolean? = null)
