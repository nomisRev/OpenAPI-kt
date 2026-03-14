package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DeleteEvent(
    val ref: String,
    @SerialName("ref_type") val refType: String,
    @SerialName("full_ref") val fullRef: String,
    @SerialName("pusher_type") val pusherType: String,
)
