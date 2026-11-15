package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int

public class Ranges internal constructor(
  private val __client: HttpClient,
) {
  public fun lo(lo: Int): LoPath = LoPath(__client, lo)

  public class LoPath internal constructor(
    private val __client: HttpClient,
    private val _lo: Int,
  ) {
    public fun hi(hi: Int): HiPath = HiPath(__client, _lo, hi)

    public class HiPath internal constructor(
      private val __client: HttpClient,
      private val _lo: Int,
      private val _hi: Int,
    ) {
      public val `get`: Get = Get(__client, _lo, _hi)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _lo: Int,
        private val _hi: Int,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("ranges/${this._lo}-${this._hi}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
