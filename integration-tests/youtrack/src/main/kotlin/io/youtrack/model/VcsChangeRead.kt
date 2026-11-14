package io.youtrack.model

import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class VcsChangeRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val date: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fetched: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val files: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val author: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val processors: List<ChangesProcessor>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val urls: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val version: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issue: IssueRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val state: Int? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
