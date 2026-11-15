package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class News internal constructor(
  private val client: HttpClient,
) {
  public val home: Home = Home(client)

  public val world: World = World(client)

  public class Home internal constructor(
    private val client: HttpClient,
  ) {
    public val json: Json = Json(client)

    public val jsonp: Jsonp = Jsonp(client)

    public class Json internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("news/home.json")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class Jsonp internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("news/home.jsonp")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }

  public class World internal constructor(
    private val client: HttpClient,
  ) {
    public val json: Json = Json(client)

    public val jsonp: Jsonp = Jsonp(client)

    public class Json internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("news/world.json")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class Jsonp internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("news/world.jsonp")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
