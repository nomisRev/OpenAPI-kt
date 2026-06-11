package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Pages Health Check Status
 */
@Serializable
public data class PagesHealthCheck(
  public val domain: Domain? = null,
  @SerialName("alt_domain")
  public val altDomain: AltDomain? = null,
) {
  @Serializable
  public data class AltDomain(
    public val host: String? = null,
    public val uri: String? = null,
    public val nameservers: String? = null,
    @SerialName("dns_resolves")
    public val dnsResolves: Boolean? = null,
    @SerialName("is_proxied")
    public val isProxied: Boolean? = null,
    @SerialName("is_cloudflare_ip")
    public val isCloudflareIp: Boolean? = null,
    @SerialName("is_fastly_ip")
    public val isFastlyIp: Boolean? = null,
    @SerialName("is_old_ip_address")
    public val isOldIpAddress: Boolean? = null,
    @SerialName("is_a_record")
    public val isARecord: Boolean? = null,
    @SerialName("has_cname_record")
    public val hasCnameRecord: Boolean? = null,
    @SerialName("has_mx_records_present")
    public val hasMxRecordsPresent: Boolean? = null,
    @SerialName("is_valid_domain")
    public val isValidDomain: Boolean? = null,
    @SerialName("is_apex_domain")
    public val isApexDomain: Boolean? = null,
    @SerialName("should_be_a_record")
    public val shouldBeARecord: Boolean? = null,
    @SerialName("is_cname_to_github_user_domain")
    public val isCnameToGithubUserDomain: Boolean? = null,
    @SerialName("is_cname_to_pages_dot_github_dot_com")
    public val isCnameToPagesDotGithubDotCom: Boolean? = null,
    @SerialName("is_cname_to_fastly")
    public val isCnameToFastly: Boolean? = null,
    @SerialName("is_pointed_to_github_pages_ip")
    public val isPointedToGithubPagesIp: Boolean? = null,
    @SerialName("is_non_github_pages_ip_present")
    public val isNonGithubPagesIpPresent: Boolean? = null,
    @SerialName("is_pages_domain")
    public val isPagesDomain: Boolean? = null,
    @SerialName("is_served_by_pages")
    public val isServedByPages: Boolean? = null,
    @SerialName("is_valid")
    public val isValid: Boolean? = null,
    public val reason: String? = null,
    @SerialName("responds_to_https")
    public val respondsToHttps: Boolean? = null,
    @SerialName("enforces_https")
    public val enforcesHttps: Boolean? = null,
    @SerialName("https_error")
    public val httpsError: String? = null,
    @SerialName("is_https_eligible")
    public val isHttpsEligible: Boolean? = null,
    @SerialName("caa_error")
    public val caaError: String? = null,
  )

  @Serializable
  public data class Domain(
    public val host: String? = null,
    public val uri: String? = null,
    public val nameservers: String? = null,
    @SerialName("dns_resolves")
    public val dnsResolves: Boolean? = null,
    @SerialName("is_proxied")
    public val isProxied: Boolean? = null,
    @SerialName("is_cloudflare_ip")
    public val isCloudflareIp: Boolean? = null,
    @SerialName("is_fastly_ip")
    public val isFastlyIp: Boolean? = null,
    @SerialName("is_old_ip_address")
    public val isOldIpAddress: Boolean? = null,
    @SerialName("is_a_record")
    public val isARecord: Boolean? = null,
    @SerialName("has_cname_record")
    public val hasCnameRecord: Boolean? = null,
    @SerialName("has_mx_records_present")
    public val hasMxRecordsPresent: Boolean? = null,
    @SerialName("is_valid_domain")
    public val isValidDomain: Boolean? = null,
    @SerialName("is_apex_domain")
    public val isApexDomain: Boolean? = null,
    @SerialName("should_be_a_record")
    public val shouldBeARecord: Boolean? = null,
    @SerialName("is_cname_to_github_user_domain")
    public val isCnameToGithubUserDomain: Boolean? = null,
    @SerialName("is_cname_to_pages_dot_github_dot_com")
    public val isCnameToPagesDotGithubDotCom: Boolean? = null,
    @SerialName("is_cname_to_fastly")
    public val isCnameToFastly: Boolean? = null,
    @SerialName("is_pointed_to_github_pages_ip")
    public val isPointedToGithubPagesIp: Boolean? = null,
    @SerialName("is_non_github_pages_ip_present")
    public val isNonGithubPagesIpPresent: Boolean? = null,
    @SerialName("is_pages_domain")
    public val isPagesDomain: Boolean? = null,
    @SerialName("is_served_by_pages")
    public val isServedByPages: Boolean? = null,
    @SerialName("is_valid")
    public val isValid: Boolean? = null,
    public val reason: String? = null,
    @SerialName("responds_to_https")
    public val respondsToHttps: Boolean? = null,
    @SerialName("enforces_https")
    public val enforcesHttps: Boolean? = null,
    @SerialName("https_error")
    public val httpsError: String? = null,
    @SerialName("is_https_eligible")
    public val isHttpsEligible: Boolean? = null,
    @SerialName("caa_error")
    public val caaError: String? = null,
  )
}
