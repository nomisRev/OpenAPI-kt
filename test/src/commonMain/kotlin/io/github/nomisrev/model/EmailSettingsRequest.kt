package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class EmailSettingsRequest(
    val isEnabled: Boolean? = null,
    val host: String? = null,
    val port: Int? = null,
    val mailProtocol: MailProtocol? = null,
    val anonymous: Boolean? = null,
    val login: String? = null,
    val sslKey: StorageEntryRequest? = null,
    val from: String? = null,
    val replyTo: String? = null,
) {
    @Serializable
    enum class MailProtocol {
        SMTP, SMTPS, @SerialName("SMTP_TLS") SMTPTLS, @SerialName("MS_GRAPH_API") MSGRAPHAPI;
    }
}
