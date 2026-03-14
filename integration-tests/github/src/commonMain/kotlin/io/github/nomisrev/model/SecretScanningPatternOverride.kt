package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SecretScanningPatternOverride(
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("custom_pattern_version") val customPatternVersion: String? = null,
    val slug: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("alert_total") val alertTotal: Long? = null,
    @SerialName("alert_total_percentage") val alertTotalPercentage: Long? = null,
    @SerialName("false_positives") val falsePositives: Long? = null,
    @SerialName("false_positive_rate") val falsePositiveRate: Long? = null,
    @SerialName("bypass_rate") val bypassRate: Long? = null,
    @SerialName("default_setting") val defaultSetting: DefaultSetting? = null,
    @SerialName("enterprise_setting") val enterpriseSetting: EnterpriseSetting? = null,
    val setting: Setting? = null,
) {
    @Serializable
    enum class DefaultSetting {
        @SerialName("disabled") Disabled, @SerialName("enabled") Enabled;
    }

    @Serializable
    enum class EnterpriseSetting {
        @SerialName("not-set") NotSet, @SerialName("disabled") Disabled, @SerialName("enabled") Enabled;
    }

    @Serializable
    enum class Setting {
        @SerialName("not-set") NotSet, @SerialName("disabled") Disabled, @SerialName("enabled") Enabled;
    }
}
