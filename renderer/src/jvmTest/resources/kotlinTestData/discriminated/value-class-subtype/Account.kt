package io.github.nomisrev.render.test.discriminated.`value`.class_.subtype

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

@Serializable(with = Account.Serializer::class)
public sealed interface Account {
  @SerialName("local")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Local.Serializer::class)
  public data object Local : Account {
    public object Serializer : TaggedCaseSerializer<Local>(Local.generatedSerializer(), "kind", "local")
  }

  @SerialName("remote")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Remote.Serializer::class)
  public data class Remote(
    public val externalId: String,
  ) : Account {
    public object Serializer : TaggedCaseSerializer<Remote>(Remote.generatedSerializer(), "kind", "remote")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Account>(Account::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Account> = when (val tag = element.discriminatorValue("kind")) {
      "local" -> Local.serializer()
      "remote" -> Remote.serializer()
      else -> throw SerializationException("'kind' of Account selects none of local, remote: " + tag)
    }
  }
}
