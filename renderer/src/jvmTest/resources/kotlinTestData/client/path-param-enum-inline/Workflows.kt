package io.github.nomisrev.render.test.client.path.`param`.`enum`.`inline`

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`

public class Workflows internal constructor(
  private val client: HttpClient,
) {
  public val queued: Queued = Queued(client)

  public val inProgress: InProgress = InProgress(client)

  public class Queued internal constructor(
    private val client: HttpClient,
  ) {
    public val runs: Runs = Runs(client)

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
  }

  public class InProgress internal constructor(
    private val client: HttpClient,
  ) {
    public val runs: Runs = Runs(client)

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
  }
}
