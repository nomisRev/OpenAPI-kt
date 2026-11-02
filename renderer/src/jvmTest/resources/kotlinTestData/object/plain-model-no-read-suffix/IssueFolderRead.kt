package io.github.nomisrev.render.test.object_.plain.model.no.read.suffix

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueFolderRead {
  public val id: String?

  @SerialName("IssueFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : IssueFolderRead

  @SerialName("Project")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Project(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shortName: String? = null,
  ) : IssueFolderRead
}
