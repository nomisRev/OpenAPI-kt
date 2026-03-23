package io.youtrack.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VcsChangeRead(
  public val id: String? = null,
  public val date: Long? = null,
  public val fetched: Long? = null,
  public val files: Int? = null,
  public val author: UserRead? = null,
  public val processors: List<ChangesProcessor>? = null,
  public val text: String? = null,
  public val urls: List<String>? = null,
  public val version: String? = null,
  public val issue: IssueRead? = null,
  public val state: Int? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
