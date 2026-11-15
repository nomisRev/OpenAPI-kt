package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class News internal constructor(
  private val __client: HttpClient,
) {
  public val home: Home = Home(__client)

  public val world: World = World(__client)

  public class Home internal constructor(
    private val __client: HttpClient,
  ) {
    public val json: Json = Json(__client)

    public val jsonp: Jsonp = Jsonp(__client)

    public class Json internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("news/home.json")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class Jsonp internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("news/home.jsonp")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }

  public class World internal constructor(
    private val __client: HttpClient,
  ) {
    public val json: Json = Json(__client)

    public val jsonp: Jsonp = Jsonp(__client)

    public class Json internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("news/world.json")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }

    public class Jsonp internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("news/world.jsonp")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
