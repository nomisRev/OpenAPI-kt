package io.github.nomisrev.render.test.client.discriminated.subtype.redeclaration

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface Activity {
  public val id: String

  @SerialName("Activity")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    override val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val count: String? = null,
  ) : Activity

  @SerialName("CommentActivity")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CommentActivity(
    override val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<Comment>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val count: String? = null,
  ) : Activity

  @SerialName("CountActivity")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CountActivity(
    override val id: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val count: Int? = null,
  ) : Activity
}
