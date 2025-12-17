package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.reference
import kotlin.test.assertEquals
import kotlin.to

val discriminatedObjectSpec by testSuite {

    val baseName = NamingContext.reference("User", SchemaContext.Null)
    val abstractProperties =
        listOf(Model.Object.Property("id", Model.Primitive.Long(null, null, null, false, null), true))
    val base = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("AnonymousUser")),
        null,
        null,
        abstractProperties,
        emptySet(),
        false,
        false
    )
    val baseSchema = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("id" to ReferenceOr.value(Schema.integer)),
        discriminator = Schema.Discriminator(
            "type",
            mapOf(
                "AnonymousUser" to "#/components/schemas/User",
                "RegisteredUser" to "#/components/schemas/RegisteredUser",
                "ProUser" to "#/components/schemas/ProUser"
            )
        )
    )
    val registered = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("RegisteredUser")),
        null,
        null,
        listOf(
            Model.Object.Property("id", Model.Primitive.Long(null, null, null, false, null), true),
            Model.Object.Property("email", Model.Primitive.String(null, null, null, false, null), true),
        ),
        emptySet(),
        false,
        false
    )
    val registeredSchema = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf(
            "id" to ReferenceOr.value(Schema.integer),
            "email" to ReferenceOr.value(Schema.string),
        )
    )
    val pro = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("ProUser")),
        null,
        null,
        listOf(
            Model.Object.Property("id", Model.Primitive.Long(null, null, null, false, null), true),
            Model.Object.Property("email", Model.Primitive.String(null, null, null, false, null), true),
            Model.Object.Property("subscriptionId", Model.Uuid(null, false, null), true),
        ),
        emptySet(),
        false,
        false
    )
    val proSchema = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf(
            "id" to ReferenceOr.value(Schema.integer),
            "email" to ReferenceOr.value(Schema.string),
            "subscriptionId" to ReferenceOr.value(Schema.uuid)
        )
    )
    val expected = Model.DiscriminatedObject(
        baseName,
        abstractProperties,
        listOf(base, registered, pro),
        null,
        null,
        "type",
        false
    )

    test("check") {
        registry(
            api.reference("User", baseSchema)
                .reference("RegisteredUser", registeredSchema)
                .reference("ProUser", proSchema)
        ) {
            assertEquals(
                expected,
                ReferenceOr.schema("User").toModel(NamingContext.Reference("User", SchemaContext.Null), SchemaContext.Write)
            )
        }
    }
}