package io.github.nomisrev.render.test.object_.nullable.alias.reference

import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class NullableAliasHolder(
  public val anyOfAlias: AnyOfAlias?,
  public val nullFirstAnyOfAlias: NullFirstAnyOfAlias?,
  public val oneOfAlias: OneOfAlias?,
  public val nullableAlias: NullableAlias?,
  public val allOfNullableAlias: AllOfNullableAlias?,
  public val optionalAnyOfAlias: AnyOfAlias? = null,
  public val anyOfAliases: List<AnyOfAlias?>,
  public val anyOfAliasMap: Map<String, AnyOfAlias?>,
)
