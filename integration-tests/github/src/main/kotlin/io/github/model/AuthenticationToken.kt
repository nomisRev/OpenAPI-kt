package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Authentication Token
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuthenticationToken(
  public val token: String,
  @SerialName("expires_at")
  public val expiresAt: Instant,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val permissions: JsonElement? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositories: List<Repository>? = null,
  @SerialName("single_file")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val singleFile: Optional<String> = Optional.Absent,
  @SerialName("repository_selection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositorySelection: RepositorySelection? = null,
) {
  @Serializable
  public enum class RepositorySelection(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
