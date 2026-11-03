package io.github.nomisrev.render.test.client.form.urlencoded.body

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class SearchQuery(
  public val query: String,
  public val limit: Long,
)
