package io.github.nomisrev.render.test.client.path.`param`.oneof.multi.`enum`.shared

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public class Workflows internal constructor(
  private val client: HttpClient,
) {
  public val queued: Queued = Queued(client)

  public val dlq: Dlq = Dlq(client)

  public val inProgress: InProgress = InProgress(client)

  public class Queued internal constructor(
    private val client: HttpClient,
  ) {
    public val runs: Runs = Runs(client)

    public val history: History = History(client)

    public class Runs internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/queued/runs")
        }
      }
    }

    public class History internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/queued/history")
        }
      }
    }
  }

  public class Dlq internal constructor(
    private val client: HttpClient,
  ) {
    public val runs: Runs = Runs(client)

    public val history: History = History(client)

    public class Runs internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/dlq/runs")
        }
      }
    }

    public class History internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/dlq/history")
        }
      }
    }
  }

  public class InProgress internal constructor(
    private val client: HttpClient,
  ) {
    public val runs: Runs = Runs(client)

    public val history: History = History(client)

    public class Runs internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/in-progress/runs")
        }
      }
    }

    public class History internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/in-progress/history")
        }
      }
    }
  }
}
