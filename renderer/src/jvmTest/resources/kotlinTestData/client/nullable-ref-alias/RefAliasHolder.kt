package io.github.nomisrev.render.test.client.nullable.ref.alias

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RefAliasHolder(
  public val anyOfRefAlias: AnyOfRefAlias?,
  public val nullFirstAnyOfRefAlias: NullFirstAnyOfRefAlias?,
  public val oneOfRefAlias: OneOfRefAlias?,
  public val nullFirstOneOfRefAlias: NullFirstOneOfRefAlias?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalAnyOfRefAlias: Optional<AnyOfRefAlias> = Optional.Absent,
  public val anyOfRefAliases: List<AnyOfRefAlias?>,
  public val anyOfRefAliasMap: Map<String, AnyOfRefAlias?>,
  public val owner: Owner,
)
