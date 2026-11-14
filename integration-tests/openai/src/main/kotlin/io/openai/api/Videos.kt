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
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.serialization.JsonConvertException
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
import kotlinx.serialization.json.JsonPrimitive

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
    ): VideoListResource {
      val response = client.get("videos") {
        if (limit != null) {
          url.encodedParameters.append("limit", limit.toString())
        }
        if (order != null) {
          url.encodedParameters.append("order", order.value.encodeURLParameter())
        }
        if (after != null) {
          url.encodedParameters.append("after", after.encodeURLParameter())
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): VideoResource {
      val response = client.post("videos")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend operator fun invoke(
      model: VideoModel? = null,
      prompt: String,
      inputReference: ByteArray? = null,
      imageReference: ImageRefParam2? = null,
      seconds: VideoSeconds? = null,
      size: VideoSize? = null,
    ): VideoResource {
      val response = client.post("videos") {
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
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }

    public suspend operator fun invoke(body: CreateVideoBody? = null): VideoResource {
      val response = client.post("videos") {
        body?.let {
          contentType(ContentType.Application.Json)
          setBody(it)
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
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
      public suspend operator fun invoke(): DeletedVideoResource {
        val response = client.delete("videos/${videoId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val videoId: String,
    ) {
      public suspend operator fun invoke(): VideoResource {
        val response = client.get("videos/${videoId.encodeURLParameter()}")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
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
        public suspend fun videoMp4(variant: VideoContentVariant? = null): ByteArray {
          val response = client.get("videos/${videoId.encodeURLParameter()}/content") {
            `header`(HttpHeaders.Accept, ContentType("video", "mp4"))
            if (variant != null) {
              url.encodedParameters.append("variant", variant.value.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend fun imageWebp(variant: VideoContentVariant? = null): ByteArray {
          val response = client.get("videos/${videoId.encodeURLParameter()}/content") {
            `header`(HttpHeaders.Accept, ContentType("image", "webp"))
            if (variant != null) {
              url.encodedParameters.append("variant", variant.value.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend fun json(variant: VideoContentVariant? = null): String {
          val response = client.get("videos/${videoId.encodeURLParameter()}/content") {
            `header`(HttpHeaders.Accept, ContentType.Application.Json)
            if (variant != null) {
              url.encodedParameters.append("variant", variant.value.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body<JsonPrimitive>().let { if (it.isString) it.content else throw JsonConvertException("Illegal input: expected a JSON string but was $it") }
        }
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
        public suspend operator fun invoke(): VideoResource {
          val response = client.post("videos/${videoId.encodeURLParameter()}/remix")
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(prompt: String): VideoResource {
          val response = client.post("videos/${videoId.encodeURLParameter()}/remix") {
            setBody(MultiPartFormDataContent(formData {
              append("prompt", prompt)
            }))
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }

        public suspend operator fun invoke(body: CreateVideoRemixBody? = null): VideoResource {
          val response = client.post("videos/${videoId.encodeURLParameter()}/remix") {
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }
}
