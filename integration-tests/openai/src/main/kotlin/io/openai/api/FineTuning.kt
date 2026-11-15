package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
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
import kotlin.collections.Map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class FineTuning internal constructor(
  private val __client: HttpClient,
) {
  public val alpha: Alpha = Alpha(__client)

  public val checkpoints: Checkpoints = Checkpoints(__client)

  public val jobs: Jobs = Jobs(__client)

  public class Alpha internal constructor(
    private val __client: HttpClient,
  ) {
    public val graders: Graders = Graders(__client)

    public class Graders internal constructor(
      private val __client: HttpClient,
    ) {
      public val run: Run = Run(__client)

      public val validate: Validate = Validate(__client)

      public class Run internal constructor(
        private val __client: HttpClient,
      ) {
        public val post: Post = Post(__client)

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(body: RunGraderRequest): RunGraderResponse {
            val response = __client.post("fine_tuning/alpha/graders/run") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Validate internal constructor(
        private val __client: HttpClient,
      ) {
        public val post: Post = Post(__client)

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(body: ValidateGraderRequest): ValidateGraderResponse {
            val response = __client.post("fine_tuning/alpha/graders/validate") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }
  }

  public class Checkpoints internal constructor(
    private val __client: HttpClient,
  ) {
    public fun fineTunedModelCheckpoint(fineTunedModelCheckpoint: String): FineTunedModelCheckpointPath = FineTunedModelCheckpointPath(__client, fineTunedModelCheckpoint)

    public class FineTunedModelCheckpointPath internal constructor(
      private val __client: HttpClient,
      private val _fineTunedModelCheckpoint: String,
    ) {
      public val permissions: Permissions = Permissions(__client, _fineTunedModelCheckpoint)

      public class Permissions internal constructor(
        private val __client: HttpClient,
        private val _fineTunedModelCheckpoint: String,
      ) {
        public val `get`: Get = Get(__client, _fineTunedModelCheckpoint)

        public val post: Post = Post(__client, _fineTunedModelCheckpoint)

        public fun permissionId(permissionId: String): PermissionIdPath = PermissionIdPath(__client, _fineTunedModelCheckpoint, permissionId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _fineTunedModelCheckpoint: String,
        ) {
          public suspend operator fun invoke(
            projectId: String? = null,
            after: String? = null,
            limit: Long? = 10L,
            order: Order? = Order.Descending,
          ): ListFineTuningCheckpointPermissionResponse {
            val response = __client.get("fine_tuning/checkpoints/${this._fineTunedModelCheckpoint.encodeURLParameter()}/permissions") {
              if (projectId != null) {
                this.url.encodedParameters.append("project_id", projectId.encodeURLParameter())
              }
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
              if (order != null) {
                this.url.encodedParameters.append("order", order.value.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }

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
          private val __client: HttpClient,
          private val _fineTunedModelCheckpoint: String,
        ) {
          public suspend operator fun invoke(body: CreateFineTuningCheckpointPermissionRequest): ListFineTuningCheckpointPermissionResponse {
            val response = __client.post("fine_tuning/checkpoints/${this._fineTunedModelCheckpoint.encodeURLParameter()}/permissions") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class PermissionIdPath internal constructor(
          private val __client: HttpClient,
          private val _fineTunedModelCheckpoint: String,
          private val _permissionId: String,
        ) {
          public val delete: Delete = Delete(__client, _fineTunedModelCheckpoint, _permissionId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _fineTunedModelCheckpoint: String,
            private val _permissionId: String,
          ) {
            public suspend operator fun invoke(): DeleteFineTuningCheckpointPermissionResponse {
              val response = __client.delete("fine_tuning/checkpoints/${this._fineTunedModelCheckpoint.encodeURLParameter()}/permissions/${this._permissionId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }
    }
  }

  public class Jobs internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun fineTuningJobId(fineTuningJobId: String): FineTuningJobIdPath = FineTuningJobIdPath(__client, fineTuningJobId)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        after: String? = null,
        limit: Long? = 20L,
        metadata: Map<String, String>? = null,
      ): ListPaginatedFineTuningJobsResponse {
        val response = __client.get("fine_tuning/jobs") {
          if (after != null) {
            this.url.encodedParameters.append("after", after.encodeURLParameter())
          }
          if (limit != null) {
            this.url.encodedParameters.append("limit", limit.toString())
          }
          metadata?.forEach { this.url.encodedParameters.append("metadata%5B${it.key.encodeURLParameter()}%5D", it.value.encodeURLParameter()) }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateFineTuningJobRequest): FineTuningJob {
        val response = __client.post("fine_tuning/jobs") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class FineTuningJobIdPath internal constructor(
      private val __client: HttpClient,
      private val _fineTuningJobId: String,
    ) {
      public val `get`: Get = Get(__client, _fineTuningJobId)

      public val cancel: Cancel = Cancel(__client, _fineTuningJobId)

      public val checkpoints: Checkpoints = Checkpoints(__client, _fineTuningJobId)

      public val events: Events = Events(__client, _fineTuningJobId)

      public val pause: Pause = Pause(__client, _fineTuningJobId)

      public val resume: Resume = Resume(__client, _fineTuningJobId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _fineTuningJobId: String,
      ) {
        public suspend operator fun invoke(): FineTuningJob {
          val response = __client.get("fine_tuning/jobs/${this._fineTuningJobId.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Cancel internal constructor(
        private val __client: HttpClient,
        private val _fineTuningJobId: String,
      ) {
        public val post: Post = Post(__client, _fineTuningJobId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(): FineTuningJob {
            val response = __client.post("fine_tuning/jobs/${this._fineTuningJobId.encodeURLParameter()}/cancel")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Checkpoints internal constructor(
        private val __client: HttpClient,
        private val _fineTuningJobId: String,
      ) {
        public val `get`: Get = Get(__client, _fineTuningJobId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(after: String? = null, limit: Long? = 10L): ListFineTuningJobCheckpointsResponse {
            val response = __client.get("fine_tuning/jobs/${this._fineTuningJobId.encodeURLParameter()}/checkpoints") {
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Events internal constructor(
        private val __client: HttpClient,
        private val _fineTuningJobId: String,
      ) {
        public val `get`: Get = Get(__client, _fineTuningJobId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(after: String? = null, limit: Long? = 20L): ListFineTuningJobEventsResponse {
            val response = __client.get("fine_tuning/jobs/${this._fineTuningJobId.encodeURLParameter()}/events") {
              if (after != null) {
                this.url.encodedParameters.append("after", after.encodeURLParameter())
              }
              if (limit != null) {
                this.url.encodedParameters.append("limit", limit.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Pause internal constructor(
        private val __client: HttpClient,
        private val _fineTuningJobId: String,
      ) {
        public val post: Post = Post(__client, _fineTuningJobId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(): FineTuningJob {
            val response = __client.post("fine_tuning/jobs/${this._fineTuningJobId.encodeURLParameter()}/pause")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class Resume internal constructor(
        private val __client: HttpClient,
        private val _fineTuningJobId: String,
      ) {
        public val post: Post = Post(__client, _fineTuningJobId)

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _fineTuningJobId: String,
        ) {
          public suspend operator fun invoke(): FineTuningJob {
            val response = __client.post("fine_tuning/jobs/${this._fineTuningJobId.encodeURLParameter()}/resume")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }
  }
}
