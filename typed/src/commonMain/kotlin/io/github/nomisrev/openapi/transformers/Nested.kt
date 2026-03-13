package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

tailrec fun Model.nestedOrNull(): Model? = when (this) {
    is Model.Collection -> inner.nestedOrNull()
    is Model.ByteArray,
    is Model.Date,
    is Model.FreeFormJson,
    is Model.DateTime,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> null

    is Model.ContextHolder if context.isTopLevel() -> null
    is Model.Object if properties.isEmpty()
            && additionalProperties is Model.Object.AdditionalProperties.Allowed
            && additionalProperties.value -> null //JsonObject

    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.Object,
    is Model.Union -> this
}

fun NamingContext.isTopLevel(): Boolean = head is NamingContext.Reference && nested.isEmpty()

internal fun Model.topLevelNames(): Set<NamingContext.Reference> =
    buildSet { addNames() }

context(builder: MutableSet<NamingContext.Reference>)
private fun NamingContext.addIfReference() {
    when {
        head is NamingContext.Reference -> builder.add(head)
        else -> {}
    }
}

context(builder: MutableSet<NamingContext.Reference>)
private fun Model.addNames() {
    when (this) {
        is Model.ContextHolder -> when (this) {
            is Model.DiscriminatedObject -> {
                context.addIfReference()
                abstractProperties.forEach { (_, prop) -> prop.model.addNames() }
                subtypes.forEach { subtype ->
                    subtype.properties.forEach { (_, prop) -> prop.model.addNames() }
                    (subtype.additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.addNames()
                }
            }

            is Model.Union -> {
                context.addIfReference()
                cases.forEach { it.model.addNames() }
            }

            is Model.Enum -> context.addIfReference()
            is Model.Object -> {
                context.addIfReference()
                properties.forEach { (_, prop) -> prop.model.addNames() }
                (additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.addNames()
            }

            is Model.Reference -> context.addIfReference()
        }

        is Model.Collection -> inner.addNames()
        is Model.ByteArray,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Primitive -> {
        }
    }
}
