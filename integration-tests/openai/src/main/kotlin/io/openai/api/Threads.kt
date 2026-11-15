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
  private val __client: HttpClient,
) {
  public val post: Post = Post(__client)

  public val runs: Runs = Runs(__client)

  public fun threadId(threadId: String): ThreadIdPath = ThreadIdPath(__client, threadId)

  public class Post internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateThreadRequest? = null): ThreadObject {
      val response = __client.post("threads") {
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
    private val __client: HttpClient,
  ) {
    public val post: Post = Post(__client)

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(body: CreateThreadAndRunRequest): RunObject {
        val response = __client.post("threads/runs") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class ThreadIdPath internal constructor(
    private val __client: HttpClient,
    private val _threadId: String,
  ) {
    public val delete: Delete = Delete(__client, _threadId)

    public val `get`: Get = Get(__client, _threadId)

    public val post: Post = Post(__client, _threadId)

    public val messages: Messages = Messages(__client, _threadId)

    public val runs: Runs = Runs(__client, _threadId)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _threadId: String,
    ) {
      public suspend operator fun invoke(): DeleteThreadResponse {
        val response = __client.delete("threads/${this._threadId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _threadId: String,
    ) {
      public suspend operator fun invoke(): ThreadObject {
        val response = __client.get("threads/${this._threadId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
      private val _threadId: String,
    ) {
      public suspend operator fun invoke(body: ModifyThreadRequest): ThreadObject {
        val response = __client.post("threads/${this._threadId.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Messages internal constructor(
      private val __client: HttpClient,
      private val _threadId: String,
    ) {
      public val `get`: Get = Get(__client, _threadId)

      public val post: Post = Post(__client, _threadId)

      public fun messageId(messageId: String): MessageIdPath = MessageIdPath(__client, _threadId, messageId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
          before: String? = null,
          runId: String? = null,
        ): ListMessagesResponse {
          val response = __client.get("threads/${this._threadId.encodeURLParameter()}/messages") {
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
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public suspend operator fun invoke(body: CreateMessageRequest): MessageObject {
          val response = __client.post("threads/${this._threadId.encodeURLParameter()}/messages") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class MessageIdPath internal constructor(
        private val __client: HttpClient,
        private val _threadId: String,
        private val _messageId: String,
      ) {
        public val delete: Delete = Delete(__client, _threadId, _messageId)

        public val `get`: Get = Get(__client, _threadId, _messageId)

        public val post: Post = Post(__client, _threadId, _messageId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _messageId: String,
        ) {
          public suspend operator fun invoke(): DeleteMessageResponse {
            val response = __client.delete("threads/${this._threadId.encodeURLParameter()}/messages/${this._messageId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _messageId: String,
        ) {
          public suspend operator fun invoke(): MessageObject {
            val response = __client.get("threads/${this._threadId.encodeURLParameter()}/messages/${this._messageId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _messageId: String,
        ) {
          public suspend operator fun invoke(body: ModifyMessageRequest): MessageObject {
            val response = __client.post("threads/${this._threadId.encodeURLParameter()}/messages/${this._messageId.encodeURLParameter()}") {
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
      private val __client: HttpClient,
      private val _threadId: String,
    ) {
      public val `get`: Get = Get(__client, _threadId)

      public val post: Post = Post(__client, _threadId)

      public fun runId(runId: String): RunIdPath = RunIdPath(__client, _threadId, runId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = 20L,
          order: Order? = Order.Desc,
          after: String? = null,
          before: String? = null,
        ): ListRunsResponse {
          val response = __client.get("threads/${this._threadId.encodeURLParameter()}/runs") {
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
        private val __client: HttpClient,
        private val _threadId: String,
      ) {
        public suspend operator fun invoke(body: CreateRunRequest, include: List<Include>? = null): RunObject {
          val response = __client.post("threads/${this._threadId.encodeURLParameter()}/runs") {
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
        private val __client: HttpClient,
        private val _threadId: String,
        private val _runId: String,
      ) {
        public val `get`: Get = Get(__client, _threadId, _runId)

        public val post: Post = Post(__client, _threadId, _runId)

        public val cancel: Cancel = Cancel(__client, _threadId, _runId)

        public val steps: Steps = Steps(__client, _threadId, _runId)

        public val submitToolOutputs: SubmitToolOutputs =
            SubmitToolOutputs(__client, _threadId, _runId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _runId: String,
        ) {
          public suspend operator fun invoke(): RunObject {
            val response = __client.get("threads/${this._threadId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _runId: String,
        ) {
          public suspend operator fun invoke(body: ModifyRunRequest): RunObject {
            val response = __client.post("threads/${this._threadId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}") {
              contentType(ContentType.Application.Json)
              setBody(body)
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Cancel internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _runId: String,
        ) {
          public val post: Post = Post(__client, _threadId, _runId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _threadId: String,
            private val _runId: String,
          ) {
            public suspend operator fun invoke(): RunObject {
              val response = __client.post("threads/${this._threadId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}/cancel")
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class Steps internal constructor(
          private val __client: HttpClient,
          private val _threadId: String,
          private val _runId: String,
        ) {
          public val `get`: Get = Get(__client, _threadId, _runId)

          public fun stepId(stepId: String): StepIdPath = StepIdPath(__client, _threadId, _runId, stepId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _threadId: String,
            private val _runId: String,
          ) {
            public suspend operator fun invoke(
              limit: Long? = 20L,
              order: Order? = Order.Desc,
              after: String? = null,
              before: String? = null,
              include: List<Include>? = null,
            ): ListRunStepsResponse {
              val response = __client.get("threads/${this._threadId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}/steps") {
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
            private val __client: HttpClient,
            private val _threadId: String,
            private val _runId: String,
            private val _stepId: String,
          ) {
            public val `get`: Get = Get(__client, _threadId, _runId, _stepId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _threadId: String,
              private val _runId: String,
              private val _stepId: String,
            ) {
              public suspend operator fun invoke(include: List<Include>? = null): RunStepObject {
                val response = __client.get("threads/${this._threadId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}/steps/${this._stepId.encodeURLParameter()}") {
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
          private val __client: HttpClient,
          private val _threadId: String,
          private val _runId: String,
        ) {
          public val post: Post = Post(__client, _threadId, _runId)

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _threadId: String,
            private val _runId: String,
          ) {
            public suspend operator fun invoke(body: SubmitToolOutputsRunRequest): RunObject {
              val response = __client.post("threads/${this._threadId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}/submit_tool_outputs") {
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
