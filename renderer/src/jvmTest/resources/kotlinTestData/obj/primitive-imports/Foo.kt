package obj.primitive.imports.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.Uuid
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlin.uuid.ExperimentalUuidApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Foo(
    val date: LocalDate,
    val dateTime: LocalDateTime,
    val uuid: Uuid,
    val json: JsonElement,
    val jsonArray: JsonArray,
    val jsonObject: JsonObject
)
