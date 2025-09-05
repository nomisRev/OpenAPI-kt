package io.github.nomisrev.codegen.api

/** Public API surface for M1 */
data class GeneratedFile(val path: String, val content: String)

/** Options for the emitter and utilities. */
data class CodegenOptions(val indent: String = "    ", val debug: Boolean = false)
