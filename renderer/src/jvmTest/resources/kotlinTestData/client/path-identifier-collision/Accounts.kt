package io.github.nomisrev.render.test.client.path.identifier.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Accounts internal constructor(
  private val __client: HttpClient,
) {
  public fun accountId(accountId: String): AccountIdPath = AccountIdPath(__client, accountId)

  public fun accountId2(accountId: String): AccountIdPath2 = AccountIdPath2(__client, accountId)

  public class AccountIdPath internal constructor(
    private val __client: HttpClient,
    private val _accountId: String,
  ) {
    public val `get`: Get = Get(__client, _accountId)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _accountId: String,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("accounts/${this._accountId.encodeURLParameter()}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class AccountIdPath2 internal constructor(
    private val __client: HttpClient,
    private val _accountId: String,
  ) {
    public val members: Members = Members(__client, _accountId)

    public class Members internal constructor(
      private val __client: HttpClient,
      private val _accountId: String,
    ) {
      public val `get`: Get = Get(__client, _accountId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _accountId: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("accounts/${this._accountId.encodeURLParameter()}/members")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
