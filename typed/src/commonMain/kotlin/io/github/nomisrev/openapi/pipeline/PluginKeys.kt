package io.github.nomisrev.openapi.pipeline

object PluginKeys {
    val DiscriminatedSubtype = PluginKey("discriminated-subtype")
    val DiscriminatedObject = PluginKey("discriminated-object")
    val Fallback = PluginKey("fallback")
    val Primitive = PluginKey("primitive")
    val Enum = PluginKey("enum")
    val ImplicitEnum = PluginKey("implicit-enum")
    val Collection = PluginKey("collection")
    val ImplicitCollection = PluginKey("implicit-collection")
    val Object = PluginKey("object")
    val ImplicitObject = PluginKey("implicit-object")
    val TypeArray = PluginKey("type-array")
    val AllOfNullable = PluginKey("all-of-nullable")
    val AllOf = PluginKey("all-of")
    val OneOfNullable = PluginKey("one-of-nullable")
    val OneOfSingle = PluginKey("one-of-single")
    val OneOf = PluginKey("one-of")
    val AnyOfNullable = PluginKey("any-of-nullable")
    val AnyOfSingle = PluginKey("any-of-single")
    val AnyOf = PluginKey("any-of")
}
