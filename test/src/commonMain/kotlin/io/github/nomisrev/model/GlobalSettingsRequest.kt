package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class GlobalSettingsRequest(
    val systemSettings: SystemSettingsRequest? = null,
    val notificationSettings: NotificationSettingsRequest? = null,
    val restSettings: RestCorsSettingsRequest? = null,
    val appearanceSettings: AppearanceSettingsRequest? = null,
    val localeSettings: LocaleSettingsRequest? = null,
    val license: LicenseRequest? = null,
)
