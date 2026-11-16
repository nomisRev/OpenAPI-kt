package io.github.nomisrev.render.test.discriminated.basic

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = User.Serializer::class)
public sealed interface User {
  public val id: Long

  @SerialName("anonymous")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Anonymous.Serializer::class)
  public data class Anonymous(
    override val id: Long,
  ) : User {
    public object Serializer : TaggedCaseSerializer<Anonymous>(Anonymous.generatedSerializer(), "type", "anonymous")
  }

  @SerialName("registered")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Registered.Serializer::class)
  public data class Registered(
    override val id: Long,
    public val email: String,
  ) : User {
    public object Serializer : TaggedCaseSerializer<Registered>(Registered.generatedSerializer(), "type", "registered")
  }

  public object Serializer : JsonContentPolymorphicSerializer<User>(User::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<User> = when (val tag = element.discriminatorValue("type")) {
      "anonymous" -> Anonymous.serializer()
      "registered" -> Registered.serializer()
      else -> throw SerializationException("'type' of User selects none of anonymous, registered: " + tag)
    }
  }
}
