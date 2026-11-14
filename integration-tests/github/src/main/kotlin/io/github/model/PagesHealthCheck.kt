package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Pages Health Check Status
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PagesHealthCheck(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val domain: Domain? = null,
  @SerialName("alt_domain")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val altDomain: Optional<AltDomain> = Optional.Absent,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AltDomain(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val host: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val uri: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nameservers: String? = null,
    @SerialName("dns_resolves")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val dnsResolves: Boolean? = null,
    @SerialName("is_proxied")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isProxied: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cloudflare_ip")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCloudflareIp: Optional<Boolean> = Optional.Absent,
    @SerialName("is_fastly_ip")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isFastlyIp: Optional<Boolean> = Optional.Absent,
    @SerialName("is_old_ip_address")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isOldIpAddress: Optional<Boolean> = Optional.Absent,
    @SerialName("is_a_record")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isARecord: Optional<Boolean> = Optional.Absent,
    @SerialName("has_cname_record")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasCnameRecord: Optional<Boolean> = Optional.Absent,
    @SerialName("has_mx_records_present")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasMxRecordsPresent: Optional<Boolean> = Optional.Absent,
    @SerialName("is_valid_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValidDomain: Boolean? = null,
    @SerialName("is_apex_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isApexDomain: Boolean? = null,
    @SerialName("should_be_a_record")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shouldBeARecord: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cname_to_github_user_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCnameToGithubUserDomain: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cname_to_pages_dot_github_dot_com")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCnameToPagesDotGithubDotCom: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cname_to_fastly")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCnameToFastly: Optional<Boolean> = Optional.Absent,
    @SerialName("is_pointed_to_github_pages_ip")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isPointedToGithubPagesIp: Optional<Boolean> = Optional.Absent,
    @SerialName("is_non_github_pages_ip_present")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isNonGithubPagesIpPresent: Optional<Boolean> = Optional.Absent,
    @SerialName("is_pages_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isPagesDomain: Boolean? = null,
    @SerialName("is_served_by_pages")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isServedByPages: Optional<Boolean> = Optional.Absent,
    @SerialName("is_valid")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValid: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reason: Optional<String> = Optional.Absent,
    @SerialName("responds_to_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val respondsToHttps: Boolean? = null,
    @SerialName("enforces_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enforcesHttps: Boolean? = null,
    @SerialName("https_error")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val httpsError: Optional<String> = Optional.Absent,
    @SerialName("is_https_eligible")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isHttpsEligible: Optional<Boolean> = Optional.Absent,
    @SerialName("caa_error")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val caaError: Optional<String> = Optional.Absent,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Domain(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val host: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val uri: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nameservers: String? = null,
    @SerialName("dns_resolves")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val dnsResolves: Boolean? = null,
    @SerialName("is_proxied")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isProxied: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cloudflare_ip")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCloudflareIp: Optional<Boolean> = Optional.Absent,
    @SerialName("is_fastly_ip")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isFastlyIp: Optional<Boolean> = Optional.Absent,
    @SerialName("is_old_ip_address")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isOldIpAddress: Optional<Boolean> = Optional.Absent,
    @SerialName("is_a_record")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isARecord: Optional<Boolean> = Optional.Absent,
    @SerialName("has_cname_record")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasCnameRecord: Optional<Boolean> = Optional.Absent,
    @SerialName("has_mx_records_present")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val hasMxRecordsPresent: Optional<Boolean> = Optional.Absent,
    @SerialName("is_valid_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValidDomain: Boolean? = null,
    @SerialName("is_apex_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isApexDomain: Boolean? = null,
    @SerialName("should_be_a_record")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shouldBeARecord: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cname_to_github_user_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCnameToGithubUserDomain: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cname_to_pages_dot_github_dot_com")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCnameToPagesDotGithubDotCom: Optional<Boolean> = Optional.Absent,
    @SerialName("is_cname_to_fastly")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isCnameToFastly: Optional<Boolean> = Optional.Absent,
    @SerialName("is_pointed_to_github_pages_ip")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isPointedToGithubPagesIp: Optional<Boolean> = Optional.Absent,
    @SerialName("is_non_github_pages_ip_present")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isNonGithubPagesIpPresent: Optional<Boolean> = Optional.Absent,
    @SerialName("is_pages_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isPagesDomain: Boolean? = null,
    @SerialName("is_served_by_pages")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isServedByPages: Optional<Boolean> = Optional.Absent,
    @SerialName("is_valid")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValid: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reason: Optional<String> = Optional.Absent,
    @SerialName("responds_to_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val respondsToHttps: Boolean? = null,
    @SerialName("enforces_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enforcesHttps: Boolean? = null,
    @SerialName("https_error")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val httpsError: Optional<String> = Optional.Absent,
    @SerialName("is_https_eligible")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isHttpsEligible: Optional<Boolean> = Optional.Absent,
    @SerialName("caa_error")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val caaError: Optional<String> = Optional.Absent,
  )
}
