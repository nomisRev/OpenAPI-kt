package io.github.nomisrev.model

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class OrgRules(val value: JsonElement)
