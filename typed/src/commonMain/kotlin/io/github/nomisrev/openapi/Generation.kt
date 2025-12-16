package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.render.import
import io.github.nomisrev.openapi.render.name
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.render.renderer
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
        KFile(
            "${result.first.first.simpleName}.kt",
            result.first.first.`package`,
            """|package ${result.first.first.`package`}
               |
               |${result.second.joinToString("\n") { "import ${it.import()}" }}
               |
               |${result.first.second}
            """.trimMargin()
        )
    }
