package io.github.nomisrev.render.test.client.schema.names.same

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Metadata2(
  public val kind: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val b: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val level: Level? = null,
) {
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
