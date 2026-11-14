package io.github.api

import io.github.model.BasicError
import io.github.model.GlobalAdvisory
import io.github.model.SecurityAdvisoryEcosystems
import io.github.model.ValidationErrorSimple
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

public class Advisories internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun ghsaId(ghsaId: String): GhsaIdPath = GhsaIdPath(client, ghsaId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      ghsaId: String? = null,
      type: Type? = Type.Reviewed,
      cveId: String? = null,
      ecosystem: SecurityAdvisoryEcosystems? = null,
      severity: Severity? = null,
      cwes: Cwes? = null,
      isWithdrawn: Boolean? = null,
      affects: Affects? = null,
      published: String? = null,
      updated: String? = null,
      modified: String? = null,
      epssPercentage: String? = null,
      epssPercentile: String? = null,
      before: String? = null,
      after: String? = null,
      direction: Direction? = Direction.Desc,
      perPage: Long? = 30L,
      sort: Sort? = Sort.Published,
    ): Response {
      val response = client.get("advisories") {
        if (ghsaId != null) {
          this.url.encodedParameters.append("ghsa_id", ghsaId.encodeURLParameter())
        }
        if (type != null) {
          this.url.encodedParameters.append("type", type.value.encodeURLParameter())
        }
        if (cveId != null) {
          this.url.encodedParameters.append("cve_id", cveId.encodeURLParameter())
        }
        if (ecosystem != null) {
          this.url.encodedParameters.append("ecosystem", ecosystem.value.encodeURLParameter())
        }
        if (severity != null) {
          this.url.encodedParameters.append("severity", severity.value.encodeURLParameter())
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
        if (isWithdrawn != null) {
          this.url.encodedParameters.append("is_withdrawn", isWithdrawn.toString())
        }
        if (affects != null) {
          when (affects) {
            is Affects.CaseString -> {
              this.url.encodedParameters.append("affects", affects.value.encodeURLParameter())
            }
            is Affects.CaseStrings -> {
              affects.value.forEach { this.url.encodedParameters.append("affects", it.encodeURLParameter()) }
            }
          }
        }
        if (published != null) {
          this.url.encodedParameters.append("published", published.encodeURLParameter())
        }
        if (updated != null) {
          this.url.encodedParameters.append("updated", updated.encodeURLParameter())
        }
        if (modified != null) {
          this.url.encodedParameters.append("modified", modified.encodeURLParameter())
        }
        if (epssPercentage != null) {
          this.url.encodedParameters.append("epss_percentage", epssPercentage.encodeURLParameter())
        }
        if (epssPercentile != null) {
          this.url.encodedParameters.append("epss_percentile", epssPercentile.encodeURLParameter())
        }
        if (before != null) {
          this.url.encodedParameters.append("before", before.encodeURLParameter())
        }
        if (after != null) {
          this.url.encodedParameters.append("after", after.encodeURLParameter())
        }
        if (direction != null) {
          this.url.encodedParameters.append("direction", direction.value.encodeURLParameter())
        }
        if (perPage != null) {
          this.url.encodedParameters.append("per_page", perPage.toString())
        }
        if (sort != null) {
          this.url.encodedParameters.append("sort", sort.value.encodeURLParameter())
        }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        422 -> Response.UnprocessableEntity(response.body())
        429 -> Response.TooManyRequests(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("reviewed")
      Reviewed("reviewed"),
      @SerialName("malware")
      Malware("malware"),
      @SerialName("unreviewed")
      Unreviewed("unreviewed"),
      ;
    }

    @Serializable
    public enum class Severity(
      public val `value`: String,
    ) {
      @SerialName("unknown")
      Unknown("unknown"),
      @SerialName("low")
      Low("low"),
      @SerialName("medium")
      Medium("medium"),
      @SerialName("high")
      High("high"),
      @SerialName("critical")
      Critical("critical"),
      ;
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
            buildSerialDescriptor("io.github.api.Advisories.Get.Cwes", PolymorphicKind.SEALED) {
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

    @Serializable(with = Affects.Serializer::class)
    public sealed interface Affects {
      @Serializable
      @JvmInline
      public value class CaseString(
        public val `value`: String,
      ) : Affects

      @Serializable
      @JvmInline
      public value class CaseStrings(
        public val `value`: List<String>,
      ) : Affects

      public object Serializer : KSerializer<Affects> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.api.Advisories.Get.Affects", PolymorphicKind.SEALED) {
          element("CaseString", String.serializer().descriptor)
          element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        }

        override fun deserialize(decoder: Decoder): Affects {
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

        override fun serialize(encoder: Encoder, `value`: Affects) {
          when(value) {
            is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          }
        }
      }
    }

    @Serializable
    public enum class Direction(
      public val `value`: String,
    ) {
      @SerialName("asc")
      Asc("asc"),
      @SerialName("desc")
      Desc("desc"),
      ;
    }

    @Serializable
    public enum class Sort(
      public val `value`: String,
    ) {
      @SerialName("updated")
      Updated("updated"),
      @SerialName("published")
      Published("published"),
      @SerialName("epss_percentage")
      EpssPercentage("epss_percentage"),
      @SerialName("epss_percentile")
      EpssPercentile("epss_percentile"),
      ;
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<GlobalAdvisory>,
      ) : Response

      public data class UnprocessableEntity(
        public val `value`: ValidationErrorSimple,
      ) : Response

      public data class TooManyRequests(
        public val `value`: BasicError,
      ) : Response
    }
  }

  public class GhsaIdPath internal constructor(
    private val client: HttpClient,
    private val ghsaId: String,
  ) {
    public val `get`: Get = Get(client, ghsaId)

    public class Get internal constructor(
      private val client: HttpClient,
      private val ghsaId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("advisories/${this.ghsaId.encodeURLParameter()}")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: GlobalAdvisory,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }
}
