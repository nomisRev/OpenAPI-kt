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
public data class ListCertificatesResponse(
  public val `data`: List<Certificate>,
  @SerialName("first_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val firstId: String? = null,
  @SerialName("last_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastId: String? = null,
  @SerialName("has_more")
  public val hasMore: Boolean,
  public val `object`: Object,
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
