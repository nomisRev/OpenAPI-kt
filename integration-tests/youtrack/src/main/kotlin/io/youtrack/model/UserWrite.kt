package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = UserWrite.Serializer::class)
public sealed interface UserWrite {
  @SerialName("User")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : UserWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "User")
  }

  @SerialName("VcsUnresolvedUser")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VcsUnresolvedUser.Serializer::class)
  public class VcsUnresolvedUser() : UserWrite {
    public object Serializer : TaggedCaseSerializer<VcsUnresolvedUser>(VcsUnresolvedUser.generatedSerializer(), "${'$'}type", "VcsUnresolvedUser")
  }

  @SerialName("Me")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Me.Serializer::class)
  public class Me() : UserWrite {
    public object Serializer : TaggedCaseSerializer<Me>(Me.generatedSerializer(), "${'$'}type", "Me")
  }

  public object Serializer : JsonContentPolymorphicSerializer<UserWrite>(UserWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<UserWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "User" -> Default.serializer()
      "VcsUnresolvedUser" -> VcsUnresolvedUser.serializer()
      "Me" -> Me.serializer()
      else -> throw SerializationException("'${'$'}type' of UserWrite selects none of User, VcsUnresolvedUser, Me: " + tag)
    }
  }
}
