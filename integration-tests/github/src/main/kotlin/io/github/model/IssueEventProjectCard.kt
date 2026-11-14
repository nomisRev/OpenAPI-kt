package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Issue Event Project Card
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueEventProjectCard(
  public val url: String,
  public val id: Long,
  @SerialName("project_url")
  public val projectUrl: String,
  @SerialName("project_id")
  public val projectId: Long,
  @SerialName("column_name")
  public val columnName: String,
  @SerialName("previous_column_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previousColumnName: String? = null,
)
