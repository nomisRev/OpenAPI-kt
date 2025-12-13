package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.ClassName
import io.github.nomisrev.openapi.render.className
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals

val classNameSpec by testSuite {
    test("ClassName secondary constructor wraps single className") {
        assertEquals(ClassName("kotlin", listOf("Int")), ClassName("kotlin", "Int"))
    }

    test("Model.className maps primitives and builtins") {
        assertEquals(ClassName("kotlin", "ByteArray"), Model.ByteArray(description = null, isNullable = false).className())
        assertEquals(ClassName("kotlinx.datetime", "LocalDate"), Model.Date(description = null, isNullable = false).className())
        assertEquals(ClassName("kotlinx.datetime", "LocalDateTime"), Model.DateTime(description = null, isNullable = false).className())
        assertEquals(
            ClassName("kotlinx.serialization.json", "JsonElement"),
            Model.FreeFormJson(description = null, constraint = null, isNullable = false).className()
        )

        assertEquals(
            ClassName("kotlin", "Boolean"),
            Model.Primitive.Boolean(default = null, description = null, isNullable = false).className()
        )
        assertEquals(
            ClassName("kotlin", "Double"),
            Model.Primitive.Double(default = null, description = null, constraint = null, isNullable = false).className()
        )
        assertEquals(
            ClassName("kotlin", "Float"),
            Model.Primitive.Float(default = null, description = null, constraint = null, isNullable = false).className()
        )
        assertEquals(
            ClassName("kotlin", "Long"),
            Model.Primitive.Long(default = null, description = null, constraint = null, isNullable = false).className()
        )
        assertEquals(
            ClassName("kotlin", "String"),
            Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false).className()
        )
        assertEquals(
            ClassName("kotlin", "Int"),
            Model.Primitive.Int(default = null, description = null, constraint = null, isNullable = false).className()
        )
        assertEquals(ClassName("kotlin", "Unit"), Model.Primitive.Unit(description = null, isNullable = false).className())
        assertEquals(ClassName("kotlin", "Uuid"), Model.Uuid(description = null, isNullable = false).className())
    }

    test("Model.className uses NamingContext.Reference for named models") {
        val ref = NamingContext.Reference("Pet", SchemaContext.Null)

        val obj = Model.Object(
            context = ref,
            description = null,
            title = null,
            properties = emptyList(),
            inline = emptySet(),
            additionalProperties = false,
            isNullable = false
        )
        assertEquals(ClassName("io.github.nomisrev.model", "Pet"), obj.className())

        val union = Model.Union(
            context = ref,
            cases = emptyList(),
            default = null,
            description = null,
            title = null,
            inline = emptySet(),
            discriminator = null,
            isNullable = false
        )
        assertEquals(ClassName("io.github.nomisrev.model", "Pet"), union.className())

        val enm = Model.Enum(
            context = ref,
            inner = Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false),
            values = emptyList(),
            default = null,
            isOpen = false,
            description = null,
            title = null,
            isNullable = false
        )
        assertEquals(ClassName("io.github.nomisrev.model", "Pet"), enm.className())
    }

    test("NamingContext.className generates names for non-reference contexts") {
        val primitive = Model.Primitive.Int(default = null, description = null, constraint = null, isNullable = false)

        assertEquals(
            ClassName("ignored-always-nested", "AdditionalProperties"),
            NamingContext.AdditionalProperties.className(primitive)
        )
        assertEquals(
            ClassName("ignored-always-nested", "kind"),
            NamingContext.DiscriminatedObjectCase("kind").className(primitive)
        )
        assertEquals(
            ClassName("ignored-always-nested", "age"),
            NamingContext.ObjectProperty("age").className(primitive)
        )
        assertEquals(
            ClassName("ignored-always-nested", "opResponse"),
            NamingContext.Response("op").className(primitive)
        )
        assertEquals(
            ClassName("ignored-always-nested", "opBody"),
            NamingContext.RouteBody(name = "body", operationId = "op").className(primitive)
        )
        assertEquals(
            ClassName("ignored-always-nested", "id"),
            NamingContext.RouteParam(name = "id", operationId = "op").className(primitive)
        )
    }

    test("NamingContext.className UnionCase prefixes primitives") {
        fun cn(model: Model): ClassName = NamingContext.UnionCase.className(model)

        assertEquals(ClassName("ignored-always-nested", "CaseByteArray"), cn(Model.ByteArray(null, false)))
        assertEquals(ClassName("ignored-always-nested", "CaseDate"), cn(Model.Date(null, false)))
        assertEquals(ClassName("ignored-always-nested", "CaseDateTime"), cn(Model.DateTime(null, false)))
        assertEquals(
            ClassName("ignored-always-nested", "CaseFreeFormJson"),
            cn(Model.FreeFormJson(description = null, constraint = null, isNullable = false))
        )
        assertEquals(
            ClassName("ignored-always-nested", "CaseBoolean"),
            cn(Model.Primitive.Boolean(default = null, description = null, isNullable = false))
        )
        assertEquals(
            ClassName("ignored-always-nested", "CaseDouble"),
            cn(Model.Primitive.Double(default = null, description = null, constraint = null, isNullable = false))
        )
        assertEquals(
            ClassName("ignored-always-nested", "CaseFloat"),
            cn(Model.Primitive.Float(default = null, description = null, constraint = null, isNullable = false))
        )
        assertEquals(
            ClassName("ignored-always-nested", "CaseInt"),
            cn(Model.Primitive.Int(default = null, description = null, constraint = null, isNullable = false))
        )
        assertEquals(
            ClassName("ignored-always-nested", "CaseLong"),
            cn(Model.Primitive.Long(default = null, description = null, constraint = null, isNullable = false))
        )
        assertEquals(
            ClassName("ignored-always-nested", "CaseString"),
            cn(Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false))
        )
        assertEquals(ClassName("ignored-always-nested", "Empty"), cn(Model.Primitive.Unit(description = null, isNullable = false)))
        assertEquals(ClassName("ignored-always-nested", "CaseUuid"), cn(Model.Uuid(description = null, isNullable = false)))
    }

    test("NamingContext.className Path, Reference, and Nested") {
        val primitive = Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false)

        assertEquals(
            ClassName("io.github.nomisrev.api", listOf("FineTuning", "Jobs")),
            NamingContext.Path(listOf("FineTuning", "Jobs")).className(primitive)
        )

        assertEquals(
            ClassName("io.github.nomisrev.model", "Pet"),
            NamingContext.Reference(name = "Pet", context = SchemaContext.Null).className(primitive)
        )
        assertEquals(
            ClassName("io.github.nomisrev.model", "PetRequest"),
            NamingContext.Reference(name = "Pet", context = SchemaContext.Write).className(primitive)
        )
        assertEquals(
            ClassName("io.github.nomisrev.model", "PetResponse"),
            NamingContext.Reference(name = "Pet", context = SchemaContext.Read).className(primitive)
        )

        val nested = NamingContext.Path("outer").nest(NamingContext.ObjectProperty("inner"))
        assertEquals(
            ClassName("io.github.nomisrev.api", listOf("outer", "inner")),
            nested.className(primitive)
        )
    }
}

