package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for a repository ruleset ref name condition
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryRulesetConditions(
  @SerialName("ref_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val refName: RefName? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RefName(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val include: List<String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val exclude: List<String>? = null,
  )
}
