package io.github.nomisrev.render.test.client.path.parameter.shadowing

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.String

public class Accounts internal constructor(
  private val client: HttpClient,
) {
  public fun accountId(accountId: String): AccountIdPath = AccountIdPath(client, accountId)

  public class AccountIdPath internal constructor(
    private val client: HttpClient,
    private val accountId: String,
  ) {
    public val `get`: Get = Get(client, accountId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val accountId: String,
    ) {
      public suspend operator fun invoke(accountId: Int) {
        val response = client.get("accounts/${this.accountId.encodeURLParameter()}") {
          this.url.encodedParameters.append("account_id", accountId.toString())
        }
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }
}
