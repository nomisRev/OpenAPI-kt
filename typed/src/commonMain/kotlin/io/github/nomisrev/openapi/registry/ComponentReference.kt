package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.parser.ReferenceOr

/**
 * Resolves a `ReferenceOr` of a [section] of `components` (`requestBodies`, `responses`, `parameters`, ...).
 * A local reference `#/components/<section>/<name>` resolves to that component, following components that are
 * themselves a `$ref` to another component of the same section (`Alias: {$ref: '#/components/<section>/X'}`).
 * Cyclic aliases fail with a diagnostic naming the cycle, and references into other documents are not supported yet.
 */
internal fun <A> ReferenceOr<A>.resolveComponent(section: String, components: Map<String, ReferenceOr<A>>): A =
    when (this) {
        is ReferenceOr.Value -> value
        is ReferenceOr.Reference -> resolveComponent(ref, section, components, emptyList())
    }

private tailrec fun <A> resolveComponent(
    ref: String,
    section: String,
    components: Map<String, ReferenceOr<A>>,
    visited: List<String>,
): A {
    check(ref !in visited) { "Cyclic $section aliases: ${(visited + ref).joinToString(" -> ")}" }
    if (!ref.startsWith("#")) TODO("Remote $section reference $ref not supported yet.")
    val prefix = "#/components/$section/"
    require(ref.startsWith(prefix)) { "Reference $ref does not point to a component in $prefix." }
    val name = ref.removePrefix(prefix)
    return when (val component = components[name]) {
        is ReferenceOr.Reference -> resolveComponent(component.ref, section, components, visited + ref)
        is ReferenceOr.Value -> component.value
        null -> error("Component $ref could not be found in ${components.keys}.")
    }
}
