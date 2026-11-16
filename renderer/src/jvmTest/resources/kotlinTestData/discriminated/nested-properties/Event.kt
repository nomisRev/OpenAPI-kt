package io.github.nomisrev.render.test.discriminated.nested.properties

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = Event.Serializer::class)
public sealed interface Event {
  public val id: Long

  @SerialName("user")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = User.Serializer::class)
  public data class User(
    override val id: Long,
  ) : Event {
    public object Serializer : TaggedCaseSerializer<User>(User.generatedSerializer(), "kind", "user")
  }

  @SerialName("system")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = System.Serializer::class)
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
      @Serializable
      public data class Metadata(
        public val attempt: Int,
      )
    }

    public object Serializer : TaggedCaseSerializer<System>(System.generatedSerializer(), "kind", "system")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Event>(Event::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Event> = when (val tag = element.discriminatorValue("kind")) {
      "user" -> User.serializer()
      "system" -> System.serializer()
      else -> throw SerializationException("'kind' of Event selects none of user, system: " + tag)
    }
  }
}
