package io.github.nomisrev.render.test.client.nullable.ref.alias

import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class RefAliasHolder(
  public val anyOfRefAlias: AnyOfRefAlias?,
  public val nullFirstAnyOfRefAlias: NullFirstAnyOfRefAlias?,
  public val oneOfRefAlias: OneOfRefAlias?,
  public val nullFirstOneOfRefAlias: NullFirstOneOfRefAlias?,
  public val optionalAnyOfRefAlias: AnyOfRefAlias? = null,
  public val anyOfRefAliases: List<AnyOfRefAlias?>,
  public val anyOfRefAliasMap: Map<String, AnyOfRefAlias?>,
  public val owner: Owner,
)
