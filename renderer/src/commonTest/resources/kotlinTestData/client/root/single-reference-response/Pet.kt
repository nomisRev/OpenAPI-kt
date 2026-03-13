package client.root.`single-reference-response`

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Pet(val name: String)