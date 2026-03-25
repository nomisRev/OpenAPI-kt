# Issue: AnyOf Generating complex models that can be simplified and made idiomatic.

We're currently generating "deep" hierarchies where shallow hierachies are desired.

```yaml
components:
	schemas:
		CreateSpeechResponseStreamEvent:
  			anyOf:
  			  - $ref: '#/components/schemas/SpeechAudioDeltaEvent'
  			  - $ref: '#/components/schemas/SpeechAudioDoneEvent'
  			discriminator:
  			  propertyName: type
  		SpeechAudioDeltaEvent:
  			type: object
  			description: Emitted for each chunk of audio data generated during speech synthesis.
  			properties:
  			  type:
  			    type: string
  			    description: |
  			      The type of the event. Always `speech.audio.delta`.
  			    enum:
  			      - speech.audio.delta
  			    x-stainless-const: true
  			  audio:
  			    type: string
  			    description: |
  			      A chunk of Base64-encoded audio data.
  			required:
  			  - type
  			  - audio
  			x-oaiMeta:
  			  name: Stream Event (speech.audio.delta)
  			  group: speech
  			  example: |
  			    {
  			      "type": "speech.audio.delta",
  			      "audio": "base64-encoded-audio-data"
  			    }
  		SpeechAudioDoneEvent:
  			type: object
  			description: >-
  			  Emitted when the speech synthesis is complete and all audio has been
  			  streamed.
  			properties:
  			  type:
  			    type: string
  			    description: |
  			      The type of the event. Always `speech.audio.done`.
  			    enum:
  			      - speech.audio.done
  			    x-stainless-const: true
  			  usage:
  			    type: object
  			    description: |
  			      Token usage statistics for the request.
  			    properties:
  			      input_tokens:
  			        type: integer
  			        description: Number of input tokens in the prompt.
  			      output_tokens:
  			        type: integer
  			        description: Number of output tokens generated.
  			      total_tokens:
  			        type: integer
  			        description: Total number of tokens used (input + output).
  			    required:
  			      - input_tokens
  			      - output_tokens
  			      - total_tokens
  			required:
  			  - type
  			  - usage
  			x-oaiMeta:
  			  name: Stream Event (speech.audio.done)
  			  group: speech
  			  example: |
  			    {
  			      "type": "speech.audio.done",
  			      "usage": {
  			        "input_tokens": 14,
  			        "output_tokens": 101,
  			        "total_tokens": 115
  			      }
  			    }
```

It should be generating a type that is simple, idiomatic, and correct.

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface CreateSpeechResponseStreamEvent {
    @Serializable
    @JvmInline
    @SerialName("speech.audio.delta")
    public value class SpeechAudioDelta(
        public val audio: String
    ) : CreateSpeechResponseStreamEvent

    @Serializable
    @SerialName("speech.audio.done")
    public data class SpeechAudioDone(
        @SerialName("input_tokens")
        public val inputTokens: Long,
        @SerialName("output_tokens")
        public val outputTokens: Long,
        @SerialName("total_tokens")
        public val totalTokens: Long,
    ) : CreateSpeechResponseStreamEvent
}
```

But its currently generating a deep hierarchy with subtle bugs:

```kotlin
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface CreateSpeechResponseStreamEvent {
  @Serializable
  @JvmInline
  @SerialName("SpeechAudioDeltaEvent")
  public value class SpeechAudioDeltaEvent(
    public val `value`: io.openai.model.SpeechAudioDeltaEvent,
  ) : CreateSpeechResponseStreamEvent

  @Serializable
  @JvmInline
  @SerialName("SpeechAudioDoneEvent")
  public value class SpeechAudioDoneEvent(
    public val `value`: io.openai.model.SpeechAudioDoneEvent,
  ) : CreateSpeechResponseStreamEvent
}
```


The `type` is correct but the `@SerialName` is completely made up.
Due to the `anyOf` pointing to references the generator respects the references but should just flatten them:

```kotlin
@Serializable
public data class SpeechAudioDoneEvent(
  public val type: Type,
  public val usage: Usage,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("speech.audio.done")
    SpeechAudioDone("speech.audio.done"),
    ;
  }

  /**
   * Token usage statistics for the request.
   *
   */
  @Serializable
  public data class Usage(
    @SerialName("input_tokens")
    public val inputTokens: Long,
    @SerialName("output_tokens")
    public val outputTokens: Long,
    @SerialName("total_tokens")
    public val totalTokens: Long,
  )
}
```

```kotlin
/**
 * Emitted for each chunk of audio data generated during speech synthesis.
 */
@Serializable
public data class SpeechAudioDeltaEvent(
  public val type: Type,
  public val audio: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("speech.audio.delta")
    SpeechAudioDelta("speech.audio.delta"),
    ;
  }
}
```
