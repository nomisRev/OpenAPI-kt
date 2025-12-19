package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.contextOrNull
import kotlinx.serialization.EncodeDefault
import kotlin.collections.plus

internal tailrec fun Model.nestedOrNull(): Model? = when (this) {
    is Model.Collection -> inner.nestedOrNull()
    is Model.ByteArray,
    is Model.Date,
    is Model.FreeFormJson,
    is Model.DateTime,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> null

    is Model.DiscriminatedObject if context.isTopLevel() -> null
    is Model.Enum if context.isTopLevel() -> null
    is Model.Object if context.isTopLevel() -> null
    is Model.Union if context.isTopLevel() -> null
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.Object,
    is Model.Union -> this
}

fun Model.topLevelNames(): Set<NamingContext.Reference> = buildSet {
    // Do not add NamingContext.Reference when nested is DiscriminatedObjectCase
    fun NamingContext.addIfReference() {
        when {
            head is NamingContext.Reference -> add(head)
            else -> {}
        }
    }

    fun Model.topLevelNames() {
        when (this) {
            is Model.ContextHolder -> when (this) {
                is Model.DiscriminatedObject -> {
                    context.addIfReference()
                    abstractProperties.forEach { (_, prop) -> prop.model.topLevelNames() }
                    subtypes.forEach {
                        it.properties.forEach { (_, prop) -> prop.model.topLevelNames() }
                        (it.additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.topLevelNames()
                    }
                }

                is Model.Union -> {
                    context.addIfReference()
                    cases.forEach { it.model.topLevelNames() }
                }

                is Model.Enum -> context.addIfReference()
                is Model.Object -> {
                    context.addIfReference()
                    properties.forEach { (_, prop) -> prop.model.topLevelNames() }
                    (additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.topLevelNames()
                }

                is Model.Reference -> context.addIfReference()
            }

            is Model.Collection -> inner.topLevelNames()
            is Model.ByteArray,
            is Model.Date,
            is Model.DateTime,
            is Model.FreeFormJson,
            is Model.Uuid,
            is Model.Primitive -> {
            }
        }
    }

    topLevelNames()
}

fun NamingContext.isTopLevel(): Boolean = head is NamingContext.Reference && nested.isEmpty()