@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.routes.Route

internal fun ApiTree.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedParameterUnion() ||
            children.any(PathNode::hasInlineNonDiscriminatedParameterUnion)

internal fun PathNode.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedParameterUnion() ||
            children.any(PathNode::hasInlineNonDiscriminatedParameterUnion)

internal fun Iterable<Route>.hasInlineNonDiscriminatedParameterUnion(): Boolean =
    flatMap(Route::inlineParameterModels)
        .any { inline -> inline.model is Model.Union && inline.model.discriminator == null }

internal fun ApiTree.hasInlineNonDiscriminatedBodyUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedBodyUnion() ||
            children.any(PathNode::hasInlineNonDiscriminatedBodyUnion)

internal fun PathNode.hasInlineNonDiscriminatedBodyUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedBodyUnion() ||
            children.any(PathNode::hasInlineNonDiscriminatedBodyUnion)

internal fun Iterable<Route>.hasInlineNonDiscriminatedBodyUnion(): Boolean =
    any { route ->
        route.body?.types.orEmpty().values.any(Route.Body::containsInlineNonDiscriminatedUnion)
    }

internal fun ApiTree.hasInlineNonDiscriminatedResponseUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedResponseUnion() ||
            children.any(PathNode::hasInlineNonDiscriminatedResponseUnion)

internal fun PathNode.hasInlineNonDiscriminatedResponseUnion(): Boolean =
    operations.values.hasInlineNonDiscriminatedResponseUnion() ||
            children.any(PathNode::hasInlineNonDiscriminatedResponseUnion)

internal fun Iterable<Route>.hasInlineNonDiscriminatedResponseUnion(): Boolean =
    any { route ->
        route.returns.responses.values.any { returnType ->
            returnType.types.values.any(Model::containsInlineNonDiscriminatedUnion)
        } || route.returns.default?.types.orEmpty().values.any(Model::containsInlineNonDiscriminatedUnion)
    }

private fun Route.Body.containsInlineNonDiscriminatedUnion(): Boolean =
    when (this) {
        is Route.Body.SetBody -> type.containsInlineNonDiscriminatedUnion()
        is Route.Body.OverloadedBody -> cases.any { it.model.containsInlineNonDiscriminatedUnion() }
        is Route.Body.FormUrlEncoded -> parameters.any { it.type.containsInlineNonDiscriminatedUnion() }
        is Route.Body.Multipart.Value -> parameters.any { it.type.containsInlineNonDiscriminatedUnion() }
        is Route.Body.Multipart.Ref -> value.containsInlineNonDiscriminatedUnion()
    }

private fun Model.containsInlineNonDiscriminatedUnion(): Boolean =
    when (this) {
        is Model.Union ->
            (context.head is NamingContext.Path && discriminator == null) ||
                    cases.any { it.model.containsInlineNonDiscriminatedUnion() }

        is Model.Object ->
            properties.values.any { it.model.containsInlineNonDiscriminatedUnion() } ||
                    ((additionalProperties as? Model.Object.AdditionalProperties.Schema)
                        ?.value
                        ?.containsInlineNonDiscriminatedUnion() == true)

        is Model.Collection -> inner.containsInlineNonDiscriminatedUnion()
        is Model.DiscriminatedObject ->
            abstractProperties.values.any { it.model.containsInlineNonDiscriminatedUnion() } ||
                    subtypes.any { subtype ->
                        subtype.properties.values.any { it.model.containsInlineNonDiscriminatedUnion() } ||
                                ((subtype.additionalProperties as? Model.Object.AdditionalProperties.Schema)
                                    ?.value
                                    ?.containsInlineNonDiscriminatedUnion() == true)
                    }

        is Model.ByteArray,
        is Model.Date,
        is Model.DateTime,
        is Model.Enum,
        is Model.FreeFormJson,
        is Model.Primitive,
        is Model.Reference,
        is Model.Uuid -> false
    }

internal fun Model.isRouteInlineModel(): Boolean =
    this is Model.ContextHolder && context.head is NamingContext.Path

internal fun Model.toInlineOperationTypeSpecOrNull(
    config: RenderConfig,
    ownerClassName: ClassName,
    nameOverride: String,
    externalTypeNames: Map<ClassName, TypeName> = emptyMap(),
): TypeSpec? =
    when (this) {
        is Model.Object -> toTypeSpec(
            config,
            classNameOverride = ownerClassName.nestedClass(nameOverride),
            externalTypeNames = externalTypeNames,
        )

        is Model.Enum -> toTypeSpec(config, nameOverride = nameOverride)
        is Model.Union -> toTypeSpec(
            config,
            classNameOverride = ownerClassName.nestedClass(nameOverride),
            externalTypeNames = externalTypeNames,
        )

        is Model.DiscriminatedObject,
        is Model.ByteArray,
        is Model.Collection,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Uuid,
        is Model.Reference,
        is Model.Primitive -> null
    }

internal fun Model.toInlineParameterTypeSpec(
    config: RenderConfig,
    ownerClassName: ClassName,
    nameOverride: String,
): TypeSpec? =
    toInlineOperationTypeSpecOrNull(
        config = config,
        ownerClassName = ownerClassName,
        nameOverride = nameOverride,
    )
