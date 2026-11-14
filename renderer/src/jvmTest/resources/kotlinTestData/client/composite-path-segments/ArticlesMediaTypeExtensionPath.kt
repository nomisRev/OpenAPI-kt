package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class ArticlesMediaTypeExtensionPath internal constructor(
  private val client: HttpClient,
  private val mediaTypeExtension: MediaTypeExtension,
) {
  public val `get`: Get = Get(client, mediaTypeExtension)

  @Serializable
  public enum class MediaTypeExtension(
    public val `value`: String,
  ) {
    @SerialName("json")
    Json("json"),
    @SerialName("xml")
    Xml("xml"),
    ;
  }

  public class Get internal constructor(
    private val client: HttpClient,
    private val mediaTypeExtension: MediaTypeExtension,
  ) {
    public suspend operator fun invoke() {
      val response = client.get("articles.${this.mediaTypeExtension.value.encodeURLParameter()}")
      if (response.status.value != 204) throw ResponseException(response, "")
    }
  }
}
