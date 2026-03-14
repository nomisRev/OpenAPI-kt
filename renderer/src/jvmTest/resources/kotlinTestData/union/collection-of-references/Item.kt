package union.collection.of.references.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Item(val id: String)
