package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class HookResponse(val code: Long?, val status: String?, val message: String?)
