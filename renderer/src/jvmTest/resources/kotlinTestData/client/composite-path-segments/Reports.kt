package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Reports internal constructor(
  private val __client: HttpClient,
) {
  public fun period(period: Period): PeriodPath {
    val encoded = when (period) {
          Period.Daily -> "daily"
          Period.Weekly -> "weekly"
        }
    return PeriodPath(__client, encoded)
  }

  public fun period(period: Int): PeriodPath = PeriodPath(__client, period.toString())

  @Serializable
  public enum class Period(
    public val `value`: String,
  ) {
    @SerialName("daily")
    Daily("daily"),
    @SerialName("weekly")
    Weekly("weekly"),
    ;
  }

  public class PeriodPath internal constructor(
    private val __client: HttpClient,
    private val _period: String,
  ) {
    public val csv: Csv = Csv(__client, _period)

    public class Csv internal constructor(
      private val __client: HttpClient,
      private val _period: String,
    ) {
      public val `get`: Get = Get(__client, _period)

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _period: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.get("reports/${this._period.encodeURLParameter()}.csv")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
