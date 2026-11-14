package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class InviteListResponse(
  public val `object`: Object,
  public val `data`: List<Invite>,
  @SerialName("first_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val firstId: String? = null,
  @SerialName("last_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastId: String? = null,
  @SerialName("has_more")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasMore: Boolean? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("list")
    List("list"),
    ;
  }
}
