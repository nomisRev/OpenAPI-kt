package io.github.nomisrev.render.test.client.path.`param`.oneof.multi.`enum`.shared

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Workflows internal constructor(
  private val client: HttpClient,
) {
  public fun workflowId(workflowId: QueuedOrDlq): WorkflowIdPath {
    val encoded = when (workflowId) {
          QueuedOrDlq.Queued -> "queued"
          QueuedOrDlq.Dlq -> "dlq"
        }
    return WorkflowIdPath(client, encoded)
  }

  public fun workflowId(workflowId: InProgress): WorkflowIdPath {
    val encoded = when (workflowId) {
          InProgress.InProgress -> "in-progress"
        }
    return WorkflowIdPath(client, encoded)
  }

  @Serializable
  public enum class QueuedOrDlq {
    @SerialName("queued")
    Queued,
    @SerialName("dlq")
    Dlq,
  }

  @Serializable
  public enum class InProgress {
    @SerialName("in-progress")
    InProgress,
  }

  public class WorkflowIdPath internal constructor(
    private val client: HttpClient,
    private val workflowId: String,
  ) {
    public val runs: Runs = Runs(client, workflowId)

    public val history: History = History(client, workflowId)

    public class Runs internal constructor(
      private val client: HttpClient,
      private val workflowId: String,
    ) {
      public val `get`: Get = Get(client, workflowId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val workflowId: String,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/$workflowId/runs")
        }
      }
    }

    public class History internal constructor(
      private val client: HttpClient,
      private val workflowId: String,
    ) {
      public val `get`: Get = Get(client, workflowId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val workflowId: String,
      ) {
        public suspend operator fun invoke() {
          client.get("/workflows/$workflowId/history")
        }
      }
    }
  }
}
