package io.github.nomisrev.render.test.client.status.code.case.names

import io.ktor.http.HttpStatusCode

public val HttpStatusCode.Companion.UnavailableForLegalReasons: HttpStatusCode
  get() = HttpStatusCode(451, "Unavailable For Legal Reasons")
