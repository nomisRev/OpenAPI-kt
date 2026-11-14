package io.github.nomisrev.render.test.client.parameter.styles

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.cookie
import io.ktor.http.encodeURLParameter
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class Items internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      ids: List<Long>,
      tags: List<String>? = null,
      words: List<String>? = null,
      pipes: List<String>? = null,
      q: String? = null,
      `when`: Instant? = null,
      status: Status? = null,
      statuses: List<Status>? = null,
      code: String? = null,
      codes: List<Code>? = null,
      filter: Filter? = null,
      range: Range? = null,
      color: Color? = null,
      cwe: Cwe? = null,
      metadata: Metadata? = null,
      labels: Map<String, String>? = null,
      attrs: Map<String, Int>? = null,
      xIds: List<Int>? = null,
      xStatus: Status? = null,
      xMeta: Map<String, String>? = null,
      xPoint: XPoint? = null,
      session: String? = null,
      prefs: List<String>? = null,
      flags: List<Status>? = null,
    ) {
      val response = client.get("items") {
        ids.forEach { this.url.encodedParameters.append("ids", it.toString()) }
        if (!tags.isNullOrEmpty()) {
          this.url.encodedParameters.append("tags", tags.joinToString(",") { it.encodeURLParameter() })
        }
        if (!words.isNullOrEmpty()) {
          this.url.encodedParameters.append("words", words.joinToString("%20") { it.encodeURLParameter() })
        }
        if (!pipes.isNullOrEmpty()) {
          this.url.encodedParameters.append("pipes", pipes.joinToString("%7C") { it.encodeURLParameter() })
        }
        if (q != null) {
          this.url.encodedParameters.append("q", q.encodeURLParameter())
        }
        if (`when` != null) {
          this.url.encodedParameters.append("when", `when`.toString().encodeURLParameter())
        }
        if (status != null) {
          this.url.encodedParameters.append("status", status.value.encodeURLParameter())
        }
        if (!statuses.isNullOrEmpty()) {
          this.url.encodedParameters.append("statuses", statuses.joinToString(",") { it.value.encodeURLParameter() })
        }
        if (code != null) {
          this.url.encodedParameters.append("code", code.encodeURLParameter())
        }
        codes?.forEach { this.url.encodedParameters.append("codes", it.value.encodeURLParameter()) }
        if (filter != null) {
          this.url.encodedParameters.append("filter%5Bstatus%5D", filter.status.value.encodeURLParameter())
          if (filter.limit != null) {
            this.url.encodedParameters.append("filter%5Blimit%5D", filter.limit.toString())
          }
        }
        if (range != null) {
          this.url.encodedParameters.append("min", range.min.toString())
          if (range.max != null) {
            this.url.encodedParameters.append("max", range.max.toString())
          }
        }
        if (color != null) {
          this.url.encodedParameters.append("color", "R,${color.r},G,${color.g}")
        }
        if (cwe != null) {
          when (cwe) {
            is Cwe.CaseString -> {
              this.url.encodedParameters.append("cwe", cwe.value.encodeURLParameter())
            }
            is Cwe.CaseStrings -> {
              cwe.value.forEach { this.url.encodedParameters.append("cwe", it.encodeURLParameter()) }
            }
          }
        }
        if (metadata != null) {
          metadata.values.forEach { this.url.encodedParameters.append(it.key.encodeURLParameter(), it.value.encodeURLParameter()) }
        }
        labels?.forEach { this.url.encodedParameters.append("labels%5B${it.key.encodeURLParameter()}%5D", it.value.encodeURLParameter()) }
        if (!attrs.isNullOrEmpty()) {
          this.url.encodedParameters.append("attrs", attrs.entries.joinToString(",") { "${it.key.encodeURLParameter()},${it.value}" })
        }
        if (!xIds.isNullOrEmpty()) {
          `header`("X-Ids", xIds.joinToString(","))
        }
        if (xStatus != null) {
          `header`("X-Status", xStatus.value)
        }
        if (!xMeta.isNullOrEmpty()) {
          `header`("X-Meta", xMeta.entries.joinToString(",") { "${it.key},${it.value}" })
        }
        if (xPoint != null) {
          `header`("X-Point", "x=${xPoint.x},y=${xPoint.y}")
        }
        if (session != null) {
          cookie("session", session)
        }
        if (!prefs.isNullOrEmpty()) {
          cookie("prefs", prefs.joinToString(","))
        }
        flags?.forEach { cookie("flags", it.value) }
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Filter(
      public val status: Status,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val limit: Int? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Range(
      public val min: Int,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val max: Int? = null,
    )

    @Serializable
    public data class Color(
      @SerialName("R")
      public val r: Int,
      @SerialName("G")
      public val g: Int,
    )

    @Serializable(with = Cwe.Serializer::class)
    public sealed interface Cwe {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Cwe

      @Serializable
      @JvmInline
      public value class CaseStrings(
        public val `value`: List<String>,
      ) : Cwe

      public object Serializer : KSerializer<Cwe> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.render.test.client.parameter.styles.Items.Get.Cwe", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Cwe {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return when {
            value is JsonPrimitive && value.isString -> json.attemptDeserialize(
              value,
              CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
            value is JsonArray -> json.attemptDeserialize(
              value,
              CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
            )
            else -> throw UnionSerializationException(value, emptyMap())
          }
        }

        override fun serialize(encoder: Encoder, `value`: Cwe) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          }
        }
      }
    }

    @Serializable
    public data class XPoint(
      public val x: Int,
      public val y: Int,
    )
  }
}
