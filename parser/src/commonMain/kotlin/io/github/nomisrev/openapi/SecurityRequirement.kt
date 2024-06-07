package io.github.nomisrev.openapi

/**
 * Lists the required security schemes to execute this operation. The object can have multiple
 * security schemes declared in it which are all required (that is, there is a logical AND between
 * the schemes).
 */
public typealias SecurityRequirement = Map<String, List<String>>
