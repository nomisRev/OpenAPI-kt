package io.github.nomisrev.render.test.client.`inline`.oneof.components.parameter

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import io.ktor.client.request.parameter
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public class Alerts internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(has: Has? = null) {
      client.get("/alerts") {
        has?.let { parameter("has", it) }
      }
    }

    @Serializable(with = Has.Serializer::class)
    public sealed interface Has {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Has

      @Serializable
      @JvmInline
      public value class CasePatchList(
        public val `value`: List<Patch>,
      ) : Has

      @Serializable
      public enum class Patch(
        public val `value`: String,
      ) {
        @SerialName("patch")
        Patch("patch"),
        ;
      }

      public object Serializer : KSerializer<Has> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.nomisrev.render.test.client.inline.oneof.components.parameter.Alerts.Get.Has", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CasePatchList", ListSerializer(Patch.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Has {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return json.attemptDeserialize(
            value,
            CasePatchList::class to { CasePatchList(decodeFromJsonElement(ListSerializer(Patch.serializer()), it)) },
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
        }

        override fun serialize(encoder: Encoder, `value`: Has) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CasePatchList -> encoder.encodeSerializableValue(ListSerializer(Patch.serializer()), value.value)
          }
        }
      }
    }
  }
}
