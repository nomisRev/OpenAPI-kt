package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Manifest(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `file`: File? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Metadata? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resolved: Map<String, Dependency>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class File(
    @SerialName("source_location")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val sourceLocation: String? = null,
  )
}
