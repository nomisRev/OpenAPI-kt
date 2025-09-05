package io.github.nomisrev.codegen.transform

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelRegistryNameOfTest {

  private fun registry(vararg models: Model): ModelRegistry = ModelRegistry.from(models.toList())

  @Test
  fun object_and_enum_and_reference_use_context_flat_name() {
    val obj =
      Model.Object(NamingContext.Named("pet"), null, properties = emptyList(), inline = emptyList())
    val enumClosed =
      Model.Enum.Closed(
        NamingContext.Named("status"),
        Model.Primitive.String(null, null, null),
        values = listOf("alive", "dead"),
        default = null,
        description = null,
      )
    val enumOpen =
      Model.Enum.Open(
        NamingContext.Named("color"),
        values = listOf("red", "blue"),
        default = null,
        description = null,
      )
    val ref = Model.Reference(NamingContext.Named("petRef"), null)

    val reg = registry(obj, enumClosed, enumOpen, ref)

    assertEquals("Pet", reg.nameOf(obj))
    assertEquals("Status", reg.nameOf(enumClosed))
    assertEquals("Color", reg.nameOf(enumOpen))
    assertEquals("PetRef", reg.nameOf(ref))
  }

  @Test
  fun nested_contexts_are_flattened() {
    val ctx = NamingContext.Nested(NamingContext.Named("files"), NamingContext.Named("assistants"))
    val obj = Model.Object(ctx, null, emptyList(), emptyList())
    val reg = registry(obj)
    assertEquals("AssistantsFiles", reg.nameOf(obj))
  }

  @Test
  fun route_param_and_body_contexts() {
    val paramCtx =
      NamingContext.RouteParam(
        name = "order",
        operationId = "listAssistantFiles",
        postfix = "Request",
      )
    val bodyCtx = NamingContext.RouteBody(name = "chat", postfix = "Response")
    val objParam = Model.Object(paramCtx, null, emptyList(), emptyList())
    val objBody = Model.Object(bodyCtx, null, emptyList(), emptyList())
    val reg = registry(objParam, objBody)
    // RouteParam in codegen flattens to operationId + postfix, ignoring the param name
    assertEquals("ListAssistantFilesRequest", reg.nameOf(objParam))
    assertEquals("ChatResponse", reg.nameOf(objBody))
  }

  @Test
  fun primitives_and_specials_have_stable_names() {
    val reg = registry()
    assertEquals(
      "Int",
      reg.nameOf(Model.Primitive.Int(default = null, description = null, constraint = null)),
    )
    assertEquals(
      "Double",
      reg.nameOf(Model.Primitive.Double(default = null, description = null, constraint = null)),
    )
    assertEquals("Boolean", reg.nameOf(Model.Primitive.Boolean(default = null, description = null)))
    assertEquals(
      "String",
      reg.nameOf(Model.Primitive.String(default = null, description = null, constraint = null)),
    )
    assertEquals("Unit", reg.nameOf(Model.Primitive.Unit(description = null)))
    assertEquals(
      "JsonElement",
      reg.nameOf(Model.FreeFormJson(description = null, constraint = null)),
    )
    assertEquals("Binary", reg.nameOf(Model.OctetStream(description = null)))
  }

  @Test
  fun collections_derive_from_inner_names() {
    val refPet = Model.Reference(NamingContext.Named("Pet"), null)
    val reg = registry(refPet)
    assertEquals(
      "PetList",
      reg.nameOf(
        Model.Collection.List(inner = refPet, default = null, description = null, constraint = null)
      ),
    )
    assertEquals(
      "PetList",
      reg.nameOf(
        Model.Collection.List(inner = refPet, default = null, description = null, constraint = null)
      ),
    )
    assertEquals(
      "PetMap",
      reg.nameOf(Model.Collection.Map(inner = refPet, description = null, constraint = null)),
    )

    val nested =
      Model.Collection.List(
        Model.Collection.Map(inner = refPet, description = null, constraint = null),
        default = null,
        description = null,
        constraint = null,
      )
    assertEquals("PetMapList", reg.nameOf(nested))
  }

  @Test
  fun union_returns_context_flat_name() {
    val u =
      Model.Union(
        context = NamingContext.Named("U"),
        cases = emptyList(),
        default = null,
        description = null,
        inline = emptyList(),
        discriminator = null,
      )
    val reg = registry()
    assertEquals("U", reg.nameOf(u))
  }

  @Test
  fun union_nested_context_name() {
    val ctx = NamingContext.Nested(NamingContext.Named("Inner"), NamingContext.Named("Outer"))
    val u =
      Model.Union(
        context = ctx,
        cases = emptyList(),
        default = null,
        description = null,
        inline = emptyList(),
        discriminator = null,
      )
    val reg = registry()
    assertEquals("OuterInner", reg.nameOf(u))
  }
}
