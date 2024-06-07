package io.github.nomisrev.openapi

import kotlin.jvm.JvmInline

public sealed interface ExpressionOrValue {
  @JvmInline public value class Expression(public val value: String) : ExpressionOrValue

  @JvmInline public value class Value(public val value: Any?) : ExpressionOrValue
}
