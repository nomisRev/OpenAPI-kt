package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SearchResultTextMatches(val items: List<Item>) {
    @Serializable
    data class Item(
        @SerialName("object_url") val objectUrl: String? = null,
        @SerialName("object_type") val objectType: String? = null,
        val property: String? = null,
        val fragment: String? = null,
        val matches: List<Matches>? = null,
    ) {
        @Serializable
        data class Matches(val text: String? = null, val indices: List<Long>? = null)
    }
}
