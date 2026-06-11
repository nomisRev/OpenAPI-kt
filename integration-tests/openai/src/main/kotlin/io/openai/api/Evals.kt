package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
import kotlin.String
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
    ): EvalList = client.get("/evals") {
      after?.let { parameter("after", it) }
      limit?.let { parameter("limit", it) }
      order?.let { parameter("order", it.value) }
      orderBy?.let { parameter("order_by", it.value) }
    }.body()

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
    public suspend operator fun invoke(body: CreateEvalRequest): Eval = client.post("/evals") {
      contentType(ContentType.Application.Json)
      setBody(body)
    }.body()
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
        val response = client.delete("/evals/$evalId")
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
      public suspend operator fun invoke(): Eval = client.get("/evals/$evalId").body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val evalId: String,
    ) {
      public suspend operator fun invoke(name: String? = null, metadata: Metadata? = null): Eval = client.post("/evals/$evalId") {
        contentType(ContentType.Application.Json)
        setBody(Body(name = name, metadata = metadata))
      }.body()

      @Serializable
      internal data class Body(
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
        ): EvalRunList = client.get("/evals/$evalId/runs") {
          after?.let { parameter("after", it) }
          limit?.let { parameter("limit", it) }
          order?.let { parameter("order", it.value) }
          status?.let { parameter("status", it.value) }
        }.body()

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
          val response = client.post("/evals/$evalId/runs") {
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
            val response = client.delete("/evals/$evalId/runs/$runId")
            return when (response.status.value) {
              200 -> response.body<Response.Ok>()
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            @Serializable
            public data class Ok(
              public val `object`: String? = null,
              public val deleted: Boolean? = null,
              @SerialName("run_id")
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
          public suspend operator fun invoke(): EvalRun = client.get("/evals/$evalId/runs/$runId").body()
        }

        public class Post internal constructor(
          private val client: HttpClient,
          private val evalId: String,
          private val runId: String,
        ) {
          public suspend operator fun invoke(): EvalRun = client.post("/evals/$evalId/runs/$runId").body()
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
            ): EvalRunOutputItemList = client.get("/evals/$evalId/runs/$runId/output_items") {
              after?.let { parameter("after", it) }
              limit?.let { parameter("limit", it) }
              status?.let { parameter("status", it.value) }
              order?.let { parameter("order", it.value) }
            }.body()

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
              public suspend operator fun invoke(): EvalRunOutputItem = client.get("/evals/$evalId/runs/$runId/output_items/$outputItemId").body()
            }
          }
        }
      }
    }
  }
}
