package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Orders internal constructor(
  private val client: HttpClient,
) {
  public val status: Status = Status(client)

  public class Status internal constructor(
    private val client: HttpClient,
  ) {
    public val abandoned: Abandoned = Abandoned(client)

    public val pendingPayment: PendingPayment = PendingPayment(client)

    public val paid: Paid = Paid(client)

    public class Abandoned internal constructor(
      private val client: HttpClient,
    ) {
      public val json: Json = Json(client)

      public class Json internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = client.get("orders/status/Abandoned.json")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }

    public class PendingPayment internal constructor(
      private val client: HttpClient,
    ) {
      public val json: Json = Json(client)

      public class Json internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = client.get("orders/status/Pending%20Payment.json")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }

    public class Paid internal constructor(
      private val client: HttpClient,
    ) {
      public val json: Json = Json(client)

      public class Json internal constructor(
        private val client: HttpClient,
      ) {
        public val `get`: Get = Get(client)

        public class Get internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = client.get("orders/status/Paid.json")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
