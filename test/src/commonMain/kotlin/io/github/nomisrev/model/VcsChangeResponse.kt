package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VcsChangeResponse(
    val id: String? = null,
    val date: Long? = null,
    val fetched: Long? = null,
    val files: Int? = null,
    val author: UserResponse? = null,
    val processors: List<ChangesProcessorResponse>? = null,
    val text: String? = null,
    val urls: List<String>? = null,
    val version: String? = null,
    val issue: IssueResponse? = null,
    val state: Int? = null,
    @SerialName($$"$type") val type: String? = null,
)
