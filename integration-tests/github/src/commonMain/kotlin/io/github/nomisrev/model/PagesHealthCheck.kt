package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PagesHealthCheck(val domain: Domain? = null, @SerialName("alt_domain") val altDomain: AltDomain? = null) {
    @Serializable
    data class Domain(
        val host: String? = null,
        val uri: String? = null,
        val nameservers: String? = null,
        @SerialName("dns_resolves") val dnsResolves: Boolean? = null,
        @SerialName("is_proxied") val isProxied: Boolean? = null,
        @SerialName("is_cloudflare_ip") val isCloudflareIp: Boolean? = null,
        @SerialName("is_fastly_ip") val isFastlyIp: Boolean? = null,
        @SerialName("is_old_ip_address") val isOldIpAddress: Boolean? = null,
        @SerialName("is_a_record") val isARecord: Boolean? = null,
        @SerialName("has_cname_record") val hasCnameRecord: Boolean? = null,
        @SerialName("has_mx_records_present") val hasMxRecordsPresent: Boolean? = null,
        @SerialName("is_valid_domain") val isValidDomain: Boolean? = null,
        @SerialName("is_apex_domain") val isApexDomain: Boolean? = null,
        @SerialName("should_be_a_record") val shouldBeARecord: Boolean? = null,
        @SerialName("is_cname_to_github_user_domain") val isCnameToGithubUserDomain: Boolean? = null,
        @SerialName("is_cname_to_pages_dot_github_dot_com") val isCnameToPagesDotGithubDotCom: Boolean? = null,
        @SerialName("is_cname_to_fastly") val isCnameToFastly: Boolean? = null,
        @SerialName("is_pointed_to_github_pages_ip") val isPointedToGithubPagesIp: Boolean? = null,
        @SerialName("is_non_github_pages_ip_present") val isNonGithubPagesIpPresent: Boolean? = null,
        @SerialName("is_pages_domain") val isPagesDomain: Boolean? = null,
        @SerialName("is_served_by_pages") val isServedByPages: Boolean? = null,
        @SerialName("is_valid") val isValid: Boolean? = null,
        val reason: String? = null,
        @SerialName("responds_to_https") val respondsToHttps: Boolean? = null,
        @SerialName("enforces_https") val enforcesHttps: Boolean? = null,
        @SerialName("https_error") val httpsError: String? = null,
        @SerialName("is_https_eligible") val isHttpsEligible: Boolean? = null,
        @SerialName("caa_error") val caaError: String? = null,
    )

    @Serializable
    data class AltDomain(
        val host: String? = null,
        val uri: String? = null,
        val nameservers: String? = null,
        @SerialName("dns_resolves") val dnsResolves: Boolean? = null,
        @SerialName("is_proxied") val isProxied: Boolean? = null,
        @SerialName("is_cloudflare_ip") val isCloudflareIp: Boolean? = null,
        @SerialName("is_fastly_ip") val isFastlyIp: Boolean? = null,
        @SerialName("is_old_ip_address") val isOldIpAddress: Boolean? = null,
        @SerialName("is_a_record") val isARecord: Boolean? = null,
        @SerialName("has_cname_record") val hasCnameRecord: Boolean? = null,
        @SerialName("has_mx_records_present") val hasMxRecordsPresent: Boolean? = null,
        @SerialName("is_valid_domain") val isValidDomain: Boolean? = null,
        @SerialName("is_apex_domain") val isApexDomain: Boolean? = null,
        @SerialName("should_be_a_record") val shouldBeARecord: Boolean? = null,
        @SerialName("is_cname_to_github_user_domain") val isCnameToGithubUserDomain: Boolean? = null,
        @SerialName("is_cname_to_pages_dot_github_dot_com") val isCnameToPagesDotGithubDotCom: Boolean? = null,
        @SerialName("is_cname_to_fastly") val isCnameToFastly: Boolean? = null,
        @SerialName("is_pointed_to_github_pages_ip") val isPointedToGithubPagesIp: Boolean? = null,
        @SerialName("is_non_github_pages_ip_present") val isNonGithubPagesIpPresent: Boolean? = null,
        @SerialName("is_pages_domain") val isPagesDomain: Boolean? = null,
        @SerialName("is_served_by_pages") val isServedByPages: Boolean? = null,
        @SerialName("is_valid") val isValid: Boolean? = null,
        val reason: String? = null,
        @SerialName("responds_to_https") val respondsToHttps: Boolean? = null,
        @SerialName("enforces_https") val enforcesHttps: Boolean? = null,
        @SerialName("https_error") val httpsError: String? = null,
        @SerialName("is_https_eligible") val isHttpsEligible: Boolean? = null,
        @SerialName("caa_error") val caaError: String? = null,
    )
}
