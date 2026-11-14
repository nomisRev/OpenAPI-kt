package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SelectedActions(
  @SerialName("github_owned_allowed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val githubOwnedAllowed: Boolean? = null,
  @SerialName("verified_allowed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val verifiedAllowed: Boolean? = null,
  @SerialName("patterns_allowed")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val patternsAllowed: List<String>? = null,
)
