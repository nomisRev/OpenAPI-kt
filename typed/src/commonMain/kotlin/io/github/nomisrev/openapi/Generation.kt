package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.render.Import
import io.github.nomisrev.openapi.render.TopLevelFunction
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.render.TypeName.Class
import io.github.nomisrev.openapi.render.joinTo
import io.github.nomisrev.openapi.render.name
import io.github.nomisrev.openapi.render.newLine
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.render.renderer
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

        tailrec fun Import.import(): Class = when (this) {
            is Class -> this
            is TypeName.Collection -> type.import()
            is TopLevelFunction -> TODO()
        }

        tailrec fun TypeName.importString(): String = when (this) {
            is Class -> "${packageName}.${names.joinToString(separator = ".")}"
            is TypeName.Collection -> type.importString()
        }

        val imports = result.second
            .map { it.import() }
            .filter { clazz -> clazz.packageName != result.first.first.packageName || clazz.names.size > 1 }

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
