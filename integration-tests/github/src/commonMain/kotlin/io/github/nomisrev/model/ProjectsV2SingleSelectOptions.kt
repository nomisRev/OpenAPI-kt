package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectsV2SingleSelectOptions(
    val id: String,
    val name: Name,
    val description: Description,
    val color: String,
) {
    @Serializable
    data class Name(val raw: String, val html: String)

    @Serializable
    data class Description(val raw: String, val html: String)
}
