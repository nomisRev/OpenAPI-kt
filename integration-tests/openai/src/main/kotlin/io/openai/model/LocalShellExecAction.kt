package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Execute a shell command on the server.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LocalShellExecAction(
  @Required
  public val type: Type = Type.Exec,
  public val command: List<String>,
  @SerialName("timeout_ms")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeoutMs: Optional<Long> = Optional.Absent,
  @SerialName("working_directory")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val workingDirectory: Optional<String> = Optional.Absent,
  public val env: Map<String, String>,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: Optional<String> = Optional.Absent,
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
