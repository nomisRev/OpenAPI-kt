package io.github.nomisrev.openapi

/**
 * Simple name for the child interface/class for this [PathNode].
 * Parameter nodes get a "Path" postfix; literal nodes use the raw PascalCase name.
 */
internal fun PathSegment.routeSegmentSimpleName(): String = when (this) {
    is PathSegment.Parameter -> name.toPascalCase() + "Path"
    is PathSegment.OverloadedParameter -> name.toPascalCase() + "Path"
    is PathSegment.FixedValue -> wireValue.toPascalCase()
    is PathSegment.Literal -> navigationName.toPascalCase()
}

/**
 * The name of a literal segment's navigation property, and of its class: `slash` for the empty segment of a trailing
 * slash (`/owners/`) or a double slash (`/a//b`), which is a node of its own only next to the same path without it
 * (`/owners`), as `api.owners.slash`.
 */
internal val PathSegment.Literal.navigationName: String
    get() = name.ifEmpty { "slash" }

/**
 * Simple name for the child interface/class for this [PathNode].
 */
internal fun PathNode.childInterfaceSimpleName(): String = segment.routeSegmentSimpleName()
