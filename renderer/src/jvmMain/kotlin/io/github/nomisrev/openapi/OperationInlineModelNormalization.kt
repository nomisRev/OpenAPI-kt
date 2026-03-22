package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.routes.SchemaContext
import kotlinx.serialization.json.Json

internal val InlineModelSharingJson = Json { encodeDefaults = true }
internal val SharedNamingContext = NamingContext.reference("Shared", SchemaContext.Null)

internal fun Model.normalizedForSharingKey(): Model =
    when (this) {
        is Model.Collection -> normalizedForSharingKey()
        is Model.DiscriminatedObject -> normalizedForSharingKey()
        is Model.Enum -> normalizedForSharingKey()
        is Model.Object -> normalizedForSharingKey()
        is Model.OneOf -> normalizedForSharingKey()
        is Model.AnyOf -> normalizedForSharingKey()
        is Model.ByteArray,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Reference,
        is Model.Primitive,
        is Model.Uuid -> clearedSharingMetadata()
    }

private fun Model.clearedSharingMetadata(): Model =
    if (this is Model.ByteArray) copy(description = null, title = null)
    else if (this is Model.Date) copy(description = null, title = null)
    else if (this is Model.DateTime) copy(description = null, title = null)
    else if (this is Model.FreeFormJson) copy(description = null, title = null)
    else if (this is Model.Primitive.Boolean) copy(description = null, title = null)
    else if (this is Model.Primitive.Double) copy(description = null, title = null)
    else if (this is Model.Primitive.Float) copy(description = null, title = null)
    else if (this is Model.Primitive.Int) copy(description = null, title = null)
    else if (this is Model.Primitive.Long) copy(description = null, title = null)
    else if (this is Model.Primitive.String) copy(description = null, title = null)
    else if (this is Model.Primitive.Unit) copy(description = null, title = null)
    else if (this is Model.Reference) copy(
        context = SharedNamingContext,
        description = null,
        title = null,
    )
    else if (this is Model.Uuid) copy(description = null, title = null)
    else error("Unsupported model: $this")

private fun Model.Collection.normalizedForSharingKey(): Model = copy(
    inner = inner.normalizedForSharingKey(),
    description = null,
    title = null,
)

private fun Model.DiscriminatedObject.normalizedForSharingKey(): Model = copy(
    context = SharedNamingContext,
    abstractProperties = abstractProperties.mapValues { (_, property) ->
        property.copy(model = property.model.normalizedForSharingKey())
    },
    subtypes = subtypes.map { it.normalizedForSharingKey() as Model.Object },
    description = null,
    title = null,
)

private fun Model.Enum.normalizedForSharingKey(): Model = copy(
    context = SharedNamingContext,
    inner = inner.normalizedForSharingKey(),
    description = null,
    title = null,
)

private fun Model.Object.normalizedForSharingKey(): Model = copy(
    context = SharedNamingContext,
    description = null,
    title = null,
    properties = properties.mapValues { (_, property) ->
        property.copy(model = property.model.normalizedForSharingKey())
    },
    additionalProperties = when (val additional = additionalProperties) {
        is Model.Object.AdditionalProperties.Allowed -> additional
        is Model.Object.AdditionalProperties.Schema ->
            Model.Object.AdditionalProperties.Schema(additional.value.normalizedForSharingKey())
    },
)

private fun Model.OneOf.normalizedForSharingKey(): Model = copy(
    context = SharedNamingContext,
    cases = cases.map { case ->
        case.copy(model = case.model.normalizedForSharingKey())
    },
    description = null,
    title = null,
)

private fun Model.AnyOf.normalizedForSharingKey(): Model = copy(
    context = SharedNamingContext,
    cases = cases.map { case ->
        case.copy(model = case.model.normalizedForSharingKey())
    },
    description = null,
    title = null,
)
