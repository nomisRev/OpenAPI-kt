package io.github.nomisrev.openapi

import kotlin.jvm.JvmInline

/**
 * Tracks whether data was referenced by name, or defined inline. This is important to be able to
 * maintain the structure of the specification during transformation.
 */
internal sealed interface Resolved<A> {
  val value: A

  data class Ref<A>(val name: String, override val value: A) : Resolved<A>

  @JvmInline
  value class Value<A>(override val value: A) : Resolved<A>
}

/** Returns a NamingContext for this Resolved, or computes one if it was a Value. */
internal fun Resolved<*>.namedOr(orElse: () -> NamingContext): NamingContext =
  when (this) {
    is Resolved.Ref -> NamingContext.Named(name)
    is Resolved.Value -> orElse()
  }

/** Returns the value for inline values, or null if it was a named reference. */
internal fun <A> Resolved<A>.valueOrNull(): A? =
  when (this) {
    is Resolved.Ref -> null
    is Resolved.Value -> value
  }

internal fun Resolved<Schema>.copy(block: (Schema) -> Schema): Resolved<Schema> =
  when (this) {
    is Resolved.Ref -> Resolved.Ref(name, block(value))
    is Resolved.Value -> Resolved.Value(block(value))
  }
