package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.`header`
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.openai.model.AudioResponseFormat
import io.openai.model.CreateSpeechRequest
import io.openai.model.CreateSpeechResponseStreamEvent
import io.openai.model.CreateTranscriptionRequest
import io.openai.model.CreateTranscriptionResponseDiarizedJson
import io.openai.model.CreateTranscriptionResponseJson
import io.openai.model.CreateTranscriptionResponseStreamEvent
import io.openai.model.CreateTranscriptionResponseVerboseJson
import io.openai.model.CreateTranslationRequest
import io.openai.model.CreateTranslationResponseJson
import io.openai.model.CreateTranslationResponseVerboseJson
import io.openai.model.TranscriptionInclude
import io.openai.model.UpdateVoiceConsentRequest
import io.openai.model.VoiceConsentDeletedResource
import io.openai.model.VoiceConsentListResource
import io.openai.model.VoiceConsentResource
import io.openai.model.VoiceResource
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

public class Audio internal constructor(
  private val client: HttpClient,
) {
  public val speech: Speech = Speech(client)

  public val transcriptions: Transcriptions = Transcriptions(client)

  public val translations: Translations = Translations(client)

  public val voiceConsents: VoiceConsents = VoiceConsents(client)

  public val voices: Voices = Voices(client)

  public class Speech internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend fun octetStream(body: CreateSpeechRequest): ByteArray = client.post("/audio/speech") {
        `header`(HttpHeaders.Accept, ContentType.Application.OctetStream)
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public suspend fun textEventStream(body: CreateSpeechRequest): ByteArray = client.post("/audio/speech") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        contentType(ContentType.Application.Json)
        setBody(body)
      }.body()

      public sealed interface OctetStreamResponse {
        public data class Ok(
          public val `value`: ByteArray,
        ) : OctetStreamResponse
      }

      public sealed interface TextEventStreamResponse {
        public data class Ok(
          public val `value`: CreateSpeechResponseStreamEvent,
        ) : TextEventStreamResponse
      }
    }
  }

  public class Transcriptions internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend fun json(
        `file`: ByteArray,
        model: CreateTranscriptionRequest.Model,
        language: String? = null,
        prompt: String? = null,
        responseFormat: AudioResponseFormat? = null,
        temperature: Double? = null,
        include: List<TranscriptionInclude>? = null,
        timestampGranularities: List<CreateTranscriptionRequest.TimestampGranularities>? = null,
        stream: Boolean? = null,
        chunkingStrategy: CreateTranscriptionRequest.ChunkingStrategy? = null,
        knownSpeakerNames: List<String>? = null,
        knownSpeakerReferences: List<String>? = null,
      ): JsonResponse = client.post("/audio/transcriptions") {
        `header`(HttpHeaders.Accept, ContentType.Application.Json)
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          append("model", Json.encodeToString(model))
          if (language != null) {
            append("language", language)
          }
          if (prompt != null) {
            append("prompt", prompt)
          }
          if (responseFormat != null) {
            append("response_format", Json.encodeToString(responseFormat))
          }
          if (temperature != null) {
            append("temperature", temperature)
          }
          if (include != null) {
            append("include", Json.encodeToString(include))
          }
          if (timestampGranularities != null) {
            append("timestamp_granularities", Json.encodeToString(timestampGranularities))
          }
          if (stream != null) {
            append("stream", stream)
          }
          if (chunkingStrategy != null) {
            append("chunking_strategy", Json.encodeToString(chunkingStrategy))
          }
          if (knownSpeakerNames != null) {
            append("known_speaker_names", Json.encodeToString(knownSpeakerNames))
          }
          if (knownSpeakerReferences != null) {
            append("known_speaker_references", Json.encodeToString(knownSpeakerReferences))
          }
        }))
      }.body()

      public suspend fun textEventStream(
        `file`: ByteArray,
        model: CreateTranscriptionRequest.Model,
        language: String? = null,
        prompt: String? = null,
        responseFormat: AudioResponseFormat? = null,
        temperature: Double? = null,
        include: List<TranscriptionInclude>? = null,
        timestampGranularities: List<CreateTranscriptionRequest.TimestampGranularities>? = null,
        stream: Boolean? = null,
        chunkingStrategy: CreateTranscriptionRequest.ChunkingStrategy? = null,
        knownSpeakerNames: List<String>? = null,
        knownSpeakerReferences: List<String>? = null,
      ): TextEventStreamResponse = client.post("/audio/transcriptions") {
        `header`(HttpHeaders.Accept, ContentType("text", "event-stream"))
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          append("model", Json.encodeToString(model))
          if (language != null) {
            append("language", language)
          }
          if (prompt != null) {
            append("prompt", prompt)
          }
          if (responseFormat != null) {
            append("response_format", Json.encodeToString(responseFormat))
          }
          if (temperature != null) {
            append("temperature", temperature)
          }
          if (include != null) {
            append("include", Json.encodeToString(include))
          }
          if (timestampGranularities != null) {
            append("timestamp_granularities", Json.encodeToString(timestampGranularities))
          }
          if (stream != null) {
            append("stream", stream)
          }
          if (chunkingStrategy != null) {
            append("chunking_strategy", Json.encodeToString(chunkingStrategy))
          }
          if (knownSpeakerNames != null) {
            append("known_speaker_names", Json.encodeToString(knownSpeakerNames))
          }
          if (knownSpeakerReferences != null) {
            append("known_speaker_references", Json.encodeToString(knownSpeakerReferences))
          }
        }))
      }.body()

      public sealed interface JsonResponse {
        @Serializable(with = Ok.Serializer::class)
        public sealed interface Ok : JsonResponse {
          @Serializable
          @JvmInline
          public value class CaseCreateTranscriptionResponseJson(
            public val `value`: CreateTranscriptionResponseJson,
          ) : Ok

          @Serializable
          @JvmInline
          public value class CaseCreateTranscriptionResponseDiarizedJson(
            public val `value`: CreateTranscriptionResponseDiarizedJson,
          ) : Ok

          @Serializable
          @JvmInline
          public value class CaseCreateTranscriptionResponseVerboseJson(
            public val `value`: CreateTranscriptionResponseVerboseJson,
          ) : Ok

          public object Serializer : KSerializer<Ok> {
            @OptIn(
              InternalSerializationApi::class,
              ExperimentalSerializationApi::class,
            )
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.openai.api.Audio.Transcriptions.Post.JsonResponse.Ok", PolymorphicKind.SEALED) {
              element("CaseCreateTranscriptionResponseJson", CreateTranscriptionResponseJson.serializer().descriptor)
              element("CaseCreateTranscriptionResponseDiarizedJson", CreateTranscriptionResponseDiarizedJson.serializer().descriptor)
              element("CaseCreateTranscriptionResponseVerboseJson", CreateTranscriptionResponseVerboseJson.serializer().descriptor)
            }

            override fun deserialize(decoder: Decoder): Ok {
              val value = decoder.decodeSerializableValue(JsonElement.serializer())
              val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
              return json.attemptDeserialize(
                value,
                CaseCreateTranscriptionResponseJson::class to { CaseCreateTranscriptionResponseJson(decodeFromJsonElement(CreateTranscriptionResponseJson.serializer(), it)) },
                CaseCreateTranscriptionResponseDiarizedJson::class to { CaseCreateTranscriptionResponseDiarizedJson(decodeFromJsonElement(CreateTranscriptionResponseDiarizedJson.serializer(), it)) },
                CaseCreateTranscriptionResponseVerboseJson::class to { CaseCreateTranscriptionResponseVerboseJson(decodeFromJsonElement(CreateTranscriptionResponseVerboseJson.serializer(), it)) },
              )
            }

            override fun serialize(encoder: Encoder, `value`: Ok) {
              when(value) {
                is CaseCreateTranscriptionResponseJson -> encoder.encodeSerializableValue(CreateTranscriptionResponseJson.serializer(), value.value)
                is CaseCreateTranscriptionResponseDiarizedJson -> encoder.encodeSerializableValue(CreateTranscriptionResponseDiarizedJson.serializer(), value.value)
                is CaseCreateTranscriptionResponseVerboseJson -> encoder.encodeSerializableValue(CreateTranscriptionResponseVerboseJson.serializer(), value.value)
              }
            }
          }
        }
      }

      public sealed interface TextEventStreamResponse {
        public data class Ok(
          public val `value`: CreateTranscriptionResponseStreamEvent,
        ) : TextEventStreamResponse
      }
    }
  }

  public class Translations internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        `file`: ByteArray,
        model: CreateTranslationRequest.Model,
        prompt: String? = null,
        responseFormat: CreateTranslationRequest.ResponseFormat? = null,
        temperature: Double? = null,
      ): Response = client.post("/audio/translations") {
        setBody(MultiPartFormDataContent(formData {
          append("file", file)
          append("model", Json.encodeToString(model))
          if (prompt != null) {
            append("prompt", prompt)
          }
          if (responseFormat != null) {
            append("response_format", responseFormat.value)
          }
          if (temperature != null) {
            append("temperature", temperature)
          }
        }))
      }.body()

      @Serializable(with = Response.Serializer::class)
      public sealed interface Response {
        @Serializable
        @JvmInline
        public value class CaseCreateTranslationResponseJson(
          public val `value`: CreateTranslationResponseJson,
        ) : Response

        @Serializable
        @JvmInline
        public value class CaseCreateTranslationResponseVerboseJson(
          public val `value`: CreateTranslationResponseVerboseJson,
        ) : Response

        public object Serializer : KSerializer<Response> {
          @OptIn(
            InternalSerializationApi::class,
            ExperimentalSerializationApi::class,
          )
          override val descriptor: SerialDescriptor =
              buildSerialDescriptor("io.openai.api.Audio.Translations.Post.Response", PolymorphicKind.SEALED) {
            element("CaseCreateTranslationResponseJson", CreateTranslationResponseJson.serializer().descriptor)
            element("CaseCreateTranslationResponseVerboseJson", CreateTranslationResponseVerboseJson.serializer().descriptor)
          }

          override fun deserialize(decoder: Decoder): Response {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
              value,
              CaseCreateTranslationResponseJson::class to { CaseCreateTranslationResponseJson(decodeFromJsonElement(CreateTranslationResponseJson.serializer(), it)) },
              CaseCreateTranslationResponseVerboseJson::class to { CaseCreateTranslationResponseVerboseJson(decodeFromJsonElement(CreateTranslationResponseVerboseJson.serializer(), it)) },
            )
          }

          override fun serialize(encoder: Encoder, `value`: Response) {
            when(value) {
              is CaseCreateTranslationResponseJson -> encoder.encodeSerializableValue(CreateTranslationResponseJson.serializer(), value.value)
              is CaseCreateTranslationResponseVerboseJson -> encoder.encodeSerializableValue(CreateTranslationResponseVerboseJson.serializer(), value.value)
            }
          }
        }
      }
    }
  }

  public class VoiceConsents internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public val post: Post = Post(client)

    public fun consentId(consentId: String): ConsentIdPath = ConsentIdPath(client, consentId)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(after: String? = null, limit: Long? = 20L): VoiceConsentListResource = client.get("/audio/voice_consents") {
        after?.let { parameter("after", it) }
        limit?.let { parameter("limit", it) }
      }.body()
    }

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        name: String,
        recording: ByteArray,
        language: String,
      ): VoiceConsentResource = client.post("/audio/voice_consents") {
        setBody(MultiPartFormDataContent(formData {
          append("name", name)
          append("recording", recording)
          append("language", language)
        }))
      }.body()
    }

    public class ConsentIdPath internal constructor(
      private val client: HttpClient,
      private val consentId: String,
    ) {
      public val delete: Delete = Delete(client, consentId)

      public val `get`: Get = Get(client, consentId)

      public val post: Post = Post(client, consentId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val consentId: String,
      ) {
        public suspend operator fun invoke(): VoiceConsentDeletedResource = client.delete("/audio/voice_consents/$consentId").body()
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val consentId: String,
      ) {
        public suspend operator fun invoke(): VoiceConsentResource = client.get("/audio/voice_consents/$consentId").body()
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val consentId: String,
      ) {
        public suspend operator fun invoke(body: UpdateVoiceConsentRequest): VoiceConsentResource = client.post("/audio/voice_consents/$consentId") {
          contentType(ContentType.Application.Json)
          setBody(body)
        }.body()
      }
    }
  }

  public class Voices internal constructor(
    private val client: HttpClient,
  ) {
    public val post: Post = Post(client)

    public class Post internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        name: String,
        audioSample: ByteArray,
        consent: String,
      ): VoiceResource = client.post("/audio/voices") {
        setBody(MultiPartFormDataContent(formData {
          append("name", name)
          append("audio_sample", audioSample)
          append("consent", consent)
        }))
      }.body()
    }
  }
}
