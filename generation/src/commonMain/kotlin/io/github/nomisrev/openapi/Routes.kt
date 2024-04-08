package io.github.nomisrev.openapi

import kotlin.jvm.JvmInline

@JvmInline
public value class Routes(private val routes: Map<String, List<Route>>) : Map<String, List<Route>> by routes

public data class Route(
  val name: String,
  val description: String?,
  val body: Body?,
  val input: List<Input>,
  val returnType: ReturnType
) {
  sealed interface Body {
    @JvmInline
    value class Json(val param: Param) : Body

    data class Multipart(val parameters: List<Param>) : Body
  }

  sealed interface Input {
    val parameter: Param

    @JvmInline
    value class Query(override val parameter: Param) : Input
    @JvmInline
    value class Path(override val parameter: Param) : Input
    @JvmInline
    value class Header(override val parameter: Param) : Input
    @JvmInline
    value class Cookie(override val parameter: Param) : Input
  }

  // TODO Turn into actual types: Query, Path, Param, etc.
  public data class Param(
    val name: String,
    val type: Model,
    val isNullable: Boolean,
    val description: String?,
    val defaultValue: String?
  )

  public data class ReturnType(val type: Model, val isNullable: Boolean)
}