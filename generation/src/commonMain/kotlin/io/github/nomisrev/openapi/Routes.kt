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
  public sealed interface Body {
    @JvmInline
    public value class Json(public val param: Param) : Body

    public data class Multipart(val parameters: List<Param>) : Body
  }

  public sealed interface Input {
    public val parameter: Param

    @JvmInline
    public value class Query(override val parameter: Param) : Input
    @JvmInline
    public value class Path(override val parameter: Param) : Input
    @JvmInline
    public value class Header(override val parameter: Param) : Input
    @JvmInline
    public value class Cookie(override val parameter: Param) : Input
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