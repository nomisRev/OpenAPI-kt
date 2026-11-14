package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import kotlin.Int

public class Ranges internal constructor(
  private val client: HttpClient,
) {
  public fun lo(lo: Int): LoPath = LoPath(client, lo)

  public class LoPath internal constructor(
    private val client: HttpClient,
    private val lo: Int,
  ) {
    public fun hi(hi: Int): HiPath = HiPath(client, lo, hi)

    public class HiPath internal constructor(
      private val client: HttpClient,
      private val lo: Int,
      private val hi: Int,
    ) {
      public val `get`: Get = Get(client, lo, hi)

      public class Get internal constructor(
        private val client: HttpClient,
        private val lo: Int,
        private val hi: Int,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("ranges/${this.lo}-${this.hi}")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
