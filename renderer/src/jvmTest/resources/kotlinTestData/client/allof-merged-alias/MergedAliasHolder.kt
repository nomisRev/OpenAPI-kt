package io.github.nomisrev.render.test.client.allof.merged.alias

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MergedAliasHolder(
  public val tags: Tags,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalTags: Tags? = null,
  public val tagLists: List<Tags>,
  public val tagMap: Map<String, Tags>,
  public val code: Code,
  public val codes: List<Code>,
  public val labels: Labels,
  public val names: NonEmptyNames,
  public val nullableTags: NullableTags?,
  public val nullableCode: NullableCode?,
  public val tagsOrCount: TagsOrCount,
)
