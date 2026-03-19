package io.github.nomisrev.openapi

/**
 * Simple name for the child interface/class for this [PathNode].
 * Parameter nodes get a "Path" postfix; literal nodes use the raw PascalCase name.
 */
internal fun PathSegment.routeSegmentSimpleName(): String = when (this) {
    is PathSegment.Parameter -> name.toPascalCase() + "Path"
    is PathSegment.OverloadedParameter -> name.toPascalCase() + "Path"
    is PathSegment.Literal -> name.toPascalCase()
}

/**
 * Simple name for the child interface/class for this [PathNode].
 */
internal fun PathNode.childInterfaceSimpleName(): String = segment.routeSegmentSimpleName()
