package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class SearchSuggestionsWrite(
  public val caret: Int? = null,
  public val ignoreUnresolvedSetting: Boolean? = null,
  public val query: String? = null,
  public val folders: List<IssueFolderWrite>? = null,
)
