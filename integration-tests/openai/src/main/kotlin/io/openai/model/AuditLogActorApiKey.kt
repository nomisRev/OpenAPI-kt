package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The API Key used to perform the audit logged action.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuditLogActorApiKey(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: Type? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: AuditLogActorUser? = null,
  @SerialName("service_account")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
