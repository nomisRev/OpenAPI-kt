package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.openai.model.CreateFineTuningCheckpointPermissionRequest
import io.openai.model.CreateFineTuningJobRequest
import io.openai.model.DeleteFineTuningCheckpointPermissionResponse
import io.openai.model.FineTuningJob
import io.openai.model.ListFineTuningCheckpointPermissionResponse
import io.openai.model.ListFineTuningJobCheckpointsResponse
import io.openai.model.ListFineTuningJobEventsResponse
import io.openai.model.ListPaginatedFineTuningJobsResponse
import io.openai.model.RunGraderRequest
import io.openai.model.RunGraderResponse
import io.openai.model.ValidateGraderRequest
import io.openai.model.ValidateGraderResponse
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class FineTuning internal constructor(
  private val client: HttpClient,
) {
  public val alpha: Alpha = Alpha(client)

  public val checkpoints: Checkpoints = Checkpoints(client)

  public val jobs: Jobs = Jobs(client)

  public class Alpha internal constructor(
    private val client: HttpClient,
  ) {
    public val graders: Graders = Graders(client)

    public class Graders internal constructor(
      private val client: HttpClient,
    ) {
      public val run: Run = Run(client)

      public val validate: Validate = Validate(client)

      public class Run internal constructor(
        private val client: HttpClient,
      ) {
        public val post: Post = Post(client)

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(body: RunGraderRequest): RunGraderResponse = client.post("/fine_tuning/alpha/graders/run") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }
      }

      public class Validate internal constructor(
        private val client: HttpClient,
      ) {
        public val post: Post = Post(client)

        public class Post internal constructor(
          private val client: HttpClient,
        ) {
          public suspend operator fun invoke(body: ValidateGraderRequest): ValidateGraderResponse = client.post("/fine_tuning/alpha/graders/validate") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }
      }
    }
  }

  public class Checkpoints internal constructor(
    private val client: HttpClient,
  ) {
    public fun fineTunedModelCheckpoint(fineTunedModelCheckpoint: String): FineTunedModelCheckpointPath = FineTunedModelCheckpointPath(client, fineTunedModelCheckpoint)

    public class FineTunedModelCheckpointPath internal constructor(
      private val client: HttpClient,
      private val fineTunedModelCheckpoint: String,
    ) {
      public val permissions: Permissions = Permissions(client, fineTunedModelCheckpoint)

      public class Permissions internal constructor(
        private val client: HttpClient,
        private val fineTunedModelCheckpoint: String,
      ) {
        public val `get`: Get = Get(client, fineTunedModelCheckpoint)

        public val post: Post = Post(client, fineTunedModelCheckpoint)

        public fun permissionId(permissionId: String): PermissionIdPath = PermissionIdPath(client, fineTunedModelCheckpoint, permissionId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val fineTunedModelCheckpoint: String,
        ) {
          public suspend operator fun invoke(
            projectId: String? = null,
            after: String? = null,
            limit: Long? = 10L,
            order: Order? = Order.Descending,
          ): ListFineTuningCheckpointPermissionResponse = client.get("/fine_tuning/checkpoints/$fineTunedModelCheckpoint/permissions") {
            projectId?.let { parameter("project_id", it) }
            after?.let { parameter("after", it) }
            limit?.let { parameter("limit", it) }
            order?.let { parameter("order", it.value) }
          }.body()

          @Serializable
          public enum class Order(
            public val `value`: String,
          ) {
            @SerialName("ascending")
            Ascending("ascending"),
            @SerialName("descending")
            Descending("descending"),
            ;
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val fineTunedModelCheckpoint: String,
        ) {
          public suspend operator fun invoke(body: CreateFineTuningCheckpointPermissionRequest): ListFineTuningCheckpointPermissionResponse = client.post("/fine_tuning/checkpoints/$fineTunedModelCheckpoint/permissions") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }.body()
        }

        public class PermissionIdPath internal constructor(
          private val client: HttpClient,
          private val fineTunedModelCheckpoint: String,
          private val permissionId: String,
        ) {
          public val delete: Delete = Delete(client, fineTunedModelCheckpoint, permissionId)

          public class Delete internal constructor(
            private val client: HttpClient,
            private val fineTunedModelCheckpoint: String,
            private val permissionId: String,
          ) {
            public suspend operator fun invoke(): DeleteFineTuningCheckpointPermissionResponse = client.delete("/fine_tuning/checkpoints/$fineTunedModelCheckpoint/permissions/$permissionId").body()
          }
        }
      }
    }
  }

  public class Jobs internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun fineTuningJobId(fineTuningJobId: String): FineTuningJobIdPath = FineTuningJobIdPath(client, fineTuningJobId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        after: String? = null,
        limit: Long? = 20L,
        metadata: List<String>? = emptyList(),
      ): ListPaginatedFineTuningJobsResponse = client.get("/fine_tuning/jobs") {
        after?.let { parameter("after", it) }
        limit?.let { parameter("limit", it) }
        metadata?.let { parameter("metadata", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateFineTuningJobRequest): FineTuningJob = client.post("/fine_tuning/jobs") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()
    }

    public class FineTuningJobIdPath internal constructor(
      private val client: HttpClient,
      private val fineTuningJobId: String,
    ) {
      public val `get`: Get = Get(client, fineTuningJobId)

      public val cancel: Cancel = Cancel(client, fineTuningJobId)

      public val checkpoints: Checkpoints = Checkpoints(client, fineTuningJobId)

      public val events: Events = Events(client, fineTuningJobId)

      public val pause: Pause = Pause(client, fineTuningJobId)

      public val resume: Resume = Resume(client, fineTuningJobId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val fineTuningJobId: String,
      ) {
        public suspend operator fun invoke(): FineTuningJob = client.get("/fine_tuning/jobs/$fineTuningJobId").body()
      }

      public class Cancel internal constructor(
        private val client: HttpClient,
        private val fineTuningJobId: String,
      ) {
        public val post: Post = Post(client, fineTuningJobId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(): FineTuningJob = client.post("/fine_tuning/jobs/$fineTuningJobId/cancel").body()
        }
      }

      public class Checkpoints internal constructor(
        private val client: HttpClient,
        private val fineTuningJobId: String,
      ) {
        public val `get`: Get = Get(client, fineTuningJobId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(after: String? = null, limit: Long? = 10L): ListFineTuningJobCheckpointsResponse = client.get("/fine_tuning/jobs/$fineTuningJobId/checkpoints") {
            after?.let { parameter("after", it) }
            limit?.let { parameter("limit", it) }
          }.body()
        }
      }

      public class Events internal constructor(
        private val client: HttpClient,
        private val fineTuningJobId: String,
      ) {
        public val `get`: Get = Get(client, fineTuningJobId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(after: String? = null, limit: Long? = 20L): ListFineTuningJobEventsResponse = client.get("/fine_tuning/jobs/$fineTuningJobId/events") {
            after?.let { parameter("after", it) }
            limit?.let { parameter("limit", it) }
          }.body()
        }
      }

      public class Pause internal constructor(
        private val client: HttpClient,
        private val fineTuningJobId: String,
      ) {
        public val post: Post = Post(client, fineTuningJobId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(): FineTuningJob = client.post("/fine_tuning/jobs/$fineTuningJobId/pause").body()
        }
      }

      public class Resume internal constructor(
        private val client: HttpClient,
        private val fineTuningJobId: String,
      ) {
        public val post: Post = Post(client, fineTuningJobId)

        public class Post internal constructor(
          private val client: HttpClient,
          private val fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(): FineTuningJob = client.post("/fine_tuning/jobs/$fineTuningJobId/resume").body()
        }
      }
    }
  }
}
