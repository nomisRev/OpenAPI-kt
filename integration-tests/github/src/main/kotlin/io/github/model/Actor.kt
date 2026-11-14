package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Actor
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Actor(
  public val id: Long,
  public val login: String,
  @SerialName("display_login")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val displayLogin: String? = null,
  @SerialName("gravatar_id")
  public val gravatarId: String?,
  public val url: String,
  @SerialName("avatar_url")
  public val avatarUrl: String,
)
