package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SelectedActionsRead(
  @SerialName("github_owned_allowed")
  public val githubOwnedAllowed: Boolean? = null,
  @SerialName("verified_allowed")
  public val verifiedAllowed: Boolean? = null,
  @SerialName("patterns_allowed")
  public val patternsAllowed: List<String>? = null,
)
