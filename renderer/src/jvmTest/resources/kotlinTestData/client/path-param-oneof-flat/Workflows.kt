package io.github.nomisrev.render.test.client.path.`param`.oneof.flat

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public interface Workflows {
  public fun workflowId(workflowId: Int): WorkflowIdPath

  public fun workflowId(workflowId: WorkflowId): WorkflowIdPath

  @Serializable
  public enum class WorkflowId {
    @SerialName("queued")
    Queued,
    @SerialName("in-progress")
    InProgress,
  }

  public interface WorkflowIdPath {
    public val runs: Runs

    public interface Runs {
      public val `get`: Get

      public interface Get {
        public suspend operator fun invoke()
      }
    }
  }
}

internal class KtorWorkflows(
  private val client: HttpClient,
) : Workflows {
  override fun workflowId(workflowId: Int): Workflows.WorkflowIdPath = KtorWorkflowsWorkflowIdPath(client, workflowId.toString())

  override fun workflowId(workflowId: Workflows.WorkflowId): Workflows.WorkflowIdPath {
    val encoded = when (workflowId) {
          Workflows.WorkflowId.Queued -> "queued"
          Workflows.WorkflowId.InProgress -> "in-progress"
        }
    return KtorWorkflowsWorkflowIdPath(client, encoded)
  }
}

internal class KtorWorkflowsWorkflowIdPath(
  private val client: HttpClient,
  private val workflowId: String,
) : Workflows.WorkflowIdPath {
  override val runs: Workflows.WorkflowIdPath.Runs =
      KtorWorkflowsWorkflowIdPathRuns(client, workflowId)
}

internal class KtorWorkflowsWorkflowIdPathRuns(
  private val client: HttpClient,
  private val workflowId: String,
) : Workflows.WorkflowIdPath.Runs {
  override val `get`: Workflows.WorkflowIdPath.Runs.Get =
      object : Workflows.WorkflowIdPath.Runs.Get {
    override suspend operator fun invoke() {
      client.get("/workflows/$workflowId/runs")
    }
  }
}
