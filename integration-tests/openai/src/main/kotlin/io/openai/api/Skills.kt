package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
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
    ): SkillListResource = client.get("/skills") {
      limit?.let { parameter("limit", it) }
      order?.let { parameter("order", it.value) }
      after?.let { parameter("after", it) }
    }.body()
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(files: CreateSkillBody.Files): SkillResource = client.post("/skills") {
      setBody(MultiPartFormDataContent(formData {
        append("files", Json.encodeToString(files))
      }))
    }.body()

    public suspend operator fun invoke(body: CreateSkillBody? = null): SkillResource = client.post("/skills") {
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }.body()
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
      public suspend operator fun invoke(): DeletedSkillResource = client.delete("/skills/$skillId").body()
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public suspend operator fun invoke(): SkillResource = client.get("/skills/$skillId").body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
      private val skillId: String,
    ) {
      public suspend operator fun invoke(body: SetDefaultSkillVersionBody? = null): SkillResource = client.post("/skills/$skillId") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }.body()

      public suspend operator fun invoke(defaultVersion: String): SkillResource = client.post("/skills/$skillId") {
        setBody(Parameters.build {
          append("default_version", defaultVersion)
        }.formUrlEncode())
      }.body()
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
        public suspend fun zip(): ByteArray = client.get("/skills/$skillId/content") {
          `header`(HttpHeaders.Accept, ContentType.Application.Zip)
        }.body()

        public suspend fun json(): String = client.get("/skills/$skillId/content") {
          `header`(HttpHeaders.Accept, ContentType.Application.Json)
        }.body()
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
        ): SkillVersionListResource = client.get("/skills/$skillId/versions") {
          limit?.let { parameter("limit", it) }
          order?.let { parameter("order", it.value) }
          after?.let { parameter("after", it) }
        }.body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val skillId: String,
      ) {
        public suspend operator fun invoke(files: CreateSkillVersionBody.Files, default: Boolean? = null): SkillVersionResource = client.post("/skills/$skillId/versions") {
          setBody(MultiPartFormDataContent(formData {
            append("files", Json.encodeToString(files))
            if (default != null) {
              append("default", default)
            }
          }))
        }.body()

        public suspend operator fun invoke(body: CreateSkillVersionBody? = null): SkillVersionResource = client.post("/skills/$skillId/versions") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
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
          public suspend operator fun invoke(): DeletedSkillVersionResource = client.delete("/skills/$skillId/versions/$version").body()
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val skillId: String,
          private val version: String,
        ) {
          public suspend operator fun invoke(): SkillVersionResource = client.get("/skills/$skillId/versions/$version").body()
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
            public suspend fun zip(): ByteArray = client.get("/skills/$skillId/versions/$version/content") {
              `header`(HttpHeaders.Accept, ContentType.Application.Zip)
            }.body()

            public suspend fun json(): String = client.get("/skills/$skillId/versions/$version/content") {
              `header`(HttpHeaders.Accept, ContentType.Application.Json)
            }.body()
          }
        }
      }
    }
  }
}
