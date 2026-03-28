# ISSUE IMPROVE DESERIALIZE LOGIC

```kotlin
override fun deserialize(decoder: Decoder): Template {
    val value = decoder.decodeSerializableValue(JsonElement.serializer())
    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
    val obj = value as? JsonObject
    val tag = (obj?.get("role") as? JsonPrimitive)?.content
    when(tag) {
        "user" -> {
            val keys = obj?.keys.orEmpty()
            if ("phase" in keys) {
                return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
            }
            return json.attemptDeserialize(
                value,
                EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
                EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
            )
        }
        "assistant" -> {
            val keys = obj?.keys.orEmpty()
            if ("phase" in keys) {
                return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
            }
            return json.attemptDeserialize(
                value,
                EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
                EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
            )
        }
        "system" -> {
            val keys = obj?.keys.orEmpty()
            if ("phase" in keys) {
                return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
            }
            return json.attemptDeserialize(
                value,
                EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
                EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
            )
        }
        "developer" -> {
            val keys = obj?.keys.orEmpty()
            if ("phase" in keys) {
                return json.decodeFromJsonElement(EasyInputMessage.serializer(), value)
            }
            return json.attemptDeserialize(
                value,
                EasyInputMessage::class to { decodeFromJsonElement(EasyInputMessage.serializer(), it) },
                EvalItem::class to { decodeFromJsonElement(EvalItem.serializer(), it) },
            )
        }
        else -> throw SerializationException("Unknown tag: " + tag + " for io.openai.model.EvalRun.DataSource.Completions.InputMessages.Template.Template")
    }
}
```