package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.Long

public class Pages internal constructor(
  private val client: HttpClient,
) {
  public fun number(number: Int): NumberIntPath = NumberIntPath(client, number)

  public fun number(number: Long): NumberLongPath = NumberLongPath(client, number)

  public class NumberIntPath internal constructor(
    private val client: HttpClient,
    private val number: Int,
  ) {
    public val `get`: Get = Get(client, number)

    public class Get internal constructor(
      private val client: HttpClient,
      private val number: Int,
    ) {
      public suspend operator fun invoke() {
        val response = client.get("pages/${this.number}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class NumberLongPath internal constructor(
    private val client: HttpClient,
    private val number: Long,
  ) {
    public val lines: Lines = Lines(client, number)

    public class Lines internal constructor(
      private val client: HttpClient,
      private val number: Long,
    ) {
      public val `get`: Get = Get(client, number)

      public class Get internal constructor(
        private val client: HttpClient,
        private val number: Long,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("pages/${this.number}/lines")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
