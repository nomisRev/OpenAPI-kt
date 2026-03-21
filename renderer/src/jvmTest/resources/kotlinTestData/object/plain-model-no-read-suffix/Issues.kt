package io.github.nomisrev.render.test.`object`.plain.model.no.read.suffix

import io.github.nomisrev.render.test.object_.plain.model.no.read.suffix.IssueKey
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String

public class Issues internal constructor(
  private val client: HttpClient,
) {
  public fun id(id: String): IdPath = IdPath(client, id)

  public class IdPath internal constructor(
    private val client: HttpClient,
    private val id: String,
  ) {
    public val `get`: Get = Get(client, id)

    public class Get internal constructor(
      private val client: HttpClient,
      private val id: String,
    ) {
      public suspend operator fun invoke(): IssueKey = client.get("/issues/$id").body()
    }
  }
}
