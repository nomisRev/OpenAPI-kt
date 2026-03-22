package io.github.nomisrev

data class Expect<out A, out B>(val actual: A, val expected: B)

infix fun <A, B> A.expect(b: B): Expect<A, B> = Expect(this, b)

@Suppress("NoNameShadowing")
fun <A, B, C> List<A>.product(other: List<B>, f: (A, B) -> C): List<C> =
    flatMap { a -> other.map { b -> f(a, b) } }

@Suppress("NoNameShadowing")
fun <A, B, C, D> List<A>.product(other: List<B>, other2: List<C>, f: (A, B, C) -> D): List<D> =
    flatMap { a ->
        other.flatMap { b -> other2.map { c -> f(a, b, c) } }
    }

@Suppress("NoNameShadowing")
fun <A, B, C, D, E> List<A>.product(b: List<B>, c: List<C>, d: List<D>, f: (A, B, C, D) -> E): List<E> =
    flatMap { a -> b.flatMap { b -> c.flatMap { c -> d.map { d -> f(a, b, c, d) } } } }

@Suppress("NoNameShadowing", "LongParameterList")
fun <A, B, C, D, E, F> List<A>.product(
    b: List<B>,
    c: List<C>,
    d: List<D>,
    e: List<E>,
    f: (A, B, C, D, E) -> F
): List<F> =
    flatMap { a -> b.flatMap { b -> c.flatMap { c -> d.flatMap { d -> e.map { e -> f(a, b, c, d, e) } } } } }

@Suppress("NoNameShadowing", "LongParameterList")
fun <A, B, C, D, E, F, G> List<A>.product(
    b: List<B>,
    c: List<C>,
    d: List<D>,
    e: List<E>,
    f: List<F>,
    g: (A, B, C, D, E, F) -> G
): List<G> =
    flatMap { a ->
        b.flatMap { b ->
            c.flatMap { c ->
                d.flatMap { d ->
                    e.flatMap { e ->
                        f.map { f ->
                            g(
                                a,
                                b,
                                c,
                                d,
                                e,
                                f
                            )
                        }
                    }
                }
            }
        }
    }
