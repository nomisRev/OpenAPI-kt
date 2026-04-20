package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An actor that can bypass rules in a ruleset
 */
@Serializable
public data class RepositoryRulesetBypassActorRead(
  @SerialName("actor_id")
  public val actorId: Long? = null,
  @SerialName("actor_type")
  public val actorType: ActorType,
  @SerialName("bypass_mode")
  public val bypassMode: BypassMode? = null,
) {
  @Serializable
  public enum class ActorType {
    Integration,
    OrganizationAdmin,
    RepositoryRole,
    Team,
    DeployKey,
  }

  @Serializable
  public enum class BypassMode(
    public val `value`: String,
  ) {
    @SerialName("always")
    Always("always"),
    @SerialName("pull_request")
    PullRequest("pull_request"),
    @SerialName("exempt")
    Exempt("exempt"),
    ;
  }
}
