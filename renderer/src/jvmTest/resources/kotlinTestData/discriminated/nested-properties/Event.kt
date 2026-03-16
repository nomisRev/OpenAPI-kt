package io.github.nomisrev.render.test.discriminated.nested.properties

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface Event {
  public val id: Long

  @JvmInline
  @SerialName("user")
  @Serializable
  public value class User(
    override val id: Long,
  ) : Event

  @SerialName("system")
  @Serializable
  public data class System(
    override val id: Long,
    public val payload: Payload,
    public val items: List<Items>,
  ) : Event {
    @Serializable
    public data class Items(
      public val name: String,
      public val enabled: Boolean,
    )

    @Serializable
    public data class Payload(
      public val source: String,
      public val metadata: Metadata,
    ) {
      @JvmInline
      @Serializable
      public value class Metadata(
        public val attempt: Int,
      )
    }
  }
}
