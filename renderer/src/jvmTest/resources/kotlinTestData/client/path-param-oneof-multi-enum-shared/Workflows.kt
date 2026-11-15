package io.github.nomisrev.render.test.client.path.`param`.oneof.multi.`enum`.shared

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Workflows internal constructor(
  private val __client: HttpClient,
) {
  public val queued: Queued = Queued(__client)

  public val dlq: Dlq = Dlq(__client)

  public val inProgress: InProgress = InProgress(__client)

  public class Queued internal constructor(
    private val __client: HttpClient,
  ) {
    public val runs: Runs = Runs(__client)

    public val history: History = History(__client)

    public class Runs internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/queued/runs")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class History internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/queued/history")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }

  public class Dlq internal constructor(
    private val __client: HttpClient,
  ) {
    public val runs: Runs = Runs(__client)

    public val history: History = History(__client)

    public class Runs internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/dlq/runs")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class History internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/dlq/history")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }

  public class InProgress internal constructor(
    private val __client: HttpClient,
  ) {
    public val runs: Runs = Runs(__client)

    public val history: History = History(__client)

    public class Runs internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/in-progress/runs")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class History internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/in-progress/history")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
