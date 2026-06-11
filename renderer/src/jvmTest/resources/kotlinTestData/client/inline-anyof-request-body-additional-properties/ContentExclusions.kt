package io.github.nomisrev.render.test.client.`inline`.anyof.request.body.additional.properties

import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
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
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

public class ContentExclusions internal constructor(
  private val client: HttpClient,
) {
  public val put: Put = Put(client)

  public class Put internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(body: Body) {
      client.put("/content-exclusions") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @KeepGeneratedSerializer
    @Serializable(with = Body.Serializer::class)
    public data class Body(
      public val additional: Map<String, List<AdditionalProperties>>? = null,
    ) {
      @Serializable(with = AdditionalProperties.Serializer::class)
      public sealed interface AdditionalProperties {
        @Serializable
        @JvmInline
        public value class CaseString(
          public val `value`: String,
        ) : AdditionalProperties

        @JvmInline
        @Serializable
        public value class IfAnyMatchStrings(
          public val ifAnyMatch: List<String>,
        ) : AdditionalProperties

        @JvmInline
        @Serializable
        public value class IfNoneMatchStrings(
          public val ifNoneMatch: List<String>,
        ) : AdditionalProperties

        public object Serializer : KSerializer<AdditionalProperties> {
          @OptIn(
            InternalSerializationApi::class,
            ExperimentalSerializationApi::class,
          )
          override val descriptor: SerialDescriptor =
              buildSerialDescriptor("io.github.nomisrev.render.test.client.inline.anyof.request.body.additional.properties.ContentExclusions.Put.Body.AdditionalProperties", PolymorphicKind.SEALED) {
            element("CaseString", String.serializer().descriptor)
            element("IfAnyMatchStrings", IfAnyMatchStrings.serializer().descriptor)
            element("IfNoneMatchStrings", IfNoneMatchStrings.serializer().descriptor)
          }

          override fun deserialize(decoder: Decoder): AdditionalProperties {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
              value,
              IfAnyMatchStrings::class to { decodeFromJsonElement(IfAnyMatchStrings.serializer(), it) },
              IfNoneMatchStrings::class to { decodeFromJsonElement(IfNoneMatchStrings.serializer(), it) },
              CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
            )
          }

          override fun serialize(encoder: Encoder, `value`: AdditionalProperties) {
            when(value) {
              is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
              is IfAnyMatchStrings -> encoder.encodeSerializableValue(IfAnyMatchStrings.serializer(), value)
              is IfNoneMatchStrings -> encoder.encodeSerializableValue(IfNoneMatchStrings.serializer(), value)
            }
          }
        }
      }

      public object Serializer : KSerializer<Body> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, `value`: Body) {
          val json = (encoder as JsonEncoder).json
          val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
          val content = mutableMapOf<String, JsonElement>()
          known.forEach {
            if (it.key != "additional") {
              content[it.key] = it.value
            }
          }
          value.additional?.forEach {
            content[it.key] = json.encodeToJsonElement(ListSerializer(AdditionalProperties.serializer()), it.value)
          }
          encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
        }

        override fun deserialize(decoder: Decoder): Body {
          val json = (decoder as JsonDecoder).json
          val element = decoder.decodeSerializableValue(JsonObject.serializer())
          val knownNames = emptySet<String>()
          val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
          val additional = (element - knownNames)
            .mapValues { json.decodeFromJsonElement(ListSerializer(AdditionalProperties.serializer()), it.value) }
            .ifEmpty { null }
          return known.copy(additional = additional)
        }
      }
    }
  }
}
