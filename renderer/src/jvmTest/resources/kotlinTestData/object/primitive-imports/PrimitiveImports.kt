package io.github.nomisrev.render.test.object_.primitive.imports

import kotlin.OptIn
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalUuidApi::class)
@Serializable
public data class PrimitiveImports(
  public val date: LocalDate,
  public val dateTime: Instant,
  public val uuid: Uuid,
  public val json: JsonElement,
  public val jsonArray: JsonArray,
  public val jsonObject: JsonElement,
)
