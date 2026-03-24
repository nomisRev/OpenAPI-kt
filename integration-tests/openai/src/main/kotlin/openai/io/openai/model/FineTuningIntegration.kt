package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class FineTuningIntegration(
  public val type: Type,
  public val wandb: Wandb,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("wandb")
    Wandb("wandb"),
    ;
  }

  /**
   * The settings for your integration with Weights and Biases. This payload specifies the project that
   * metrics will be sent to. Optionally, you can set an explicit display name for your run, add tags
   * to your run, and set a default entity (team, username, etc) to be associated with your run.
   *
   */
  @Serializable
  public data class Wandb(
    public val project: String,
    public val name: String? = null,
    public val entity: String? = null,
    public val tags: List<String>? = null,
  )
}
