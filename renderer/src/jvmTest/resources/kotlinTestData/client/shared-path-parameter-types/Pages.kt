package io.github.nomisrev.render.test.client.shared.path.parameter.types

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int
import kotlin.Long

public class Pages internal constructor(
  private val __client: HttpClient,
) {
  public fun number(number: Int): NumberIntPath = NumberIntPath(__client, number)

  public fun number(number: Long): NumberLongPath = NumberLongPath(__client, number)

  public class NumberIntPath internal constructor(
    private val __client: HttpClient,
    private val _number: Int,
  ) {
    public val `get`: Get = Get(__client, _number)

    public class Get internal constructor(
      private val __client: HttpClient,
      private val _number: Int,
    ) {
      public suspend operator fun invoke() {
        val response = __client.get("pages/${this._number}")
        if (response.status.value != 204) throw ResponseException(response, "")
      }
    }
  }

  public class NumberLongPath internal constructor(
    private val __client: HttpClient,
    private val _number: Long,
  ) {
    public val lines: Lines = Lines(__client, _number)

    public class Lines internal constructor(
      private val __client: HttpClient,
      private val _number: Long,
    ) {
      public val `get`: Get = Get(__client, _number)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _number: Long,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("pages/${this._number}/lines")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
