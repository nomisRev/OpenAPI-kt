package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An actor that can bypass rules in a ruleset
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryRulesetBypassActor(
  @SerialName("actor_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val actorId: Optional<Long> = Optional.Absent,
  @SerialName("actor_type")
  public val actorType: ActorType,
  @SerialName("bypass_mode")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
