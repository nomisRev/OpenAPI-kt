package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GitTag(
    @SerialName("node_id") val nodeId: String,
    val tag: String,
    val sha: String,
    val url: String,
    val message: String,
    val tagger: Tagger,
    @SerialName("object") val `object`: Object,
    val verification: Verification? = null,
) {
    @Serializable
    data class Tagger(val date: String, val email: String, val name: String)

    @Serializable
    data class Object(val sha: String, val type: String, val url: String)
}
