package io.github.nomisrev.render.test.client.state.name.collision

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class RealmPath internal constructor(
  private val __client: HttpClient,
  private val _realm: String,
) {
  public val roles: Roles = Roles(__client, _realm)

  public class Roles internal constructor(
    private val __client: HttpClient,
    private val _realm: String,
  ) {
    public val realm: Realm = Realm(__client, _realm)

    public class Realm internal constructor(
      private val __client: HttpClient,
      private val _realm: String,
    ) {
      public val `get`: Get = Get(__client, _realm)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _realm: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("${this._realm.encodeURLParameter()}/roles/realm")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
