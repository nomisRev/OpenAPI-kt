package io.github.nomisrev.render.test.client.`enum`.typed.parameters

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.formUrlEncode
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
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

public class Meters internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public val _300: `300` = `300`(client)

  public val _600: `600` = `600`(client)

  public val _1200: `1200` = `1200`(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(rate: Baudrate) {
      val response = client.post("meters") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("rate", rate.value.toString())
        }.formUrlEncode())
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }
  }

  public class `300` internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        speed: Baudrate? = null,
        scale: Scale? = null,
        on: On? = null,
        xLevel: XLevel? = null,
      ) {
        val response = client.get("meters/300") {
          if (speed != null) {
            url.encodedParameters.append("speed", speed.value.toString().encodeURLParameter())
          }
          if (scale != null) {
            url.encodedParameters.append("scale", scale.value.toString().encodeURLParameter())
          }
          if (on != null) {
            url.encodedParameters.append("on", on.value.toString().encodeURLParameter())
          }
          if (xLevel != null) {
            `header`("X-Level", xLevel.value.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
      }

      @Serializable(with = Scale.Serializer::class)
      public enum class Scale(
        public val `value`: Double,
      ) {
        `05`(0.5),
        `15`(1.5),
        ;

        internal object Serializer : KSerializer<Scale> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("Scale", PrimitiveKind.DOUBLE)

          override fun serialize(encoder: Encoder, `value`: Scale) {
            encoder.encodeDouble(value.value)
          }

          override fun deserialize(decoder: Decoder): Scale {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.doubleOrNull ?: throw SerializationException("""Scale expects a JSON number, got $element""")
            } else {
              decoder.decodeDouble()
            }
            val member = Scale.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""Scale has no member with value $value""")
          }
        }
      }

      @Serializable(with = On.Serializer::class)
      public enum class On(
        public val `value`: Boolean,
      ) {
        True(true),
        False(false),
        ;

        internal object Serializer : KSerializer<On> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("On", PrimitiveKind.BOOLEAN)

          override fun serialize(encoder: Encoder, `value`: On) {
            encoder.encodeBoolean(value.value)
          }

          override fun deserialize(decoder: Decoder): On {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.booleanOrNull ?: throw SerializationException("""On expects a JSON boolean, got $element""")
            } else {
              decoder.decodeBoolean()
            }
            val member = On.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""On has no member with value $value""")
          }
        }
      }

      @Serializable(with = XLevel.Serializer::class)
      public enum class XLevel(
        public val `value`: Long,
      ) {
        `1`(1L),
        `5000000000`(5_000_000_000L),
        ;

        internal object Serializer : KSerializer<XLevel> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("XLevel", PrimitiveKind.LONG)

          override fun serialize(encoder: Encoder, `value`: XLevel) {
            encoder.encodeLong(value.value)
          }

          override fun deserialize(decoder: Decoder): XLevel {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.longOrNull ?: throw SerializationException("""XLevel expects a JSON integer, got $element""")
            } else {
              decoder.decodeLong()
            }
            val member = XLevel.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""XLevel has no member with value $value""")
          }
        }
      }
    }
  }

  public class `600` internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        speed: Baudrate? = null,
        scale: Scale? = null,
        on: On? = null,
        xLevel: XLevel? = null,
      ) {
        val response = client.get("meters/600") {
          if (speed != null) {
            url.encodedParameters.append("speed", speed.value.toString().encodeURLParameter())
          }
          if (scale != null) {
            url.encodedParameters.append("scale", scale.value.toString().encodeURLParameter())
          }
          if (on != null) {
            url.encodedParameters.append("on", on.value.toString().encodeURLParameter())
          }
          if (xLevel != null) {
            `header`("X-Level", xLevel.value.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
      }

      @Serializable(with = Scale.Serializer::class)
      public enum class Scale(
        public val `value`: Double,
      ) {
        `05`(0.5),
        `15`(1.5),
        ;

        internal object Serializer : KSerializer<Scale> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("Scale", PrimitiveKind.DOUBLE)

          override fun serialize(encoder: Encoder, `value`: Scale) {
            encoder.encodeDouble(value.value)
          }

          override fun deserialize(decoder: Decoder): Scale {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.doubleOrNull ?: throw SerializationException("""Scale expects a JSON number, got $element""")
            } else {
              decoder.decodeDouble()
            }
            val member = Scale.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""Scale has no member with value $value""")
          }
        }
      }

      @Serializable(with = On.Serializer::class)
      public enum class On(
        public val `value`: Boolean,
      ) {
        True(true),
        False(false),
        ;

        internal object Serializer : KSerializer<On> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("On", PrimitiveKind.BOOLEAN)

          override fun serialize(encoder: Encoder, `value`: On) {
            encoder.encodeBoolean(value.value)
          }

          override fun deserialize(decoder: Decoder): On {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.booleanOrNull ?: throw SerializationException("""On expects a JSON boolean, got $element""")
            } else {
              decoder.decodeBoolean()
            }
            val member = On.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""On has no member with value $value""")
          }
        }
      }

      @Serializable(with = XLevel.Serializer::class)
      public enum class XLevel(
        public val `value`: Long,
      ) {
        `1`(1L),
        `5000000000`(5_000_000_000L),
        ;

        internal object Serializer : KSerializer<XLevel> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("XLevel", PrimitiveKind.LONG)

          override fun serialize(encoder: Encoder, `value`: XLevel) {
            encoder.encodeLong(value.value)
          }

          override fun deserialize(decoder: Decoder): XLevel {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.longOrNull ?: throw SerializationException("""XLevel expects a JSON integer, got $element""")
            } else {
              decoder.decodeLong()
            }
            val member = XLevel.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""XLevel has no member with value $value""")
          }
        }
      }
    }
  }

  public class `1200` internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        speed: Baudrate? = null,
        scale: Scale? = null,
        on: On? = null,
        xLevel: XLevel? = null,
      ) {
        val response = client.get("meters/1200") {
          if (speed != null) {
            url.encodedParameters.append("speed", speed.value.toString().encodeURLParameter())
          }
          if (scale != null) {
            url.encodedParameters.append("scale", scale.value.toString().encodeURLParameter())
          }
          if (on != null) {
            url.encodedParameters.append("on", on.value.toString().encodeURLParameter())
          }
          if (xLevel != null) {
            `header`("X-Level", xLevel.value.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
      }

      @Serializable(with = Scale.Serializer::class)
      public enum class Scale(
        public val `value`: Double,
      ) {
        `05`(0.5),
        `15`(1.5),
        ;

        internal object Serializer : KSerializer<Scale> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("Scale", PrimitiveKind.DOUBLE)

          override fun serialize(encoder: Encoder, `value`: Scale) {
            encoder.encodeDouble(value.value)
          }

          override fun deserialize(decoder: Decoder): Scale {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.doubleOrNull ?: throw SerializationException("""Scale expects a JSON number, got $element""")
            } else {
              decoder.decodeDouble()
            }
            val member = Scale.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""Scale has no member with value $value""")
          }
        }
      }

      @Serializable(with = On.Serializer::class)
      public enum class On(
        public val `value`: Boolean,
      ) {
        True(true),
        False(false),
        ;

        internal object Serializer : KSerializer<On> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("On", PrimitiveKind.BOOLEAN)

          override fun serialize(encoder: Encoder, `value`: On) {
            encoder.encodeBoolean(value.value)
          }

          override fun deserialize(decoder: Decoder): On {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.booleanOrNull ?: throw SerializationException("""On expects a JSON boolean, got $element""")
            } else {
              decoder.decodeBoolean()
            }
            val member = On.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""On has no member with value $value""")
          }
        }
      }

      @Serializable(with = XLevel.Serializer::class)
      public enum class XLevel(
        public val `value`: Long,
      ) {
        `1`(1L),
        `5000000000`(5_000_000_000L),
        ;

        internal object Serializer : KSerializer<XLevel> {
          override val descriptor: SerialDescriptor =
              PrimitiveSerialDescriptor("XLevel", PrimitiveKind.LONG)

          override fun serialize(encoder: Encoder, `value`: XLevel) {
            encoder.encodeLong(value.value)
          }

          override fun deserialize(decoder: Decoder): XLevel {
            val value = if (decoder is JsonDecoder) {
              val element = decoder.decodeJsonElement()
              val primitive = (element as? JsonPrimitive)?.takeIf { !it.isString }
              primitive?.longOrNull ?: throw SerializationException("""XLevel expects a JSON integer, got $element""")
            } else {
              decoder.decodeLong()
            }
            val member = XLevel.entries.firstOrNull { it.value == value }
            return member ?: throw SerializationException("""XLevel has no member with value $value""")
          }
        }
      }
    }
  }
}
