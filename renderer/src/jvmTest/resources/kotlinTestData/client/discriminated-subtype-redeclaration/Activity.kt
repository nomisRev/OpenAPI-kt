package io.github.nomisrev.render.test.client.discriminated.subtype.redeclaration

import kotlin.Int
import kotlin.OptIn
import kotlin.String
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

@Serializable(with = Activity.Serializer::class)
public sealed interface Activity {
  public val id: String

  @SerialName("Activity")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    override val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val count: String? = null,
  ) : Activity {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "kind", "Activity")
  }

  @SerialName("CommentActivity")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommentActivity.Serializer::class)
  public data class CommentActivity(
    override val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<Comment>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val count: String? = null,
  ) : Activity {
    public object Serializer : TaggedCaseSerializer<CommentActivity>(CommentActivity.generatedSerializer(), "kind", "CommentActivity")
  }

  @SerialName("CountActivity")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CountActivity.Serializer::class)
  public data class CountActivity(
    override val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val count: Int? = null,
  ) : Activity {
    public object Serializer : TaggedCaseSerializer<CountActivity>(CountActivity.generatedSerializer(), "kind", "CountActivity")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Activity>(Activity::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Activity> = when (val tag = element.discriminatorValue("kind")) {
      "Activity" -> Default.serializer()
      "CommentActivity" -> CommentActivity.serializer()
      "CountActivity" -> CountActivity.serializer()
      else -> throw SerializationException("'kind' of Activity selects none of Activity, CommentActivity, CountActivity: " + tag)
    }
  }
}
