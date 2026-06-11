package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Manifest(
  public val name: String,
  public val `file`: File? = null,
  public val metadata: Metadata? = null,
  public val resolved: List<Dependency>? = null,
) {
  @JvmInline
  @Serializable
  public value class File(
    @SerialName("source_location")
    public val sourceLocation: String? = null,
  )
}
