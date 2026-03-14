package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class ContributorActivity(val author: NullableSimpleUser?, val total: Long, val weeks: List<Weeks>) {
    @Serializable
    data class Weeks(val w: Long? = null, val a: Long? = null, val d: Long? = null, val c: Long? = null)
}
