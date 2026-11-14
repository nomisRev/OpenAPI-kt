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
public data class ListFineTuningCheckpointPermissionResponse(
  public val `data`: List<FineTuningCheckpointPermission>,
  public val `object`: Object,
  @SerialName("first_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val firstId: Optional<String> = Optional.Absent,
  @SerialName("last_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastId: Optional<String> = Optional.Absent,
  @SerialName("has_more")
  public val hasMore: Boolean,
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
