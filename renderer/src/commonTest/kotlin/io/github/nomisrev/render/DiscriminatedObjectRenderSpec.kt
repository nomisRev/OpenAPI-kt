package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.ApiModel
import io.github.nomisrev.openapi.routes.SchemaContext

val discriminatedObjectRenderSpec by testSuite {
    val baseName = NamingContext.reference("User", SchemaContext.Null)
    val abstractProperties =
        mapOf("id" to Model.Object.Property(Model.Primitive.Long(null, null, null, false, null), true))
    val base = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("AnonymousUser")),
        null,
        null,
        abstractProperties,
        false,
        false
    )
    val registered = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("RegisteredUser")),
        null,
        null,
        mapOf(
            "id" to Model.Object.Property(Model.Primitive.Long(null, null, null, false, null), true),
            "email" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
        ),
        false,
        false
    )
    val pro = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("ProUser")),
        null,
        null,
        mapOf(
            "id" to Model.Object.Property(Model.Primitive.Long(null, null, null, false, null), true),
            "email" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
            "subscriptionId" to Model.Object.Property(Model.Primitive.Long(null, null, null, false, null), true),
        ),
        false,
        false
    )

    verifyKotlinFiles(
        "User.kt",
        "discriminatedunion"
    ) {
        ApiModel(
            routes = emptyList(),
            models = listOf(Model.DiscriminatedObject(
                baseName,
                abstractProperties,
                listOf(base, registered, pro),
                null,
                null,
                discriminator = "type",
                false
            )),
            servers = emptyList(),
        ).generate()
    }
}