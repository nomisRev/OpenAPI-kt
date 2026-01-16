package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ProjectColorRequest(val project: Project? = null, val color: FieldStyleRequest? = null)
