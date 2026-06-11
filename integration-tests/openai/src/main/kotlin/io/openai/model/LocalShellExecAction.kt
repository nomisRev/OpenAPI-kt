package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Execute a shell command on the server.
 */
@Serializable
public data class LocalShellExecAction(
  @Required
  public val type: Type = Type.Exec,
  public val command: List<String>,
  @SerialName("timeout_ms")
  public val timeoutMs: Long? = null,
  @SerialName("working_directory")
  public val workingDirectory: String? = null,
  @Required
  public val env: List<String> = emptyList(),
  public val user: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("exec")
    Exec("exec"),
    ;
  }
}
