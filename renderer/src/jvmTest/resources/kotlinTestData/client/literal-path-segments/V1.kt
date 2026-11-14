package io.github.nomisrev.render.test.client.literal.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.post

public class V1 internal constructor(
  private val client: HttpClient,
) {
  public val entities: Entities = Entities(client)

  public val items: Items = Items(client)

  public class Entities internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val search: Search = Search(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("v1/entities")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }

    public class Search internal constructor(
      private val client: HttpClient,
    ) {
      public val post: Post = Post(client)

      public class Post internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.post("v1/entities:search")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }

  public class Items internal constructor(
    private val client: HttpClient,
  ) {
    public val search: Search = Search(client)

    public val colonSearch: ColonSearch = ColonSearch(client)

    public class Search internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("v1/items/search")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class ColonSearch internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("v1/items:search")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
