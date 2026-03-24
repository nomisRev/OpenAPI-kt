package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteCertificateResponse(
  public val `object`: Object,
  public val id: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("certificate.deleted")
    CertificateDeleted("certificate.deleted"),
    ;
  }
}
