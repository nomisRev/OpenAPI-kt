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
import io.ktor.http.contentType
import io.openai.model.CreateVideoBody
import io.openai.model.CreateVideoRemixBody
import io.openai.model.DeletedVideoResource
import io.openai.model.ImageRefParam2
import io.openai.model.OrderEnum
import io.openai.model.VideoContentVariant
import io.openai.model.VideoListResource
import io.openai.model.VideoModel
import io.openai.model.VideoResource
import io.openai.model.VideoSeconds
import io.openai.model.VideoSize
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlinx.serialization.json.Json

public class Videos internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public fun videoId(videoId: String): VideoIdPath = VideoIdPath(client, videoId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      limit: Long? = null,
      order: OrderEnum? = null,
      after: String? = null,
    ): VideoListResource = client.get("/videos") {
      limit?.let { parameter("limit", it) }
      order?.let { parameter("order", it.value) }
      after?.let { parameter("after", it) }
    }.body()
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      model: VideoModel? = null,
      prompt: String,
      inputReference: ByteArray? = null,
      imageReference: ImageRefParam2? = null,
      seconds: VideoSeconds? = null,
      size: VideoSize? = null,
    ): VideoResource = client.post("/videos") {
      setBody(MultiPartFormDataContent(formData {
        if (model != null) {
          append("model", Json.encodeToString(model))
        }
        append("prompt", prompt)
        if (inputReference != null) {
          append("input_reference", inputReference)
        }
        if (imageReference != null) {
          append("image_reference", Json.encodeToString(imageReference))
        }
        if (seconds != null) {
          append("seconds", Json.encodeToString(seconds))
        }
        if (size != null) {
          append("size", Json.encodeToString(size))
        }
      }))
    }.body()

    public suspend operator fun invoke(body: CreateVideoBody? = null): VideoResource = client.post("/videos") {
      body?.let {
        contentType(ContentType.Application.Json)
        setBody(it)
      }
    }.body()
  }

  public class VideoIdPath internal constructor(
    private val client: HttpClient,
    private val videoId: String,
  ) {
    public val delete: Delete = Delete(client, videoId)

    public val `get`: Get = Get(client, videoId)

    public val content: Content = Content(client, videoId)

    public val remix: Remix = Remix(client, videoId)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val videoId: String,
    ) {
      public suspend operator fun invoke(): DeletedVideoResource = client.delete("/videos/$videoId").body()
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val videoId: String,
    ) {
      public suspend operator fun invoke(): VideoResource = client.get("/videos/$videoId").body()
    }

    public class Content internal constructor(
      private val client: HttpClient,
      private val videoId: String,
    ) {
      public val `get`: Get = Get(client, videoId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val videoId: String,
      ) {
        public suspend fun videoMp4(variant: VideoContentVariant? = null): ByteArray = client.get("/videos/$videoId/content") {
          `header`(HttpHeaders.Accept, ContentType("video", "mp4"))
          variant?.let { parameter("variant", it.value) }
        }.body()

        public suspend fun imageWebp(variant: VideoContentVariant? = null): ByteArray = client.get("/videos/$videoId/content") {
          `header`(HttpHeaders.Accept, ContentType("image", "webp"))
          variant?.let { parameter("variant", it.value) }
        }.body()

        public suspend fun json(variant: VideoContentVariant? = null): String = client.get("/videos/$videoId/content") {
          `header`(HttpHeaders.Accept, ContentType.Application.Json)
          variant?.let { parameter("variant", it.value) }
        }.body()
      }
    }

    public class Remix internal constructor(
      private val client: HttpClient,
      private val videoId: String,
    ) {
      public val post: Post = Post(client, videoId)

      public class Post internal constructor(
        private val client: HttpClient,
        private val videoId: String,
      ) {
        public suspend operator fun invoke(prompt: String): VideoResource = client.post("/videos/$videoId/remix") {
          setBody(MultiPartFormDataContent(formData {
            append("prompt", prompt)
          }))
        }.body()

        public suspend operator fun invoke(body: CreateVideoRemixBody? = null): VideoResource = client.post("/videos/$videoId/remix") {
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }.body()
      }
    }
  }
}
