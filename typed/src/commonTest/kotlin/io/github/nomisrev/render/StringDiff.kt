package io.github.nomisrev.render


fun String.diff(other: String): String =
    lines().zip(other.lines()).joinToString(separator = "\n") { (expected, actual) ->
        if (expected == actual) actual
        else {
            val diff = buildString {
                val es = expected.toList().iterator()
                val `as` = actual.toList().iterator()
                while (es.hasNext() || `as`.hasNext()) {
                    val e = if (es.hasNext()) es.next() else null
                    val a = if (`as`.hasNext()) `as`.next() else null
                    if (e == a) append(" ") else append("^")
                }
            }

            """
            |$actual
            |$diff
            """.trimMargin()
        }
    }