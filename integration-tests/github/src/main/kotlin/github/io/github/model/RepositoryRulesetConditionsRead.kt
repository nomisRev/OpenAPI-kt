package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for a repository ruleset ref name condition
 */
@JvmInline
@Serializable
public value class RepositoryRulesetConditionsRead(
  @SerialName("ref_name")
  public val refName: RefName? = null,
) {
  @Serializable
  public data class RefName(
    public val include: List<String>? = null,
    public val exclude: List<String>? = null,
  )
}
