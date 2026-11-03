package io.github.nomisrev.render.test.client.allof.untyped.member

import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class UntypedMerge(
  public val tags: List<String>,
  public val scores: List<Int>,
  public val meta: Meta,
  public val extras: Map<String, Long>,
  public val status: Status,
  public val createdAt: Instant,
  public val either: List<String>,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val note: String? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Meta(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val a: String? = null,
  )

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("on")
    On("on"),
    @SerialName("off")
    Off("off"),
    ;
  }
}
