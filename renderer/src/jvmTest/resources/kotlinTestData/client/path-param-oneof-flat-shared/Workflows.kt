package io.github.nomisrev.render.test.client.path.`param`.oneof.flat.shared

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Int

public class Workflows internal constructor(
  private val client: HttpClient,
) {
  public val queued: Queued = Queued(client)

  public val inProgress: InProgress = InProgress(client)

  public fun workflowId(workflowId: Int): WorkflowIdPath = WorkflowIdPath(client, workflowId)

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

  public class WorkflowIdPath internal constructor(
    private val client: HttpClient,
    private val workflowId: Int,
  ) {
    public val runs: Runs = Runs(client, workflowId)

    public val history: History = History(client, workflowId)

    public class Runs internal constructor(
      private val client: HttpClient,
      private val workflowId: Int,
    ) {
      public val `get`: Get = Get(client, workflowId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val workflowId: Int,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/$workflowId/runs")
        }
      }
    }

    public class History internal constructor(
      private val client: HttpClient,
      private val workflowId: Int,
    ) {
      public val `get`: Get = Get(client, workflowId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val workflowId: Int,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/$workflowId/history")
        }
      }
    }
  }
}
