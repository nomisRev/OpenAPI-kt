package io.github.nomisrev.render.test.client.`enum`.int.default

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public class Meter internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      rate: Rate? = Rate.`300`,
      limit: Long? = 25L,
      ratio: Double? = 1.5,
      on: Boolean? = true,
    ) {
      val response = client.get("meter") {
        if (rate != null) {
          this.url.encodedParameters.append("rate", rate.value.toString().encodeURLParameter())
        }
        if (limit != null) {
          this.url.encodedParameters.append("limit", limit.toString())
        }
        if (ratio != null) {
          this.url.encodedParameters.append("ratio", ratio.toString())
        }
        if (on != null) {
          this.url.encodedParameters.append("on", on.toString())
        }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @Serializable(with = Rate.Serializer::class)
    public enum class Rate(
      public val `value`: Long,
    ) {
      `300`(300L),
      `600`(600L),
      `1200`(1_200L),
      ;

      internal object Serializer : KSerializer<Rate> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Rate", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, `value`: Rate) {
          encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): Rate {
          val value = decoder.decodeLong()
          val member = Rate.entries.firstOrNull { it.value == value }
          return member ?: throw SerializationException("""Rate has no member with value $value""")
        }
      }
    }
  }
}
