package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GollumEvent(
  public val pages: List<Pages>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Pages(
    @SerialName("page_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val pageName: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val title: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val summary: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val action: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sha: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
  )
}
