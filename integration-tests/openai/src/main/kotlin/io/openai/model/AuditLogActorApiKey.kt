package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The API Key used to perform the audit logged action.
 */
@Serializable
public data class AuditLogActorApiKey(
  public val id: String? = null,
  public val type: Type? = null,
  public val user: AuditLogActorUser? = null,
  @SerialName("service_account")
  public val serviceAccount: AuditLogActorServiceAccount? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("user")
    User("user"),
    @SerialName("service_account")
    ServiceAccount("service_account"),
    ;
  }
}
