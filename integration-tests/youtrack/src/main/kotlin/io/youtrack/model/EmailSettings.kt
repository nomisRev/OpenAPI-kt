package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents email settings for this YouTrack installation.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class EmailSettings(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isEnabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val host: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val port: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mailProtocol: MailProtocol? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val anonymous: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val login: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sslKey: StorageEntry? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val from: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val replyTo: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
