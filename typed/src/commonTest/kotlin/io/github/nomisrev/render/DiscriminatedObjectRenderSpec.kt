package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
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
        emptySet(),
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
        emptySet(),
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
           |    @SerialName("AnonymousUser")
           |    @Serializable
           |    @JvmInline
           |    value class AnonymousUser(val id: Long) : User
           |
           |    @SerialName("RegisteredUser")
           |    @Serializable
           |    data class RegisteredUser(val id: Long, val email: String) : User
           |
           |    @SerialName("ProUser")
           |    @Serializable
           |    data class ProUser(val id: Long, val email: String, val subscriptionId: Long) : User
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
        )
    )
}