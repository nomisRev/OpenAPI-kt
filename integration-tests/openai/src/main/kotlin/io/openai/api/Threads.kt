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
import io.openai.model.CreateMessageRequest
import io.openai.model.CreateRunRequest
import io.openai.model.CreateThreadAndRunRequest
import io.openai.model.CreateThreadRequest
import io.openai.model.DeleteMessageResponse
import io.openai.model.DeleteThreadResponse
import io.openai.model.ListMessagesResponse
import io.openai.model.ListRunStepsResponse
import io.openai.model.ListRunsResponse
import io.openai.model.MessageObject
import io.openai.model.ModifyMessageRequest
import io.openai.model.ModifyRunRequest
import io.openai.model.ModifyThreadRequest
import io.openai.model.RunObject
import io.openai.model.RunStepObject
import io.openai.model.SubmitToolOutputsRunRequest
import io.openai.model.ThreadObject
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Threads internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val runs: Runs = Runs(client)

  public fun threadId(threadId: String): ThreadIdPath = ThreadIdPath(client, threadId)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateThreadRequest? = null): ThreadObject {
      val response = client.post("threads") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Runs internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateThreadAndRunRequest): RunObject {
        val response = client.post("threads/runs") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class ThreadIdPath internal constructor(
    private val client: HttpClient,
    private val threadId: String,
  ) {
    public val delete: Delete = Delete(client, threadId)

    public val `get`: Get = Get(client, threadId)

    public val post: Post = Post(client, threadId)

    public val messages: Messages = Messages(client, threadId)

    public val runs: Runs = Runs(client, threadId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val threadId: String,
    ) {
      public suspend operator fun invoke(): DeleteThreadResponse {
        val response = client.delete("threads/${this.threadId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val threadId: String,
    ) {
      public suspend operator fun invoke(): ThreadObject {
        val response = client.get("threads/${this.threadId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val threadId: String,
    ) {
      public suspend operator fun invoke(body: ModifyThreadRequest): ThreadObject {
        val response = client.post("threads/${this.threadId.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Messages internal constructor(
      private val client: HttpClient,
      private val threadId: String,
    ) {
      public val `get`: Get = Get(client, threadId)

      public val post: Post = Post(client, threadId)

      public fun messageId(messageId: String): MessageIdPath = MessageIdPath(client, threadId, messageId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
          before: String? = null,
          runId: String? = null,
        ): ListMessagesResponse {
          val response = client.get("threads/${this.threadId.encodeURLParameter()}/messages") {
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
            if (before != null) {
              this.url.encodedParameters.append("before", before.encodeURLParameter())
            }
            if (runId != null) {
              this.url.encodedParameters.append("run_id", runId.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        public enum class Order(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public suspend operator fun invoke(body: CreateMessageRequest): MessageObject {
          val response = client.post("threads/${this.threadId.encodeURLParameter()}/messages") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class MessageIdPath internal constructor(
        private val client: HttpClient,
        private val threadId: String,
        private val messageId: String,
      ) {
        public val delete: Delete = Delete(client, threadId, messageId)

        public val `get`: Get = Get(client, threadId, messageId)

        public val post: Post = Post(client, threadId, messageId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val messageId: String,
        ) {
          public suspend operator fun invoke(): DeleteMessageResponse {
            val response = client.delete("threads/${this.threadId.encodeURLParameter()}/messages/${this.messageId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val messageId: String,
        ) {
          public suspend operator fun invoke(): MessageObject {
            val response = client.get("threads/${this.threadId.encodeURLParameter()}/messages/${this.messageId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val messageId: String,
        ) {
          public suspend operator fun invoke(body: ModifyMessageRequest): MessageObject {
            val response = client.post("threads/${this.threadId.encodeURLParameter()}/messages/${this.messageId.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }

    public class Runs internal constructor(
      private val client: HttpClient,
      private val threadId: String,
    ) {
      public val `get`: Get = Get(client, threadId)

      public val post: Post = Post(client, threadId)

      public fun runId(runId: String): RunIdPath = RunIdPath(client, threadId, runId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
          before: String? = null,
        ): ListRunsResponse {
          val response = client.get("threads/${this.threadId.encodeURLParameter()}/runs") {
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
            if (before != null) {
              this.url.encodedParameters.append("before", before.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        public enum class Order(
          public val `value`: String,
        ) {
          @SerialName("asc")
          Asc("asc"),
          @SerialName("desc")
          Desc("desc"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val threadId: String,
      ) {
        public suspend operator fun invoke(body: CreateRunRequest, include: List<Include>? = null): RunObject {
          val response = client.post("threads/${this.threadId.encodeURLParameter()}/runs") {
            include?.forEach { this.url.encodedParameters.append("include%5B%5D", it.value.encodeURLParameter()) }
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        @Serializable
        public enum class Include(
          public val `value`: String,
        ) {
          @SerialName("step_details.tool_calls[*].file_search.results[*].content")
          StepDetailsToolCallsFileSearchResultsContent("step_details.tool_calls[*].file_search.results[*].content"),
          ;
        }
      }

      public class RunIdPath internal constructor(
        private val client: HttpClient,
        private val threadId: String,
        private val runId: String,
      ) {
        public val `get`: Get = Get(client, threadId, runId)

        public val post: Post = Post(client, threadId, runId)

        public val cancel: Cancel = Cancel(client, threadId, runId)

        public val steps: Steps = Steps(client, threadId, runId)

        public val submitToolOutputs: SubmitToolOutputs = SubmitToolOutputs(client, threadId, runId)

        public class Get internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val runId: String,
        ) {
          public suspend operator fun invoke(): RunObject {
            val response = client.get("threads/${this.threadId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val runId: String,
        ) {
          public suspend operator fun invoke(body: ModifyRunRequest): RunObject {
            val response = client.post("threads/${this.threadId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Cancel internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val runId: String,
        ) {
          public val post: Post = Post(client, threadId, runId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val threadId: String,
            private val runId: String,
          ) {
            public suspend operator fun invoke(): RunObject {
              val response = client.post("threads/${this.threadId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}/cancel")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class Steps internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val runId: String,
        ) {
          public val `get`: Get = Get(client, threadId, runId)

          public fun stepId(stepId: String): StepIdPath = StepIdPath(client, threadId, runId, stepId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val threadId: String,
            private val runId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = 20L,
              order: Order? = Order.Desc,
              after: String? = null,
              before: String? = null,
              include: List<Include>? = null,
            ): ListRunStepsResponse {
              val response = client.get("threads/${this.threadId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}/steps") {
                if (limit != null) {
                  this.url.encodedParameters.append("limit", limit.toString())
                }
                if (order != null) {
                  this.url.encodedParameters.append("order", order.value.encodeURLParameter())
                }
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (before != null) {
                  this.url.encodedParameters.append("before", before.encodeURLParameter())
                }
                include?.forEach { this.url.encodedParameters.append("include%5B%5D", it.value.encodeURLParameter()) }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Order(
              public val `value`: String,
            ) {
              @SerialName("asc")
              Asc("asc"),
              @SerialName("desc")
              Desc("desc"),
              ;
            }

            @Serializable
            public enum class Include(
              public val `value`: String,
            ) {
              @SerialName("step_details.tool_calls[*].file_search.results[*].content")
              StepDetailsToolCallsFileSearchResultsContent("step_details.tool_calls[*].file_search.results[*].content"),
              ;
            }
          }

          public class StepIdPath internal constructor(
            private val client: HttpClient,
            private val threadId: String,
            private val runId: String,
            private val stepId: String,
          ) {
            public val `get`: Get = Get(client, threadId, runId, stepId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val threadId: String,
              private val runId: String,
              private val stepId: String,
            ) {
              public suspend operator fun invoke(include: List<Include>? = null): RunStepObject {
                val response = client.get("threads/${this.threadId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}/steps/${this.stepId.encodeURLParameter()}") {
                  include?.forEach { this.url.encodedParameters.append("include%5B%5D", it.value.encodeURLParameter()) }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }

              @Serializable
              public enum class Include(
                public val `value`: String,
              ) {
                @SerialName("step_details.tool_calls[*].file_search.results[*].content")
                StepDetailsToolCallsFileSearchResultsContent("step_details.tool_calls[*].file_search.results[*].content"),
                ;
              }
            }
          }
        }

        public class SubmitToolOutputs internal constructor(
          private val client: HttpClient,
          private val threadId: String,
          private val runId: String,
        ) {
          public val post: Post = Post(client, threadId, runId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val threadId: String,
            private val runId: String,
          ) {
            public suspend operator fun invoke(body: SubmitToolOutputsRunRequest): RunObject {
              val response = client.post("threads/${this.threadId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}/submit_tool_outputs") {
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
  }
}
