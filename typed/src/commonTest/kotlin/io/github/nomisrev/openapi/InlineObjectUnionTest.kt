package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class InlineObjectUnionTest {
    @Test
    fun inlineObjectInUnionWithoutTypeOrEventProperty() {
        val schema = Schema(
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        type = Type.Basic.Object,
                        properties = mapOf(
                            "value" to ReferenceOr.value(Schema(type = Type.Basic.String))
                        )
                    )
                ),
                ReferenceOr.value(Schema(type = Type.Basic.Integer))
            )
        )

        // With the fix, this should no longer throw an exception
        val model = schema.toModel("TypeOrEvent")

        // Verify that the model is a Union
        assertIs<Model.Union>(model)

        // The union should have 2 cases
        assertEquals(2, model.cases.size, "Expected 2 cases in the union")

        // Find the object case
        val objectCase = model.cases.find { it.model is Model.Object }
        assertTrue(objectCase != null, "Expected to find an Object case")

        // The name should be derived from the "value" property
        assertTrue(objectCase!!.context.toString().contains("Value"), 
            "Expected the object case name to contain 'Value', but was: ${objectCase.context}")
    }

    @Test
    fun realWorldInlineObjectInUnionWithoutTypeOrEventProperty() {
        // This test simulates a real-world API schema with a union that includes an inline object
        // without "type" or "event" properties, which is a common pattern in many APIs
        val schema = Schema(
            description = ReferenceOr.value("A response that can be either a user profile object or an error code"),
            oneOf = listOf(
                // First case: an inline object representing a user profile
                ReferenceOr.value(
                    Schema(
                        type = Type.Basic.Object,
                        properties = mapOf(
                            "id" to ReferenceOr.value(Schema(type = Type.Basic.String)),
                            "name" to ReferenceOr.value(Schema(type = Type.Basic.String)),
                            "email" to ReferenceOr.value(Schema(type = Type.Basic.String)),
                            "created_at" to ReferenceOr.value(Schema(type = Type.Basic.String))
                        ),
                        required = listOf("id", "name")
                    )
                ),
                // Second case: a string representing an error code
                ReferenceOr.value(Schema(type = Type.Basic.String))
            )
        )

        // With the fix, this should no longer throw an exception
        val model = schema.toModel("UserProfileOrError")

        // Verify that the model is a Union
        assertIs<Model.Union>(model)

        // The union should have 2 cases
        assertEquals(2, model.cases.size, "Expected 2 cases in the union")

        // Find the object case
        val objectCase = model.cases.find { it.model is Model.Object }
        assertTrue(objectCase != null, "Expected to find an Object case")

        // The name should be derived from the properties
        val expectedNameParts = listOf("Id", "Name", "Email", "Created_at")
        val contextString = objectCase!!.context.toString()

        // Check that the context name contains the property names
        for (part in expectedNameParts) {
            assertTrue(contextString.contains(part), 
                "Expected the object case name to contain '$part', but was: $contextString")
        }
    }
}
