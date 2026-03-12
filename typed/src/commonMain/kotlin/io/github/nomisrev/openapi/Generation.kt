package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.render.Config
import io.github.nomisrev.openapi.render.Import
import io.github.nomisrev.openapi.render.TopLevelFunction
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.render.TypeName.Class
import io.github.nomisrev.openapi.render.joinTo
import io.github.nomisrev.openapi.render.name
import io.github.nomisrev.openapi.render.newLine
import io.github.nomisrev.openapi.render.renderApiFile
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

fun ApiModel.generate(): List<KFile> = models.generate()

fun List<Model>.generate(`package`: String = "io.github.nomisrev"): List<KFile> = map { model ->
    val context = model.contextOrNull()
    require(context != null && context.head is NamingContext.Reference) {
        "$context is not a top-level reference. $model"
    }

    val result =
        renderer(Config(120, 4, jvm = true, js = true, `package`)) {
            Pair(context.name(), model.render())
        }

    tailrec fun Import.import(): Import = when (this) {
        is Class -> this
        is TypeName.Collection -> type.import()
        is TopLevelFunction -> this
    }

    tailrec fun Import.importString(): String = when (this) {
        is Class -> "${this.packageName}.${names.joinToString(separator = ".")}"
        is TypeName.Collection -> type.importString()
        is TopLevelFunction -> "${this.packageName}.${functionName}"
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

    fun renderClientFile(fileName: String, content: String, rawImports: Set<Import>): KFile {
        val resolved = rawImports
            .map { it.resolveImport() }
            .filter { it.packageName != apiPackage }
        val imports = resolved.reorderClientImports()

        return KFile(
            fileName,
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
    }

    val (rootContent, rootImports) = renderer { renderRootFile() }
    val rootFile = renderClientFile("$rootFileName.kt", rootContent, rootImports)

    val endpointFiles = endpoints.map { api ->
        val fileName = "${api.name.toPascalCase()}.kt"
        val (content, rawImports) = renderer { api.renderApiFile() }
        renderClientFile(fileName, content, rawImports)
    }

    return listOf(rootFile) + endpointFiles
}

private tailrec fun Import.resolveImport(): Import = when (this) {
    is Class -> this
    is TypeName.Collection -> type.resolveImport()
    is TopLevelFunction -> this
}

private fun List<Import>.reorderClientImports(): List<Import> {
    val lateRequestHelpers = mutableListOf<TopLevelFunction>()
    val trailingHttpContentType = mutableListOf<TopLevelFunction>()
    val regular = mutableListOf<Import>()

    for (import in this) {
        when (import) {
            is TopLevelFunction if import.packageName == "io.ktor.client.request" &&
                    import.functionName in setOf(
                "get",
                "post",
                "put",
                "patch",
                "delete",
                "head",
                "options",
                "parameter"
            ) -> {
                lateRequestHelpers += import
            }

            is TopLevelFunction if import.packageName == "io.ktor.http" && import.functionName == "contentType" -> {
                trailingHttpContentType += import
            }

            else -> regular += import
        }
    }

    return buildList {
        addAll(regular)
        addAll(lateRequestHelpers.sortedBy { it.functionName })
        addAll(trailingHttpContentType)
    }
}

private fun Import.importString(): String = when (this) {
    is Class -> "${packageName}.${names.joinToString(separator = ".")}"
    is TypeName.Collection -> type.importString()
    is TopLevelFunction -> "${packageName}.${functionName}"
}
