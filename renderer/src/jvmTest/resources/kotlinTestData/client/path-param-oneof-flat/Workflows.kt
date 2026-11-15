package io.github.nomisrev.render.test.client.path.`param`.oneof.flat

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int

public class Workflows internal constructor(
  private val __client: HttpClient,
) {
  public val queued: Queued = Queued(__client)

  public val inProgress: InProgress = InProgress(__client)

  public fun workflowId(workflowId: Int): WorkflowIdPath = WorkflowIdPath(__client, workflowId)

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

  public class WorkflowIdPath internal constructor(
    private val __client: HttpClient,
    private val _workflowId: Int,
  ) {
    public val runs: Runs = Runs(__client, _workflowId)

    public class Runs internal constructor(
      private val __client: HttpClient,
      private val _workflowId: Int,
    ) {
      public val `get`: Get = Get(__client, _workflowId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _workflowId: Int,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("workflows/${this._workflowId}/runs")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
