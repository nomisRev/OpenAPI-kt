package io.github.nomisrev.render.test.union.discriminated.inlined

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("event")
@Serializable
public sealed interface DiscriminatedInlinedUnion {
  @SerialName("created")
  @Serializable
  public data class Created(
    public val event: Event,
    public val id: String,
  ) : DiscriminatedInlinedUnion {
    @Serializable
    public enum class Event(
      public val `value`: String,
    ) {
      @SerialName("created")
      Created("created"),
      ;
    }
  }

  @SerialName("deleted")
  @Serializable
  public data class Deleted(
    public val event: Event,
    public val hard: Boolean,
  ) : DiscriminatedInlinedUnion {
    @Serializable
    public enum class Event(
      public val `value`: String,
    ) {
      @SerialName("deleted")
      Deleted("deleted"),
      ;
    }
  }
}
