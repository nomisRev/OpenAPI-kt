package io.github.nomisrev.render


fun String.diff(other: String): String {
    val expectedLines = lines()
    val actualLines = other.lines()

    // Unified diff with a small amount of context, similar to `git diff`.
    val ops = lineDiff(expectedLines, actualLines)
    val diffLines = buildUnifiedLines(ops)
    val hunks = buildHunks(diffLines, context = 3)

    return buildString {
        appendLine("--- expected")
        appendLine("+++ actual")
        if (hunks.isEmpty()) {
            // Keep output small and readable on exact matches.
            appendLine("(no differences)")
        } else {
            hunks.forEach { hunk ->
                appendLine(hunk.header)
                hunk.lines.forEach { line ->
                    appendLine(line.prefix + line.text)
                }
            }
        }
    }.trimEnd()
}

private enum class OpType { Equal, Delete, Insert }

private data class Op(val type: OpType, val text: String)

private data class UnifiedLine(
    val prefix: String,
    val text: String,
    val oldNo: Int?,
    val newNo: Int?,
    val isChange: Boolean,
)

private data class Hunk(val header: String, val lines: List<UnifiedLine>)

private fun lineDiff(expected: List<String>, actual: List<String>): List<Op> {
    // LCS-based diff. O(n*m) is fine for test output sizes.
    val n = expected.size
    val m = actual.size
    val dp = Array(n + 1) { IntArray(m + 1) }

    for (i in n - 1 downTo 0) {
        for (j in m - 1 downTo 0) {
            dp[i][j] = if (expected[i] == actual[j]) {
                dp[i + 1][j + 1] + 1
            } else {
                maxOf(dp[i + 1][j], dp[i][j + 1])
            }
        }
    }

    val result = ArrayList<Op>(n + m)
    var i = 0
    var j = 0
    while (i < n && j < m) {
        when {
            expected[i] == actual[j] -> {
                result += Op(OpType.Equal, expected[i])
                i++
                j++
            }

            dp[i + 1][j] >= dp[i][j + 1] -> {
                result += Op(OpType.Delete, expected[i])
                i++
            }

            else -> {
                result += Op(OpType.Insert, actual[j])
                j++
            }
        }
    }
    while (i < n) {
        result += Op(OpType.Delete, expected[i])
        i++
    }
    while (j < m) {
        result += Op(OpType.Insert, actual[j])
        j++
    }

    return result
}

private fun buildUnifiedLines(ops: List<Op>): List<UnifiedLine> {
    var oldNo = 1
    var newNo = 1
    val out = ArrayList<UnifiedLine>(ops.size)

    ops.forEach { op ->
        when (op.type) {
            OpType.Equal -> {
                out += UnifiedLine(" ", op.text, oldNo, newNo, isChange = false)
                oldNo++
                newNo++
            }

            OpType.Delete -> {
                out += UnifiedLine("-", op.text, oldNo, null, isChange = true)
                oldNo++
            }

            OpType.Insert -> {
                out += UnifiedLine("+", op.text, null, newNo, isChange = true)
                newNo++
            }
        }
    }

    return out
}

private fun buildHunks(lines: List<UnifiedLine>, context: Int): List<Hunk> {
    if (lines.none { it.isChange }) return emptyList()

    val changeIdxs = lines.indices.filter { lines[it].isChange }
    val ranges = ArrayList<IntRange>()
    var currentStart = maxOf(0, changeIdxs.first() - context)
    var currentEnd = minOf(lines.lastIndex, changeIdxs.first() + context)

    changeIdxs.drop(1).forEach { idx ->
        val start = maxOf(0, idx - context)
        val end = minOf(lines.lastIndex, idx + context)
        if (start <= currentEnd + 1) {
            currentEnd = maxOf(currentEnd, end)
        } else {
            ranges += currentStart..currentEnd
            currentStart = start
            currentEnd = end
        }
    }
    ranges += currentStart..currentEnd

    return ranges.map { r ->
        val hunkLines = lines.subList(r.first, r.last + 1)
        Hunk(buildHunkHeader(hunkLines), hunkLines)
    }
}

private fun buildHunkHeader(hunkLines: List<UnifiedLine>): String {
    val oldNos = hunkLines.mapNotNull { it.oldNo }
    val newNos = hunkLines.mapNotNull { it.newNo }

    val oldStart = if (oldNos.isNotEmpty()) oldNos.first() else (newNos.firstOrNull() ?: 1) - 1
    val newStart = if (newNos.isNotEmpty()) newNos.first() else (oldNos.firstOrNull() ?: 1) - 1

    val oldCount = hunkLines.count { it.oldNo != null }
    val newCount = hunkLines.count { it.newNo != null }

    fun formatRange(start: Int, count: Int): String = when (count) {
        1 -> "$start"
        else -> "$start,$count"
    }

    return "@@ -${formatRange(oldStart, oldCount)} +${formatRange(newStart, newCount)} @@"
}