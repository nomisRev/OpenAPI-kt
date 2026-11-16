package io.github.nomisrev.render.test.client.discriminator.several.tags

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = Invocation.Serializer::class)
public sealed interface Invocation {
  public val session: String?

  @SerialName("AddMediaInvocation")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AddMediaInvocation.Serializer::class)
  public data class AddMediaInvocation(
    public val method: Method,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val session: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val item: String? = null,
  ) : Invocation {
    @Serializable
    public enum class Method(
      public val `value`: String,
    ) {
      @SerialName("AddMedia.confirm")
      AddMediaConfirm("AddMedia.confirm"),
      @SerialName("AddMedia.handle")
      AddMediaHandle("AddMedia.handle"),
      ;
    }

    public object Serializer : TaggedCaseSerializer<AddMediaInvocation>(AddMediaInvocation.generatedSerializer(), "method", setOf("AddMedia.confirm", "AddMedia.handle"))
  }

  @SerialName("PlayMedia.handle")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = PlayMediaHandle.Serializer::class)
  public data class PlayMediaHandle(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val session: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shuffle: Boolean? = null,
  ) : Invocation {
    public object Serializer : TaggedCaseSerializer<PlayMediaHandle>(PlayMediaHandle.generatedSerializer(), "method", "PlayMedia.handle")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Invocation>(Invocation::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Invocation> = when (val tag = element.discriminatorValue("method")) {
      "AddMedia.confirm", "AddMedia.handle" -> AddMediaInvocation.serializer()
      "PlayMedia.handle" -> PlayMediaHandle.serializer()
      else -> throw SerializationException("'method' of Invocation selects none of AddMedia.confirm, AddMedia.handle, PlayMedia.handle: " + tag)
    }
  }
}
