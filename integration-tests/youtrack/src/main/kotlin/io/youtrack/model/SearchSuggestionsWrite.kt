package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SearchSuggestionsWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val caret: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ignoreUnresolvedSetting: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val query: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val folders: List<IssueFolderWrite>? = null,
)
