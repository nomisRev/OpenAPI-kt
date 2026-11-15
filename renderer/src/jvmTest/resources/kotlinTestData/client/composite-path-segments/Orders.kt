package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Orders internal constructor(
  private val __client: HttpClient,
) {
  public val status: Status = Status(__client)

  public class Status internal constructor(
    private val __client: HttpClient,
  ) {
    public val abandoned: Abandoned = Abandoned(__client)

    public val pendingPayment: PendingPayment = PendingPayment(__client)

    public val paid: Paid = Paid(__client)

    public class Abandoned internal constructor(
      private val __client: HttpClient,
    ) {
      public val json: Json = Json(__client)

      public class Json internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("orders/status/Abandoned.json")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }

    public class PendingPayment internal constructor(
      private val __client: HttpClient,
    ) {
      public val json: Json = Json(__client)

      public class Json internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("orders/status/Pending%20Payment.json")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }

    public class Paid internal constructor(
      private val __client: HttpClient,
    ) {
      public val json: Json = Json(__client)

      public class Json internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("orders/status/Paid.json")
            if (response.status.value != 204) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
