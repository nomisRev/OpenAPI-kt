package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class DeleteBudget(val message: String, val id: String)
