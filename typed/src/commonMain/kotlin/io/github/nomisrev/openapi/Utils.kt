package io.github.nomisrev.openapi

fun <A> requireUnique(values: Iterable<A>, message: String? = null) =
    require(values.count() == values.toSet().size) {
        if (message == null) {
            val duplicates = values.groupBy { it }.filter { it.value.size > 1 }.keys
            "Non-unique entries found: $duplicates"
        } else message
    }

inline fun <Key, Value> Map<Key, Value>.merge(
    other: Map<Key, Value>,
    merge: (key: Key, Value, Value) -> Value
): Map<Key, Value> = buildMap {
    this@merge.forEach { (key, value) ->
        if (other.contains(key)) {
            val otherValue = other[key]!!
            if (value != otherValue) {
                put(key, merge(key, value, otherValue))
            } else put(key, otherValue)
        } else {
            put(key, value)
        }
    }
    putAll(other - this@merge.keys)
}