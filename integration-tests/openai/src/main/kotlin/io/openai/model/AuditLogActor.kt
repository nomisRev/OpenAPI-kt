package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The actor who performed the audit logged action.
 */
@Serializable
public data class AuditLogActor(
  public val type: Type? = null,
  public val session: AuditLogActorSession? = null,
  @SerialName("api_key")
  public val apiKey: AuditLogActorApiKey? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("session")
    Session("session"),
    @SerialName("api_key")
    ApiKey("api_key"),
    ;
  }
}
