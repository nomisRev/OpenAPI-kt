package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SecretScanningPatternOverride(
  @SerialName("token_type")
  public val tokenType: String? = null,
  @SerialName("custom_pattern_version")
  public val customPatternVersion: String? = null,
  public val slug: String? = null,
  @SerialName("display_name")
  public val displayName: String? = null,
  @SerialName("alert_total")
  public val alertTotal: Long? = null,
  @SerialName("alert_total_percentage")
  public val alertTotalPercentage: Long? = null,
  @SerialName("false_positives")
  public val falsePositives: Long? = null,
  @SerialName("false_positive_rate")
  public val falsePositiveRate: Long? = null,
  @SerialName("bypass_rate")
  public val bypassRate: Long? = null,
  @SerialName("default_setting")
  public val defaultSetting: DefaultSetting? = null,
  @SerialName("enterprise_setting")
  public val enterpriseSetting: EnterpriseSetting? = null,
  public val setting: Setting? = null,
) {
  @Serializable
  public enum class DefaultSetting(
    public val `value`: String,
  ) {
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("enabled")
    Enabled("enabled"),
    ;
  }

  @Serializable
  public enum class EnterpriseSetting(
    public val `value`: String,
  ) {
    @SerialName("not-set")
    NotSet("not-set"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("enabled")
    Enabled("enabled"),
    ;
  }

  @Serializable
  public enum class Setting(
    public val `value`: String,
  ) {
    @SerialName("not-set")
    NotSet("not-set"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("enabled")
    Enabled("enabled"),
    ;
  }
}
