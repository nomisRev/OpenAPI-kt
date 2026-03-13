package obj.single.property.additional.json.model

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder

fun JsonObjectBuilder.putAll(jsonObject: JsonObject?) =
    jsonObject.orEmpty().forEach { (key, value) -> put(key, value) }
