package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Hovercard(val contexts: List<Contexts>) {
    @Serializable
    data class Contexts(val message: String, val octicon: String)
}
