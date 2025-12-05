package io.github.nomisrev.openapi

fun <A> requireUnique(values: Iterable<A>, message: String? = null) {
    require(values.count() == values.toSet().size) { 
        if (message == null) {
            val duplicates = values.groupBy { it }.filter { it.value.size > 1 }.keys
            "Non-unique entries found: $duplicates"
        } else message
    }
}