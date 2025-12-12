package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

fun interface Imports {
    fun import(name: ClassName): String
}

context(imports: Imports)
operator fun ClassName.not() = imports.import(this)

context(imports: Imports)
fun Model.Object.render(): String = """
data class ${className()}(
${properties.joinToString(separator = ",\n", prefix = "    ") { it.render() }}    
)
""".trimIndent()

context(import: Imports)
private fun Model.Object.Property.render(): String {
    val required = if (isRequired) "@Required " else ""
    return "${required}val $baseName: ${!model.className()}"
}
