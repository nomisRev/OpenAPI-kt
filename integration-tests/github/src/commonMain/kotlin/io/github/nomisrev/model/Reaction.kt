package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Reaction(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val user: NullableSimpleUser?,
    val content: Content,
    @SerialName("created_at") val createdAt: LocalDateTime,
) {
    @Serializable
    enum class Content {
        @SerialName("+1")
        `+1`,
        @SerialName("-1")
        `-1`,
        @SerialName("laugh")
        Laugh,
        @SerialName("confused")
        Confused,
        @SerialName("heart")
        Heart,
        @SerialName("hooray")
        Hooray,
        @SerialName("rocket")
        Rocket,
        @SerialName("eyes")
        Eyes;
    }
}
