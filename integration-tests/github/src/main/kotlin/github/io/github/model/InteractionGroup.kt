package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class InteractionGroup(
  public val `value`: String,
) {
  @SerialName("existing_users")
  ExistingUsers("existing_users"),
  @SerialName("contributors_only")
  ContributorsOnly("contributors_only"),
  @SerialName("collaborators_only")
  CollaboratorsOnly("collaborators_only"),
  ;
}
