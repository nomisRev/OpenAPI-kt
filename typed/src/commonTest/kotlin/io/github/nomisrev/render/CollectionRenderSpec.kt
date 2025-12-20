package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext

val collectionRenderSpec by testSuite {
    val item = Model.Object(
        NamingContext.reference("Foo", SchemaContext.Null)
            .nest(NamingContext.ObjectProperty("item")),
        null,
        null,
        mapOf(
            "id" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
            "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
        ),
        false,
        false
    )
    val collection = Model.Object(
        NamingContext.reference("Foo", SchemaContext.Null),
        null,
        null,
        mapOf(
            "items" to Model.Object.Property(
                Model.Collection(
                    item,
                    null,
                    null,
                    null,
                    false,
                    null
                ),
                true
            )
        ),
        false,
        false
    )
    verify(
        """
            |@Serializable
            |@JvmInline
            |value class Foo(val items: List<Item>) {
            |    @Serializable
            |    data class Item(val id: String, val name: String)
            |}
        """.trimMargin(),
        collection,
        TypeName.Serializable,
        TypeName.JvmInline
    )
}