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
public sealed interface Implicit {
  @SerialName("Other")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Other(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val o: String? = null,
  ) : Implicit

  @SerialName("metadata")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Metadata(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val b: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val level: Level? = null,
  ) : Implicit {
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
