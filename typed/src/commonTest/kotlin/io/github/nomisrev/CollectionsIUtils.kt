package io.github.nomisrev

@Suppress("ComplexCondition")
fun <T, T2, T3, T4, V> List<T>.zip(
    other: List<T2>,
    other2: List<T3>,
    other3: List<T4>,
    transform: (a: T, b: T2, c: T3, d: T4) -> V
): List<V> {
    val first = iterator()
    val second = other.iterator()
    val third = other2.iterator()
    val fourth = other3.iterator()
    val list = ArrayList<V>(minOf(size, other.size, other2.size, other3.size))
    while (first.hasNext() && second.hasNext() && third.hasNext() && fourth.hasNext()) {
        list.add(transform(first.next(), second.next(), third.next(), fourth.next()))
    }
    return list
}
