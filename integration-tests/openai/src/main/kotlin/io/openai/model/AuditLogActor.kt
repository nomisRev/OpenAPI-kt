package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The actor who performed the audit logged action.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuditLogActor(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: Type? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val session: AuditLogActorSession? = null,
  @SerialName("api_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
