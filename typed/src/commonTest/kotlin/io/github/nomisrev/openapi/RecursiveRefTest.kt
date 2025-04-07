package io.github.nomisrev.openapi

class RecursiveRefTest {
  val schema =
    Schema(
      recursiveAnchor = true,
      type = Schema.Type.Basic.Object,
      additionalProperties = AdditionalProperties.Allowed(false),
      title = "Compound Filter",
      description = ReferenceOr.value("Combine multiple filters using `and` or `or`."),
      properties = TODO(),
    )
}
