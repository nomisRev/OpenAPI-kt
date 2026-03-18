package io.github.nomisrev.openapi

/**
 * Simple name for the child interface/class for this [PathNode].
 * Parameter nodes get a "Path" postfix; literal nodes use the raw PascalCase name.
 */
internal fun PathSegment.routeSegmentSimpleName(): String = when (this) {
    is PathSegment.Parameter -> name.toPascalCase() + "Path"
    is PathSegment.Literal -> name.toPascalCase()
}

/**
 * Simple name for the child interface/class for this [PathNode].
 */
internal fun PathNode.childInterfaceSimpleName(): String = segment.routeSegmentSimpleName()

/**
 * True when every case of this union is a simple type that can be flattened
 * into individual navigation function overloads for a path parameter.
 */
internal fun Model.Union.isFlattenablePathUnion(): Boolean =
    cases.all { case ->
        case.model is Model.Primitive ||
            case.model is Model.Enum ||
            case.model is Model.Uuid ||
            case.model is Model.Date ||
            case.model is Model.DateTime
    }

/**
 * Temporary guard while path-parameter union flattening only supports a single enum case.
 */
internal fun Model.Union.requireSupportedFlattenablePathUnion(paramName: String) {
    if (!isFlattenablePathUnion()) return
    val enumCaseCount = cases.count { it.model is Model.Enum }
    require(enumCaseCount <= 1) {
        "Path parameter '$paramName' uses a oneOf union with multiple enum cases, which is not supported yet."
    }
}
