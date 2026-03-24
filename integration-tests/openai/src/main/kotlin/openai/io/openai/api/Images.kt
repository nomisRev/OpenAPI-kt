package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`header`
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.openai.model.CreateImageEditRequest
import io.openai.model.CreateImageRequest
import io.openai.model.CreateImageVariationRequest
import io.openai.model.EditImageBodyJsonParam
import io.openai.model.ImageEditStreamEvent
import io.openai.model.ImageGenStreamEvent
import io.openai.model.ImagesResponse
import io.openai.model.InputFidelity
import io.openai.model.PartialImages
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Long
import kotlin.String
import kotlinx.serialization.json.Json

public class Images internal constructor(
  private val client: HttpClient,
) {
  public val edits: Edits = Edits(client)

  public val generations: Generations = Generations(client)

  public val variations: Variations = Variations(client)

  public class Edits internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend fun json(
        image: CreateImageEditRequest.Image,
        prompt: String,
        mask: ByteArray? = null,
        background: CreateImageEditRequest.Background? = null,
        model: CreateImageEditRequest.Model? = null,
        n: Long? = null,
        size: CreateImageEditRequest.Size? = null,
        responseFormat: CreateImageEditRequest.ResponseFormat? = null,
        outputFormat: CreateImageEditRequest.OutputFormat? = null,
        outputCompression: Long? = null,
        user: String? = null,
        inputFidelity: InputFidelity? = null,
        stream: Boolean? = null,
        partialImages: PartialImages? = null,
        quality: CreateImageEditRequest.Quality? = null,
      ): ImagesResponse = client.post("/images/edits") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        setBody(MultiPartFormDataContent(formData {
          append("image", Json.encodeToString(image))
          append("prompt", prompt)
          if (mask != null) {
            append("mask", mask)
          }
          if (background != null) {
            append("background", background.value)
          }
          if (model != null) {
            append("model", Json.encodeToString(model))
          }
          if (n != null) {
            append("n", n)
          }
          if (size != null) {
            append("size", size.value)
          }
          if (responseFormat != null) {
            append("response_format", responseFormat.value)
          }
          if (outputFormat != null) {
            append("output_format", outputFormat.value)
          }
          if (outputCompression != null) {
            append("output_compression", outputCompression)
          }
          if (user != null) {
            append("user", user)
          }
          if (inputFidelity != null) {
            append("input_fidelity", inputFidelity.value)
          }
          if (stream != null) {
            append("stream", stream)
          }
          if (partialImages != null) {
            append("partial_images", Json.encodeToString(partialImages))
          }
          if (quality != null) {
            append("quality", quality.value)
          }
        }))
      }.body()

      public suspend fun json(body: EditImageBodyJsonParam): ImagesResponse = client.post("/images/edits") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public suspend fun textEventStream(
        image: CreateImageEditRequest.Image,
        prompt: String,
        mask: ByteArray? = null,
        background: CreateImageEditRequest.Background? = null,
        model: CreateImageEditRequest.Model? = null,
        n: Long? = null,
        size: CreateImageEditRequest.Size? = null,
        responseFormat: CreateImageEditRequest.ResponseFormat? = null,
        outputFormat: CreateImageEditRequest.OutputFormat? = null,
        outputCompression: Long? = null,
        user: String? = null,
        inputFidelity: InputFidelity? = null,
        stream: Boolean? = null,
        partialImages: PartialImages? = null,
        quality: CreateImageEditRequest.Quality? = null,
      ): ImagesResponse = client.post("/images/edits") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        setBody(MultiPartFormDataContent(formData {
          append("image", Json.encodeToString(image))
          append("prompt", prompt)
          if (mask != null) {
            append("mask", mask)
          }
          if (background != null) {
            append("background", background.value)
          }
          if (model != null) {
            append("model", Json.encodeToString(model))
          }
          if (n != null) {
            append("n", n)
          }
          if (size != null) {
            append("size", size.value)
          }
          if (responseFormat != null) {
            append("response_format", responseFormat.value)
          }
          if (outputFormat != null) {
            append("output_format", outputFormat.value)
          }
          if (outputCompression != null) {
            append("output_compression", outputCompression)
          }
          if (user != null) {
            append("user", user)
          }
          if (inputFidelity != null) {
            append("input_fidelity", inputFidelity.value)
          }
          if (stream != null) {
            append("stream", stream)
          }
          if (partialImages != null) {
            append("partial_images", Json.encodeToString(partialImages))
          }
          if (quality != null) {
            append("quality", quality.value)
          }
        }))
      }.body()

      public suspend fun textEventStream(body: EditImageBodyJsonParam): ImagesResponse = client.post("/images/edits") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public sealed interface JsonResponse {
        public data class Ok(
          public val `value`: ImagesResponse,
        ) : JsonResponse
      }

      public sealed interface TextEventStreamResponse {
        public data class Ok(
          public val `value`: ImageEditStreamEvent,
        ) : TextEventStreamResponse
      }
    }
  }

  public class Generations internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend fun json(body: CreateImageRequest): ImagesResponse = client.post("/images/generations") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public suspend fun textEventStream(body: CreateImageRequest): ImagesResponse = client.post("/images/generations") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public sealed interface JsonResponse {
        public data class Ok(
          public val `value`: ImagesResponse,
        ) : JsonResponse
      }

      public sealed interface TextEventStreamResponse {
        public data class Ok(
          public val `value`: ImageGenStreamEvent,
        ) : TextEventStreamResponse
      }
    }
  }

  public class Variations internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        image: ByteArray,
        model: CreateImageVariationRequest.Model? = null,
        n: Long? = null,
        responseFormat: CreateImageVariationRequest.ResponseFormat? = null,
        size: CreateImageVariationRequest.Size? = null,
        user: String? = null,
      ): ImagesResponse = client.post("/images/variations") {
        setBody(MultiPartFormDataContent(formData {
          append("image", image)
          if (model != null) {
            append("model", Json.encodeToString(model))
          }
          if (n != null) {
            append("n", n)
          }
          if (responseFormat != null) {
            append("response_format", responseFormat.value)
          }
          if (size != null) {
            append("size", size.toString())
          }
          if (user != null) {
            append("user", user)
          }
        }))
      }.body()
    }
  }
}
