package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.routes.ApiModel
import io.github.nomisrev.openapi.routes.SchemaContext

private fun sortEnum(name: String, values: List<String>) = Model.Enum(
    context = NamingContext.reference(name, SchemaContext.Null),
    inner = Model.Primitive.String(null, null, null, false, null),
    values = values,
    description = null,
    title = null,
    default = null,
    isNullable = false
)

private fun apiModel(model: Model.Enum): ApiModel = ApiModel(
    routes = emptyList(),
    models = listOf(model),
    servers = emptyList(),
)

val enumRenderSpec by testSuite {
    verifyKotlinFiles(
        name = "enum renders compact entries on one line",
        resourceDirectory = "enum/basic",
    ) {
        apiModel(sortEnum(name = "Sort", values = listOf("ASC", "DESC"))).generate()
    }

    verifyKotlinFiles(
        name = "enum renders long entries with serial names",
        resourceDirectory = "enum/serial-name",
    ) {
        apiModel(
            sortEnum(
                name = "SortSerialName",
                values = (1..5).map { "very_long_enum_value_$it" }
            )
        ).generate()
    }
}
