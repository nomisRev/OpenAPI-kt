package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Metaproperties for Git author/committer information.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableGitUser(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val date: Instant? = null,
)
