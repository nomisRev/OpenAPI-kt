package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.render.Import
import io.github.nomisrev.openapi.render.TopLevelFunction
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.render.TypeName.Class
import io.github.nomisrev.openapi.render.joinTo
import io.github.nomisrev.openapi.render.name
import io.github.nomisrev.openapi.render.newLine
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.render.renderRootFile
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.render.toPascalCase
import io.github.nomisrev.openapi.render.unaryPlus
import io.github.nomisrev.openapi.routes.ApiModel

data class KFile(val name: String, val packageName: String, val content: String)

tailrec fun Model.contextOrNull(): NamingContext? = when (this) {
    is Model.Collection -> inner.contextOrNull()
    is Model.DiscriminatedObject -> context
    is Model.Enum -> context
    is Model.Object -> context
    is Model.Reference -> context
    is Model.Union -> context
    is Model.Date,
    is Model.DateTime,
    is Model.Uuid,
    is Model.FreeFormJson,
    is Model.ByteArray,
    is Model.Primitive -> null
}

fun ApiModel.generate(): List<KFile> =
    models.map { model ->
        val context = model.contextOrNull()
        require(context != null && context.head is NamingContext.Reference) {
            "$context is not a top-level reference. $model"
        }

        val result = renderer {
            Pair(context.name(), model.render())
        }

        tailrec fun Import.import(): Import = when (this) {
            is Class -> this
            is TypeName.Collection -> type.import()
            is TopLevelFunction -> this
        }

        tailrec fun Import.importString(): String = when (this) {
            is Class -> "${packageName}.${names.joinToString(separator = ".")}"
            is TypeName.Collection -> type.importString()
            is TopLevelFunction -> "${packageName}.${functionName}"
        }

        val imports = result.second
            .map { it.import() }
            .filter { clazz -> clazz.packageName != result.first.first.packageName }

        KFile(
            "${result.first.first.simpleName}.kt",
            result.first.first.packageName,
            buildString {
                +"package ${result.first.first.packageName}"
                newLine()
                imports.joinTo(separator = "\n", postfix = "\n\n") {
                    "import ${it.importString()}"
                }
                +result.first.second
            }
        )
    }

fun Root.generateClient(packageName: String = "io.github.nomisrev"): List<KFile> {
    val apiPackage = "$packageName.api"
    val rootFileName = name.toPascalCase()

    val (content, rawImports) = renderer { renderRootFile() }

    val imports = rawImports
        .map { it.resolveImport() }
        .filter { it.packageName != apiPackage }

    return listOf(
        KFile(
            "$rootFileName.kt",
            apiPackage,
            buildString {
                +"package $apiPackage"
                newLine()
                imports.joinTo(separator = "\n", postfix = "\n\n") {
                    "import ${it.importString()}"
                }
                +content
            }
        )
    )
}

private tailrec fun Import.resolveImport(): Import = when (this) {
    is Class -> this
    is TypeName.Collection -> type.resolveImport()
    is TopLevelFunction -> this
}

private fun Import.importString(): String = when (this) {
    is Class -> "${packageName}.${names.joinToString(separator = ".")}"
    is TypeName.Collection -> type.importString()
    is TopLevelFunction -> "${packageName}.${functionName}"
}
