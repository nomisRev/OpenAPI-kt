package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class GollumEvent(
  public val pages: List<Pages>,
) {
  @Serializable
  public data class Pages(
    @SerialName("page_name")
    public val pageName: String? = null,
    public val title: String? = null,
    public val summary: String? = null,
    public val action: String? = null,
    public val sha: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
  )
}
