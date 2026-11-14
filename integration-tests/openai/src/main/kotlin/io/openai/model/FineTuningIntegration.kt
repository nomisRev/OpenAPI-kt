package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Wandb(
    public val project: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val entity: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tags: List<String>? = null,
  )
}
