package io.github.nomisrev.render.test.client.path.`param`.oneof.flat

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Workflows internal constructor(
  private val client: HttpClient,
) {
  public fun workflowId(workflowId: Int): WorkflowIdPath = WorkflowIdPath(client, workflowId.toString())

  public fun workflowId(workflowId: WorkflowId): WorkflowIdPath {
    val encoded = when (workflowId) {
          WorkflowId.Queued -> "queued"
          WorkflowId.InProgress -> "in-progress"
        }
    return WorkflowIdPath(client, encoded)
  }

  @Serializable
  public enum class WorkflowId {
    @SerialName("queued")
    Queued,
    @SerialName("in-progress")
    InProgress,
  }

  public class WorkflowIdPath internal constructor(
    private val client: HttpClient,
    private val workflowId: String,
  ) {
    public val runs: Runs = Runs(client, workflowId)

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
  }
}
