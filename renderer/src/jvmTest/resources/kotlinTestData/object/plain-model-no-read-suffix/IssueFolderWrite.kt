package io.github.nomisrev.render.test.object_.plain.model.no.read.suffix

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueFolderWrite {
  @SerialName("Default")
  @Serializable
  public data object Default : IssueFolderWrite

  @SerialName("Project")
  @Serializable
  public data class Project(
    public val shortName: String? = null,
  ) : IssueFolderWrite
}
