package io.github.nomisrev.render.test.discriminated.multiple.`abstract`

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = Asset.Serializer::class)
public sealed interface Asset {
  public val id: String

  public val createdAt: Instant

  @SerialName("image")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Image.Serializer::class)
  public data class Image(
    override val id: String,
    override val createdAt: Instant,
  ) : Asset {
    public object Serializer : TaggedCaseSerializer<Image>(Image.generatedSerializer(), "kind", "image")
  }

  @SerialName("video")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Video.Serializer::class)
  public data class Video(
    override val id: String,
    override val createdAt: Instant,
    public val durationSeconds: Int,
  ) : Asset {
    public object Serializer : TaggedCaseSerializer<Video>(Video.generatedSerializer(), "kind", "video")
  }

  public object Serializer : JsonContentPolymorphicSerializer<Asset>(Asset::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Asset> = when (val tag = element.discriminatorValue("kind")) {
      "image" -> Image.serializer()
      "video" -> Video.serializer()
      else -> throw SerializationException("'kind' of Asset selects none of image, video: " + tag)
    }
  }
}
