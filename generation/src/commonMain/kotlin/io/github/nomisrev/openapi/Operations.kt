package io.github.nomisrev.openapi

import kotlin.jvm.JvmInline

@JvmInline
public value class Routes(private val routes: Map<String, List<Route>>): Map<String, List<Route>> by routes

public data class Route(
  val name: String,
  val description: String?,
  val parameters: List<Param>,
  val returnType: ReturnType
) {
  public data class Param(
    val name: String,
    val type: Model,
    val isNullable: Boolean,
    val description: String?,
    val defaultValue: String?
  ) {
    override fun toString(): String {
      val nullable = if (isNullable) "" else "?"
      val default = defaultValue?.let { " = $it" } ?: ""
      val description = if (description != null) """
    |/** $description */
    |""".trimMargin() else ""
      return "$name: $type$nullable$default"
    }
  }

  public data class ReturnType(val type: Model, val isNullable: Boolean) {
    override fun toString(): String {
      val nullable = if (isNullable) "?" else ""
      return "$type$nullable"
    }
  }

  public fun docs(): String = if (description != null) """
    |/** $description */
    |""".trimMargin() else ""
}