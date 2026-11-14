package io.github.nomisrev.render.test.client.operations.`inline`.oneof.parameter

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
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

public class Advisories internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      ghsaId: String? = null,
      cveId: String? = null,
      cwes: Cwes? = null,
    ) {
      val response = client.get("advisories") {
        if (ghsaId != null) {
          this.url.encodedParameters.append("ghsa_id", ghsaId.encodeURLParameter())
        }
        if (cveId != null) {
          this.url.encodedParameters.append("cve_id", cveId.encodeURLParameter())
        }
        if (cwes != null) {
          when (cwes) {
            is Cwes.CaseString -> {
              this.url.encodedParameters.append("cwes", cwes.value.encodeURLParameter())
            }
            is Cwes.CaseStrings -> {
              cwes.value.forEach { this.url.encodedParameters.append("cwes", it.encodeURLParameter()) }
            }
          }
        }
      }
      if (response.status.value != 200) throw ResponseException(response, "")
    }

    @Serializable(with = Cwes.Serializer::class)
    public sealed interface Cwes {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Cwes

      @Serializable
      @JvmInline
      public value class CaseStrings(
        public val `value`: List<String>,
      ) : Cwes

      public object Serializer : KSerializer<Cwes> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.render.test.client.operations.inline.oneof.parameter.Advisories.Get.Cwes", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Cwes {
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

        override fun serialize(encoder: Encoder, `value`: Cwes) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          }
        }
      }
    }
  }
}
