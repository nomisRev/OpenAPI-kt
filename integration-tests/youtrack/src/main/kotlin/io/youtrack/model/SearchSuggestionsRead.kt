package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SearchSuggestionsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val caret: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ignoreUnresolvedSetting: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val query: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val suggestions: List<Suggestion>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val folders: List<IssueFolderRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
