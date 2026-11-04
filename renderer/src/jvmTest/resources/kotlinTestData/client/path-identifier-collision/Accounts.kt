package io.github.nomisrev.render.test.client.path.identifier.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Accounts internal constructor(
  private val client: HttpClient,
) {
  public fun accountId(accountId: String): AccountIdPath = AccountIdPath(client, accountId)

  public fun accountId2(accountId: String): AccountIdPath2 = AccountIdPath2(client, accountId)

  public class AccountIdPath internal constructor(
    private val client: HttpClient,
    private val accountId: String,
  ) {
    public val `get`: Get = Get(client, accountId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val accountId: String,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("accounts/${accountId.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class AccountIdPath2 internal constructor(
    private val client: HttpClient,
    private val accountId: String,
  ) {
    public val members: Members = Members(client, accountId)

    public class Members internal constructor(
      private val client: HttpClient,
      private val accountId: String,
    ) {
      public val `get`: Get = Get(client, accountId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val accountId: String,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("accounts/${accountId.encodeURLParameter()}/members")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
