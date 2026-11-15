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
import io.openai.model.Optional
import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Evals internal constructor(
  private val __client: HttpClient,
) {
  public val `get`: Get = Get(__client)

  public val post: Post = Post(__client)

  public fun evalId(evalId: String): EvalIdPath = EvalIdPath(__client, evalId)

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(
      after: String? = null,
      limit: Long? = 20L,
      order: Order? = Order.Asc,
      orderBy: OrderBy? = OrderBy.CreatedAt,
    ): EvalList {
      val response = __client.get("evals") {
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
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(body: CreateEvalRequest): Eval {
      val response = __client.post("evals") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
      if (response.status.value != 201) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class EvalIdPath internal constructor(
    private val __client: HttpClient,
    private val _evalId: String,
  ) {
    public val delete: Delete = Delete(__client, _evalId)

    public val `get`: Get = Get(__client, _evalId)

    public val post: Post = Post(__client, _evalId)

    public val runs: Runs = Runs(__client, _evalId)

    public class Delete internal constructor(
      private val __client: HttpClient,
      private val _evalId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = __client.delete("evals/${this._evalId.encodeURLParameter()}")
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
      private val __client: HttpClient,
      private val _evalId: String,
    ) {
      public suspend operator fun invoke(): Eval {
        val response = __client.get("evals/${this._evalId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
      private val _evalId: String,
    ) {
      public suspend operator fun invoke(name: String? = null, metadata: Optional<Metadata> = Optional.Absent): Eval {
        val response = __client.post("evals/${this._evalId.encodeURLParameter()}") {
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
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val metadata: Optional<Metadata> = Optional.Absent,
      )
    }

    public class Runs internal constructor(
      private val __client: HttpClient,
      private val _evalId: String,
    ) {
      public val `get`: Get = Get(__client, _evalId)

      public val post: Post = Post(__client, _evalId)

      public fun runId(runId: String): RunIdPath = RunIdPath(__client, _evalId, runId)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _evalId: String,
      ) {
        public suspend operator fun invoke(
          after: String? = null,
          limit: Long? = 20L,
          order: Order? = Order.Asc,
          status: Status? = null,
        ): EvalRunList {
          val response = __client.get("evals/${this._evalId.encodeURLParameter()}/runs") {
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
        private val __client: HttpClient,
        private val _evalId: String,
      ) {
        public suspend operator fun invoke(body: CreateEvalRunRequest): Response {
          val response = __client.post("evals/${this._evalId.encodeURLParameter()}/runs") {
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
        private val __client: HttpClient,
        private val _evalId: String,
        private val _runId: String,
      ) {
        public val delete: Delete = Delete(__client, _evalId, _runId)

        public val `get`: Get = Get(__client, _evalId, _runId)

        public val post: Post = Post(__client, _evalId, _runId)

        public val outputItems: OutputItems = OutputItems(__client, _evalId, _runId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _evalId: String,
          private val _runId: String,
        ) {
          public suspend operator fun invoke(): Response {
            val response = __client.delete("evals/${this._evalId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}")
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
          private val __client: HttpClient,
          private val _evalId: String,
          private val _runId: String,
        ) {
          public suspend operator fun invoke(): EvalRun {
            val response = __client.get("evals/${this._evalId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _evalId: String,
          private val _runId: String,
        ) {
          public suspend operator fun invoke(): EvalRun {
            val response = __client.post("evals/${this._evalId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class OutputItems internal constructor(
          private val __client: HttpClient,
          private val _evalId: String,
          private val _runId: String,
        ) {
          public val `get`: Get = Get(__client, _evalId, _runId)

          public fun outputItemId(outputItemId: String): OutputItemIdPath = OutputItemIdPath(__client, _evalId, _runId, outputItemId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _evalId: String,
            private val _runId: String,
          ) {
            public suspend operator fun invoke(
              after: String? = null,
              limit: Long? = 20L,
              status: Status? = null,
              order: Order? = Order.Asc,
            ): EvalRunOutputItemList {
              val response = __client.get("evals/${this._evalId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}/output_items") {
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
            private val __client: HttpClient,
            private val _evalId: String,
            private val _runId: String,
            private val _outputItemId: String,
          ) {
            public val `get`: Get = Get(__client, _evalId, _runId, _outputItemId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _evalId: String,
              private val _runId: String,
              private val _outputItemId: String,
            ) {
              public suspend operator fun invoke(): EvalRunOutputItem {
                val response = __client.get("evals/${this._evalId.encodeURLParameter()}/runs/${this._runId.encodeURLParameter()}/output_items/${this._outputItemId.encodeURLParameter()}")
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
