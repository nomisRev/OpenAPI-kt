package io.github.nomisrev.render.test.client.schema.names.same

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface Thing {
  @SerialName("plain")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Plain(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val a: String? = null,
  ) : Thing

  @SerialName("meta")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Meta(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val b: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val level: Level? = null,
  ) : Thing {
    @Serializable
    public enum class Level(
      public val `value`: String,
    ) {
      @SerialName("low")
      Low("low"),
      @SerialName("high")
      High("high"),
      ;
    }
  }
}
