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
    val date: LocalDate? = null,
    val dateTime: LocalDateTime? = null,
    val uuid: Uuid? = null,
    val json: JsonElement? = null,
    val jsonArray: JsonArray? = null,
    val jsonObject: JsonObject? = null,
)
