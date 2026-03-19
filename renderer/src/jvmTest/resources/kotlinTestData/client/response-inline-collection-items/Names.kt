package io.github.nomisrev.render.test.client.response.`inline`.collection.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public interface Names {
  public val `get`: Get

  public interface Get {
    public suspend operator fun invoke(): Response

    public data class Response(
      public val `value`: List<Name>,
    ) {
      @JvmInline
      @Serializable
      public value class Name(
        public val name: String,
      )
    }
  }
}

internal class KtorNames(
  private val client: HttpClient,
) : Names {
  override val `get`: Names.Get = object : Names.Get {
    override suspend operator fun invoke(): Names.Get.Response {
      val value: List<Names.Get.Response.Name> = client.get("/names").body()
      return Names.Get.Response(value)
    }
  }
}
