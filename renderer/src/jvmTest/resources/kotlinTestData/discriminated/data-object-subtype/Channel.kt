package io.github.nomisrev.render.test.discriminated.`data`.object_.subtype

import kotlin.OptIn
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = Channel.Serializer::class)
public sealed interface Channel {
  @SerialName("sms")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Sms.Serializer::class)
  public data object Sms : Channel {
    public object Serializer : TaggedCaseSerializer<Sms>(Sms.generatedSerializer(), "kind", "sms")
  }

  @SerialName("email")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Email.Serializer::class)
  public data object Email : Channel {
    public object Serializer : TaggedCaseSerializer<Email>(Email.generatedSerializer(), "kind", "email")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Channel>(Channel::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Channel> = when (val tag = element.discriminatorValue("kind")) {
      "sms" -> Sms.serializer()
      "email" -> Email.serializer()
      else -> throw SerializationException("'kind' of Channel selects none of sms, email: " + tag)
    }
  }
}
