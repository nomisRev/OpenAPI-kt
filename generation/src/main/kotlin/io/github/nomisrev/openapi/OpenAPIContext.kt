package io.github.nomisrev.openapi

interface OpenAPIContext : Naming, APIInterceptor {
  val `package`: String
}

fun OpenAPIContext(
  `package`: String,
  interceptor: APIInterceptor = APIInterceptor.NoOp
): OpenAPIContext = object : OpenAPIContext,
  Naming by Naming(`package`),
  APIInterceptor by interceptor {
  override val `package`: String = `package`
}
