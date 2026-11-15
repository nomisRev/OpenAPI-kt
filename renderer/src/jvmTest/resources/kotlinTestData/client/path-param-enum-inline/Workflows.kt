package io.github.nomisrev.render.test.client.path.`param`.`enum`.`inline`

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Workflows internal constructor(
  private val __client: HttpClient,
) {
  public val queued: Queued = Queued(__client)

  public val inProgress: InProgress = InProgress(__client)

  public class Queued internal constructor(
    private val __client: HttpClient,
  ) {
    public val runs: Runs = Runs(__client)

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
  }

  public class InProgress internal constructor(
    private val __client: HttpClient,
  ) {
    public val runs: Runs = Runs(__client)

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
  }
}
