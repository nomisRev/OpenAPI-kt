package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GlobalSettingsResponse(
    val id: String? = null,
    val systemSettings: SystemSettingsResponse? = null,
    val notificationSettings: NotificationSettingsResponse? = null,
    val restSettings: RestCorsSettingsResponse? = null,
    val appearanceSettings: AppearanceSettingsResponse? = null,
    val localeSettings: LocaleSettingsResponse? = null,
    val license: LicenseResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
