# Issue: non-idiomatic Kotlin client generated

## Step 1: Research issue

Find the `"/repos/{owner}/{repo}/check-runs/{check_run_id}"` patch route and inline the JSON OpenAPI for it.
Include the **full** spec for it, meaning look for all references schemas and add them.

## Current Code

```kotlin
public class Patch internal constructor(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
  private val checkRunId: Long,
) {
  public suspend operator fun invoke(body: One): CheckRun = client.patch("/repos/$owner/$repo/check-runs/$checkRunId") {
    contentType(ContentType.Application.Json)
    setBody(body)
  }.body()

  public suspend operator fun invoke(body: Two): CheckRun = client.patch("/repos/$owner/$repo/check-runs/$checkRunId") {
    contentType(ContentType.Application.Json)
    setBody(body)
  }.body()

  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = One.Serializer::class)
  public data class One(
    public val status: Status? = null,
    public val additional: JsonObject? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("completed")
      Completed("completed"),
      ;
    }

    public object Serializer : KSerializer<One> {
      override val descriptor: SerialDescriptor = generatedSerializer().descriptor

      override fun serialize(encoder: Encoder, `value`: One) {
        val json = (encoder as JsonEncoder).json
        val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
        val content = mutableMapOf<String, JsonElement>()
        known.forEach { (key, jsonElement) ->
          if (key != "additional") {
            content[key] = jsonElement
          }
        }
        value.additional?.forEach { (key, jsonElement) ->
          content[key] = jsonElement
        }
        encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
      }

      override fun deserialize(decoder: Decoder): One {
        val json = (decoder as JsonDecoder).json
        val element = decoder.decodeSerializableValue(JsonObject.serializer())
        val knownNames = setOf("status")
        val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
        val additional = JsonObject(element - knownNames).ifEmpty { null }
        return known.copy(additional = additional)
      }
    }
  }

  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Two.Serializer::class)
  public data class Two(
    public val status: Status? = null,
    public val additional: JsonObject? = null,
  ) {
    @Serializable
    public enum class Status(
      public val `value`: String,
    ) {
      @SerialName("queued")
      Queued("queued"),
      @SerialName("in_progress")
      InProgress("in_progress"),
      ;
    }

    public object Serializer : KSerializer<Two> {
      override val descriptor: SerialDescriptor = generatedSerializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Two) {
        val json = (encoder as JsonEncoder).json
        val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
        val content = mutableMapOf<String, JsonElement>()
        known.forEach { (key, jsonElement) ->
          if (key != "additional") {
            content[key] = jsonElement
          }
        }
        value.additional?.forEach { (key, jsonElement) ->
          content[key] = jsonElement
        }
        encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
      }

      override fun deserialize(decoder: Decoder): Two {
        val json = (decoder as JsonDecoder).json
        val element = decoder.decodeSerializableValue(JsonObject.serializer())
        val knownNames = setOf("status")
        val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
        val additional = JsonObject(element - knownNames).ifEmpty { null }
        return known.copy(additional = additional)
      }
    }
  }
}
```
