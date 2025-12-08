package io.github.nomisrev

import io.github.nomisrev.transformers.forever

fun <T, T2, T3, V> Sequence<T>.zip(
    other: Sequence<T2>,
    other2: Sequence<T3>,
    transform: (a: T, b: T2, c: T3) -> V
): Sequence<V> = zip(other, other2,
    sequenceOf(Unit).forever(),
) { a, b, c, _ -> transform(a, b, c) }

fun <T, T2, T3, T4, V> Sequence<T>.zip(
    other: Sequence<T2>,
    other2: Sequence<T3>,
    other3: Sequence<T4>,
    transform: (a: T, b: T2, c: T3, d: T4) -> V
): Sequence<V> =
    MergingSequence(this, other, other2, other3, transform)

private class MergingSequence<T1, T2, T3, T4, V>(
    private val sequence1: Sequence<T1>,
    private val sequence2: Sequence<T2>,
    private val sequence3: Sequence<T3>,
    private val sequence4: Sequence<T4>,
    private val transform: (T1, T2, T3, T4) -> V
) : Sequence<V> {
    override fun iterator(): Iterator<V> = object : Iterator<V> {
        val iterator1 = sequence1.iterator()
        val iterator2 = sequence2.iterator()
        val iterator3 = sequence3.iterator()
        val iterator4 = sequence4.iterator()
        override fun next(): V {
            return transform(iterator1.next(), iterator2.next(), iterator3.next(), iterator4.next())
        }

        override fun hasNext(): Boolean {
            return iterator1.hasNext() && iterator2.hasNext() && iterator3.hasNext() && iterator4.hasNext()
        }
    }
}