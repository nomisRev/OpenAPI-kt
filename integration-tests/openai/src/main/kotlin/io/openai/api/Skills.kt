package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.formUrlEncode
import io.ktor.serialization.JsonConvertException
import io.openai.model.CreateSkillBody
import io.openai.model.CreateSkillVersionBody
import io.openai.model.DeletedSkillResource
import io.openai.model.DeletedSkillVersionResource
import io.openai.model.OrderEnum
import io.openai.model.SetDefaultSkillVersionBody
import io.openai.model.SkillListResource
import io.openai.model.SkillResource
import io.openai.model.SkillVersionListResource
import io.openai.model.SkillVersionResource
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

public class Skills internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun skillId(skillId: String): SkillIdPath = SkillIdPath(client, skillId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      limit: Long? = null,
      order: OrderEnum? = null,
      after: String? = null,
    ): SkillListResource {
      val response = client.get("skills") {
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          this.url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (after != null) {
          this.url.encodedParameters.append("after", after.encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): SkillResource {
      val response = client.post("skills")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend operator fun invoke(files: CreateSkillBody.Files): SkillResource {
      val response = client.post("skills") {
        setBody(MultiPartFormDataContent(formData {
          append("files", Json.encodeToString(files))
        }))
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend operator fun invoke(body: CreateSkillBody? = null): SkillResource {
      val response = client.post("skills") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class SkillIdPath internal constructor(
    private val client: HttpClient,
    private val skillId: String,
  ) {
    public val delete: Delete = Delete(client, skillId)

    public val `get`: Get = Get(client, skillId)

    public val post: Post = Post(client, skillId)

    public val content: Content = Content(client, skillId)

    public val versions: Versions = Versions(client, skillId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public suspend operator fun invoke(): DeletedSkillResource {
        val response = client.delete("skills/${skillId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public suspend operator fun invoke(): SkillResource {
        val response = client.get("skills/${skillId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public suspend operator fun invoke(): SkillResource {
        val response = client.post("skills/${skillId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      public suspend operator fun invoke(body: SetDefaultSkillVersionBody? = null): SkillResource {
        val response = client.post("skills/${skillId.encodeURLParameter()}") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }

      public suspend operator fun invoke(defaultVersion: String): SkillResource {
        val response = client.post("skills/${skillId.encodeURLParameter()}") {
          contentType(ContentType.Application.FormUrlEncoded)
          setBody(Parameters.build {
            append("default_version", defaultVersion)
          }.formUrlEncode())
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Content internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public val `get`: Get = Get(client, skillId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val skillId: String,
      ) {
        public suspend fun zip(): ByteArray {
          val response = client.get("skills/${skillId.encodeURLParameter()}/content") {
            `header`(HttpHeaders.Accept, ContentType.Application.Zip)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend fun json(): String {
          val response = client.get("skills/${skillId.encodeURLParameter()}/content") {
            `header`(HttpHeaders.Accept, ContentType.Application.Json)
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
        }
      }
    }

    public class Versions internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public val `get`: Get = Get(client, skillId)

      public val post: Post = Post(client, skillId)

      public fun version(version: String): VersionPath = VersionPath(client, skillId, version)

      public class Get internal constructor(
        private val client: HttpClient,
        private val skillId: String,
      ) {
        public suspend operator fun invoke(
          limit: Long? = null,
          order: OrderEnum? = null,
          after: String? = null,
        ): SkillVersionListResource {
          val response = client.get("skills/${skillId.encodeURLParameter()}/versions") {
            if (limit != null) {
              this.url.encodedParameters.append("limit", limit.toString())
            }
            if (order != null) {
              this.url.encodedParameters.append("order", order.value.encodeURLParameter())
            }
            if (after != null) {
              this.url.encodedParameters.append("after", after.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val skillId: String,
      ) {
        public suspend operator fun invoke(): SkillVersionResource {
          val response = client.post("skills/${skillId.encodeURLParameter()}/versions")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(files: CreateSkillVersionBody.Files, default: Boolean? = null): SkillVersionResource {
          val response = client.post("skills/${skillId.encodeURLParameter()}/versions") {
            setBody(MultiPartFormDataContent(formData {
              append("files", Json.encodeToString(files))
              if (default != null) {
                append("default", default)
              }
            }))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(body: CreateSkillVersionBody? = null): SkillVersionResource {
          val response = client.post("skills/${skillId.encodeURLParameter()}/versions") {
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class VersionPath internal constructor(
        private val client: HttpClient,
        private val skillId: String,
        private val version: String,
      ) {
        public val delete: Delete = Delete(client, skillId, version)

        public val `get`: Get = Get(client, skillId, version)

        public val content: Content = Content(client, skillId, version)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val skillId: String,
          private val version: String,
        ) {
          public suspend operator fun invoke(): DeletedSkillVersionResource {
            val response = client.delete("skills/${skillId.encodeURLParameter()}/versions/${version.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val skillId: String,
          private val version: String,
        ) {
          public suspend operator fun invoke(): SkillVersionResource {
            val response = client.get("skills/${skillId.encodeURLParameter()}/versions/${version.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Content internal constructor(
          private val client: HttpClient,
          private val skillId: String,
          private val version: String,
        ) {
          public val `get`: Get = Get(client, skillId, version)

          public class Get internal constructor(
            private val client: HttpClient,
            private val skillId: String,
            private val version: String,
          ) {
            public suspend fun zip(): ByteArray {
              val response = client.get("skills/${skillId.encodeURLParameter()}/versions/${version.encodeURLParameter()}/content") {
                `header`(HttpHeaders.Accept, ContentType.Application.Zip)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }

            public suspend fun json(): String {
              val response = client.get("skills/${skillId.encodeURLParameter()}/versions/${version.encodeURLParameter()}/content") {
                `header`(HttpHeaders.Accept, ContentType.Application.Json)
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
            }
          }
        }
      }
    }
  }
}
