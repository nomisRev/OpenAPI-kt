package io.github.nomisrev.render.test.client.operations.`inline`.markdown.body

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Markdown internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      text: String,
      mode: Mode? = null,
      context: String? = null,
    ): String = client.post("/markdown") {
      contentType(ContentType.Application.Json)
      setBody(Body(text = text, mode = mode, context = context))
    }.body()

    @Serializable
    public enum class Mode(
      public val `value`: String,
    ) {
      @SerialName("markdown")
      Markdown("markdown"),
      @SerialName("gfm")
      Gfm("gfm"),
      ;
    }

    @Serializable
    internal data class Body(
      public val text: String,
      public val mode: Mode? = null,
      public val context: String? = null,
    )
  }
}
