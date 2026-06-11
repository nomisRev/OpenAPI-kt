package io.github.nomisrev.render.test.client.primitive.input.flattened

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@JvmInline
@OptIn(ExperimentalUuidApi::class)
@Serializable
public value class RequestId(
  public val `value`: Uuid,
)
