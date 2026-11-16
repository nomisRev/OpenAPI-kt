package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.recursive.allof.ref.Node
import io.github.nomisrev.render.test.client.recursive.allof.ref.NodeList
import io.github.nomisrev.render.test.client.recursive.allof.ref.NodeMap
import io.github.nomisrev.render.test.client.recursive.allof.ref.Owner
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Recursive `allOf` properties decode nested values of any depth, through the schema itself, a list and a map of it.
 * Uses the goldens under `kotlinTestData/client/recursive-allof-ref` directly.
 */
class RecursiveAllOfRefRoundTripTest {
    private val json = Json

    @Test
    fun nestedValuesDecodeAndEncode() {
        val text = """
            {
              "name": "root",
              "child": { "name": "a", "child": { "name": "b" } },
              "children": [{ "name": "c", "children": [{ "name": "d" }] }],
              "byName": { "e": { "name": "e", "byName": { "f": { "name": "f" } } } },
              "owner": { "id": 1 },
              "annotated": { "note": "n", "annotated": { "note": "m" } }
            }
        """.trimIndent()
        val decoded = json.decodeFromString<Node>(text)
        assertEquals(
            Node(
                name = "root",
                child = Node(name = "a", child = Node(name = "b")),
                children = NodeList(listOf(Node(name = "c", children = NodeList(listOf(Node(name = "d")))))),
                byName = NodeMap(mapOf("e" to Node(name = "e", byName = NodeMap(mapOf("f" to Node(name = "f")))))),
                owner = Owner(id = 1),
                annotated = Node.Annotated(note = "n", annotated = Node.Annotated(note = "m")),
            ),
            decoded,
        )
        assertEquals(json.parseToJsonElement(text), json.parseToJsonElement(json.encodeToString(decoded)))
    }
}
