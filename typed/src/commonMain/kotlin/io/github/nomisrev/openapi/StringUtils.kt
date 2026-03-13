package io.github.nomisrev.openapi

fun String.splitToWords(): List<String> {
    val boundaries = setOf(' ', '-', '_', '.', '/', '[', '*', ']')
    val list = mutableListOf<String>()
    val word = StringBuilder()
    for (index in indices) {
        val ch = this[index]
        if (ch in boundaries) {
            list.add(word.toString().also { word.clear() })
        } else {
            if (ch.isDigitOrUpperCase()) {
                val hasPrev = index > 0
                val hasNext = index < length - 1
                val prevLowerCase = hasPrev && this[index - 1].isLowerCase()
                val prevDigitUpperCase = hasPrev && this[index - 1].isDigitOrUpperCase()
                val nextLowerCase = hasNext && this[index + 1].isLowerCase()
                if (prevLowerCase || (prevDigitUpperCase && nextLowerCase)) {
                    list.add(word.toString().also { word.clear() })
                }
            }
            word.append(ch)
        }
    }
    list.add(word.toString().also { word.clear() })
    return list
}

private fun Char.isDigitOrUpperCase(): Boolean = isDigit() || isUpperCase()

fun String.toPascalCase(): String =
    splitToWords().joinToString("") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
