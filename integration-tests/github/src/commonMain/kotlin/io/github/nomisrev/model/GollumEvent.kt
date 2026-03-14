package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class GollumEvent(val pages: List<Pages>) {
    @Serializable
    data class Pages(
        @SerialName("page_name") val pageName: String? = null,
        val title: String? = null,
        val summary: String? = null,
        val action: String? = null,
        val sha: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
    )
}
