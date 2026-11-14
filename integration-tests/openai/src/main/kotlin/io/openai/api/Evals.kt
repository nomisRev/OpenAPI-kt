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
import io.openai.model.CreateEvalRequest
import io.openai.model.CreateEvalRunRequest
import io.openai.model.Error
import io.openai.model.Eval
import io.openai.model.EvalList
import io.openai.model.EvalRun
import io.openai.model.EvalRunList
import io.openai.model.EvalRunOutputItem
import io.openai.model.EvalRunOutputItemList
import io.openai.model.Metadata
import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Evals internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun evalId(evalId: String): EvalIdPath = EvalIdPath(client, evalId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      after: String? = null,
      limit: Long? = 20L,
      order: Order? = Order.Asc,
      orderBy: OrderBy? = OrderBy.CreatedAt,
    ): EvalList {
      val response = client.get("evals") {
        if (after != null) {
          this.url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          this.url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (orderBy != null) {
          this.url.encodedParameters.append("order_by", orderBy.value.encodeURLParameter())
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

    @Serializable
    public enum class OrderBy(
      public val `value`: String,
    ) {
      @SerialName("created_at")
      CreatedAt("created_at"),
      @SerialName("updated_at")
      UpdatedAt("updated_at"),
      ;
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateEvalRequest): Eval {
      val response = client.post("evals") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 201) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class EvalIdPath internal constructor(
    private val client: HttpClient,
    private val evalId: String,
  ) {
    public val delete: Delete = Delete(client, evalId)

    public val `get`: Get = Get(client, evalId)

    public val post: Post = Post(client, evalId)

    public val runs: Runs = Runs(client, evalId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val evalId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.delete("evals/${this.evalId.encodeURLParameter()}")
        return when (response.status.value) {
          200 -> response.body<Response.Ok>()
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        @Serializable
        public data class Ok(
          public val `object`: String,
          public val deleted: Boolean,
          @SerialName("eval_id")
          public val evalId: String,
        ) : Response

        public data class NotFound(
          public val `value`: Error,
        ) : Response
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val evalId: String,
    ) {
      public suspend operator fun invoke(): Eval {
        val response = client.get("evals/${this.evalId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val evalId: String,
    ) {
      public suspend operator fun invoke(name: String? = null, metadata: Metadata? = null): Eval {
        val response = client.post("evals/${this.evalId.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(Body(name = name, metadata = metadata))
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val name: String? = null,
        public val metadata: Metadata? = null,
      )
    }

    public class Runs internal constructor(
      private val client: HttpClient,
      private val evalId: String,
    ) {
      public val `get`: Get = Get(client, evalId)

      public val post: Post = Post(client, evalId)

      public fun runId(runId: String): RunIdPath = RunIdPath(client, evalId, runId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val evalId: String,
      ) {
        public suspend operator fun invoke(
          after: String? = null,
          limit: Long? = 20L,
          order: Order? = Order.Asc,
          status: Status? = null,
        ): EvalRunList {
          val response = client.get("evals/${this.evalId.encodeURLParameter()}/runs") {
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (status != null) {
              this.url.encodedParameters.append("status", status.value.encodeURLParameter())
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

        @Serializable
        public enum class Status(
          public val `value`: String,
        ) {
          @SerialName("queued")
          Queued("queued"),
          @SerialName("in_progress")
          InProgress("in_progress"),
          @SerialName("completed")
          Completed("completed"),
          @SerialName("canceled")
          Canceled("canceled"),
          @SerialName("failed")
          Failed("failed"),
          ;
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val evalId: String,
      ) {
        public suspend operator fun invoke(body: CreateEvalRunRequest): Response {
          val response = client.post("evals/${this.evalId.encodeURLParameter()}/runs") {
            contentType(ContentType.Application.Json)
            setBody(body)
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            400 -> Response.BadRequest(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Created(
            public val `value`: EvalRun,
          ) : Response

          public data class BadRequest(
            public val `value`: Error,
          ) : Response
        }
      }

      public class RunIdPath internal constructor(
        private val client: HttpClient,
        private val evalId: String,
        private val runId: String,
      ) {
        public val delete: Delete = Delete(client, evalId, runId)

        public val `get`: Get = Get(client, evalId, runId)

        public val post: Post = Post(client, evalId, runId)

        public val outputItems: OutputItems = OutputItems(client, evalId, runId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val evalId: String,
          private val runId: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("evals/${this.evalId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}")
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Ok(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val `object`: String? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val deleted: Boolean? = null,
              @SerialName("run_id")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val runId: String? = null,
            ) : Response

            public data class NotFound(
              public val `value`: Error,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val evalId: String,
          private val runId: String,
        ) {
          public suspend operator fun invoke(): EvalRun {
            val response = client.get("evals/${this.evalId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val evalId: String,
          private val runId: String,
        ) {
          public suspend operator fun invoke(): EvalRun {
            val response = client.post("evals/${this.evalId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class OutputItems internal constructor(
          private val client: HttpClient,
          private val evalId: String,
          private val runId: String,
        ) {
          public val `get`: Get = Get(client, evalId, runId)

          public fun outputItemId(outputItemId: String): OutputItemIdPath = OutputItemIdPath(client, evalId, runId, outputItemId)

          public class Get internal constructor(
            private val client: HttpClient,
            private val evalId: String,
            private val runId: String,
          ) {
            public suspend operator fun invoke(
              after: String? = null,
              limit: Long? = 20L,
              status: Status? = null,
              order: Order? = Order.Asc,
            ): EvalRunOutputItemList {
              val response = client.get("evals/${this.evalId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}/output_items") {
                if (after != null) {
                  this.url.encodedParameters.append("after", after.encodeURLParameter())
                }
                if (limit != null) {
                  this.url.encodedParameters.append("limit", limit.toString())
                }
                if (status != null) {
                  this.url.encodedParameters.append("status", status.value.encodeURLParameter())
                }
                if (order != null) {
                  this.url.encodedParameters.append("order", order.value.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            @Serializable
            public enum class Status(
              public val `value`: String,
            ) {
              @SerialName("fail")
              Fail("fail"),
              @SerialName("pass")
              Pass("pass"),
              ;
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

          public class OutputItemIdPath internal constructor(
            private val client: HttpClient,
            private val evalId: String,
            private val runId: String,
            private val outputItemId: String,
          ) {
            public val `get`: Get = Get(client, evalId, runId, outputItemId)

            public class Get internal constructor(
              private val client: HttpClient,
              private val evalId: String,
              private val runId: String,
              private val outputItemId: String,
            ) {
              public suspend operator fun invoke(): EvalRunOutputItem {
                val response = client.get("evals/${this.evalId.encodeURLParameter()}/runs/${this.runId.encodeURLParameter()}/output_items/${this.outputItemId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }
    }
  }
}
