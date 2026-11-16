package io.github.nomisrev.render.test.client.allof.ref.alias

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class MyString(
  public val `value`: String,
)
