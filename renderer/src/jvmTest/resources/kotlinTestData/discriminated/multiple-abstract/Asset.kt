package io.github.nomisrev.render.test.discriminated.multiple.`abstract`

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface Asset {
  public val id: String

  public val createdAt: Instant

  @SerialName("image")
  @Serializable
  public data class Image(
    override val id: String,
    override val createdAt: Instant,
  ) : Asset

  @SerialName("video")
  @Serializable
  public data class Video(
    override val id: String,
    override val createdAt: Instant,
    public val durationSeconds: Int,
  ) : Asset
}
