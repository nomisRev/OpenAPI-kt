package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SearchSuggestionsRead(
  public val id: String? = null,
  public val caret: Int? = null,
  public val ignoreUnresolvedSetting: Boolean? = null,
  public val query: String? = null,
  public val suggestions: List<Suggestion>? = null,
  public val folders: List<IssueFolderRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
