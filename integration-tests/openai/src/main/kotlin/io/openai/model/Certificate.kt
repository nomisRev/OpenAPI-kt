package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual `certificate` uploaded to the organization.
 */
@Serializable
public data class Certificate(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("certificate_details")
  public val certificateDetails: CertificateDetails,
  public val active: Boolean? = null,
) {
  @Serializable
  public data class CertificateDetails(
    @SerialName("valid_at")
    public val validAt: Long? = null,
    @SerialName("expires_at")
    public val expiresAt: Long? = null,
    public val content: String? = null,
  )

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("certificate")
    Certificate("certificate"),
    @SerialName("organization.certificate")
    OrganizationCertificate("organization.certificate"),
    @SerialName("organization.project.certificate")
    OrganizationProjectCertificate("organization.project.certificate"),
    ;
  }
}
