package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class CodeownersErrors(val errors: List<Errors>) {
    @Serializable
    data class Errors(
        val line: Long,
        val column: Long,
        val source: String? = null,
        val kind: String,
        val suggestion: String? = null,
        val message: String,
        val path: String,
    )
}
