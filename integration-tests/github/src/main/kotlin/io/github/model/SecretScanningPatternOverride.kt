package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecretScanningPatternOverride(
  @SerialName("token_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tokenType: String? = null,
  @SerialName("custom_pattern_version")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val customPatternVersion: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val slug: String? = null,
  @SerialName("display_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val displayName: String? = null,
  @SerialName("alert_total")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val alertTotal: Long? = null,
  @SerialName("alert_total_percentage")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val alertTotalPercentage: Long? = null,
  @SerialName("false_positives")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val falsePositives: Long? = null,
  @SerialName("false_positive_rate")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val falsePositiveRate: Long? = null,
  @SerialName("bypass_rate")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bypassRate: Long? = null,
  @SerialName("default_setting")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val defaultSetting: DefaultSetting? = null,
  @SerialName("enterprise_setting")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enterpriseSetting: Optional<EnterpriseSetting> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
