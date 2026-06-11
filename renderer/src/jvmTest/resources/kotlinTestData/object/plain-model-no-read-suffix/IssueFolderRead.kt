package io.github.nomisrev.render.test.object_.plain.model.no.read.suffix

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueFolderRead {
  public val id: String?

  @JvmInline
  @SerialName("IssueFolder")
  @Serializable
  public value class Default(
    override val id: String? = null,
  ) : IssueFolderRead

  @SerialName("Project")
  @Serializable
  public data class Project(
    override val id: String? = null,
    public val shortName: String? = null,
  ) : IssueFolderRead
}
