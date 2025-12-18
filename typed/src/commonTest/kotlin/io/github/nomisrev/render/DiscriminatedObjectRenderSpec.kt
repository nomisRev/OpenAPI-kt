package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext

val discriminatedObjectRenderSpec by testSuite {
    val baseName = NamingContext.reference("User", SchemaContext.Null)
    val abstractProperties = listOf(Model.Object.Property("id", Model.Primitive.Long(null, null, null, false, null), true))
    val base = Model.Object(
        baseName.nest(NamingContext.DiscriminatedObjectCase("AnonymousUser")),
        null,
        null,
        abstractProperties,
        emptySet(),
        false,
        false
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

    verify( // TODO discriminator for not properly lifted to Model
        """|@JsonClassDiscriminator("type")
           |@Serializable
           |sealed class User {
           |    abstract val id: Long
           |    
           |    @Serializable
           |    @JvmInline
           |    value class AnonymousUser(val id: Long): User
           |    
           |    @Serializable
           |    data class RegisteredUser(val id: Long, val email: String): User
           |    
           |    @OptIn(ExperimentalUuidApi::class)
           |    @Serializable
           |    data class ProUser(val id: Long, val email: String, val subscriptionId: Uuid): RegisteredUser
           |}
        """.trimMargin(),
        Model.DiscriminatedObject(
            baseName,
            abstractProperties,
            listOf(base, registered, pro),
            null,
            null,
            discriminator = "type",
            false
        ),
        setOf(TypeName.Uuid)
    )
}