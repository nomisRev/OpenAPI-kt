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
  public val altDomain: AltDomain? = null,
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValidDomain: Boolean? = null,
    @SerialName("is_apex_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isPagesDomain: Boolean? = null,
    @SerialName("is_served_by_pages")
    public val isServedByPages: Boolean? = null,
    @SerialName("is_valid")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValid: Boolean? = null,
    public val reason: String? = null,
    @SerialName("responds_to_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val respondsToHttps: Boolean? = null,
    @SerialName("enforces_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enforcesHttps: Boolean? = null,
    @SerialName("https_error")
    public val httpsError: String? = null,
    @SerialName("is_https_eligible")
    public val isHttpsEligible: Boolean? = null,
    @SerialName("caa_error")
    public val caaError: String? = null,
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValidDomain: Boolean? = null,
    @SerialName("is_apex_domain")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isPagesDomain: Boolean? = null,
    @SerialName("is_served_by_pages")
    public val isServedByPages: Boolean? = null,
    @SerialName("is_valid")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isValid: Boolean? = null,
    public val reason: String? = null,
    @SerialName("responds_to_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val respondsToHttps: Boolean? = null,
    @SerialName("enforces_https")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val enforcesHttps: Boolean? = null,
    @SerialName("https_error")
    public val httpsError: String? = null,
    @SerialName("is_https_eligible")
    public val isHttpsEligible: Boolean? = null,
    @SerialName("caa_error")
    public val caaError: String? = null,
  )
}
