package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class SearchResultTextMatches(
  public val items: List<Item>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Item(
    @SerialName("object_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val objectUrl: String? = null,
    @SerialName("object_type")
    public val objectType: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `property`: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fragment: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val matches: List<Matches>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Matches(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val text: String? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val indices: List<Long>? = null,
    )
  }
}
