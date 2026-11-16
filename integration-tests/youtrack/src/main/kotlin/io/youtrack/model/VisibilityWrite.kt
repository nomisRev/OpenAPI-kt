package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Represents visibility settings of an entity, for example, an issue or a comment.
 */
@Serializable(with = VisibilityWrite.Serializer::class)
public sealed interface VisibilityWrite {
  @SerialName("Visibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : VisibilityWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "Visibility")
  }

  @SerialName("UnlimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = UnlimitedVisibility.Serializer::class)
  public class UnlimitedVisibility() : VisibilityWrite {
    public object Serializer : TaggedCaseSerializer<UnlimitedVisibility>(UnlimitedVisibility.generatedSerializer(), "${'$'}type", "UnlimitedVisibility")
  }

  @SerialName("LimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = LimitedVisibility.Serializer::class)
  public data class LimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedGroups: List<UserGroupWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedUsers: List<UserWrite>? = null,
  ) : VisibilityWrite {
    public object Serializer : TaggedCaseSerializer<LimitedVisibility>(LimitedVisibility.generatedSerializer(), "${'$'}type", "LimitedVisibility")
  }

  public object Serializer : JsonContentPolymorphicSerializer<VisibilityWrite>(VisibilityWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<VisibilityWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "Visibility" -> Default.serializer()
      "UnlimitedVisibility" -> UnlimitedVisibility.serializer()
      "LimitedVisibility" -> LimitedVisibility.serializer()
      else -> throw SerializationException("'${'$'}type' of VisibilityWrite selects none of Visibility, UnlimitedVisibility, LimitedVisibility: " + tag)
    }
  }
}
