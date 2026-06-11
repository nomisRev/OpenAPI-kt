package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents email settings for this YouTrack installation.
 */
@Serializable
public data class EmailSettings(
  public val id: String? = null,
  public val isEnabled: Boolean? = null,
  public val host: String? = null,
  public val port: Int? = null,
  public val mailProtocol: MailProtocol? = null,
  public val anonymous: Boolean? = null,
  public val login: String? = null,
  public val sslKey: StorageEntry? = null,
  public val from: String? = null,
  public val replyTo: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
) {
  @Serializable
  public enum class MailProtocol(
    public val `value`: String,
  ) {
    SMTP("SMTP"),
    SMTPS("SMTPS"),
    @SerialName("SMTP_TLS")
    SMTPTLS("SMTP_TLS"),
    @SerialName("MS_GRAPH_API")
    MSGRAPHAPI("MS_GRAPH_API"),
    ;
  }
}
