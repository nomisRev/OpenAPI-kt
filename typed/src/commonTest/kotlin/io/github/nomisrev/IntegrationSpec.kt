package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

val integrationSpec by testSuite {
//    yamlSpec("openai.yaml")
//    jsonSpec("github.json")
//    jsonSpec("youtrack.json")

    test("SavedQuery") {
        val schemas = Json.decodeFromString(
            MapSerializer(String.serializer(), ReferenceOr.serializer(Schema.serializer())),
            readText("Test.json")
        )
        val api = api.copy(
            components = api.components.copy(schemas = api.components.schemas + schemas)
        )

        registry(api) {
            val model = (api.components.schemas["SavedQuery"] as ReferenceOr.Value<Schema>)
                .toModel(NamingContext.Reference("SavedQuery", SchemaContext.Read), SchemaContext.Read)

            val x = renderer {
                model.render()
            }
        }

//        val y = registry(api) {
//            val x = ReferenceOr.schema("SavedQuery").toModel(NamingContext.ObjectProperty("test"), SchemaContext.Write)
//            println(x.description)
//            val y = ReferenceOr.schema("SavedQuery").toModel(NamingContext.ObjectProperty("test"), SchemaContext.Read)
//            println(y.description)
//            y
//        }
//        println(y.isNullable.not())
    }
}

@TestRegistering
fun TestSuite.spec(api: OpenAPI) = test("${api.info.title} ${api.info.version}") {
    api.debug("SavedQuery")
    val result = api.toApiModel()
    println(result)
}

@TestRegistering
fun TestSuite.yamlSpec(name: String) =
    spec(OpenAPI.fromYaml(readText(name)))

@TestRegistering
fun TestSuite.jsonSpec(name: String) =
    spec(OpenAPI.fromJson(readText(name)))

private fun readText(path: String) = with(SystemFileSystem) {
    val candidates = listOf(
        Path("test-specs/$path"), // when working directory is the repository root
        Path("../test-specs/$path"), // when working directory is the module directory
    )
    val path = requireNotNull(candidates.firstOrNull { exists(it) }) {
        "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
    }
    source(path).buffered().readString()
}