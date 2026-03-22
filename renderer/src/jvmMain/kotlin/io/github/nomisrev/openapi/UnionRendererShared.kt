package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName

internal val UnionJsonClassDiscriminatorType = ClassName("kotlinx.serialization.json", "JsonClassDiscriminator")
internal val UnionOptInType = ClassName("kotlin", "OptIn")
internal val UnionExperimentalUuidApiType = ClassName("kotlin.uuid", "ExperimentalUuidApi")
internal val UnionInstantType = ClassName("kotlin.time", "Instant")
internal val UnionExperimentalTimeType = ClassName("kotlin.time", "ExperimentalTime")
