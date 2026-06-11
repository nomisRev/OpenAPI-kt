package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Contributor Activity
 */
@Serializable
public data class ContributorActivity(
  public val author: NullableSimpleUser?,
  public val total: Long,
  public val weeks: List<Weeks>,
) {
  @Serializable
  public data class Weeks(
    public val w: Long? = null,
    public val a: Long? = null,
    public val d: Long? = null,
    public val c: Long? = null,
  )
}
