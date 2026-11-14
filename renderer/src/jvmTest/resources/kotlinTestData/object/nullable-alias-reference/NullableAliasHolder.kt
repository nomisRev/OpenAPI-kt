package io.github.nomisrev.render.test.object_.nullable.alias.reference

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableAliasHolder(
  public val anyOfAlias: AnyOfAlias?,
  public val nullFirstAnyOfAlias: NullFirstAnyOfAlias?,
  public val oneOfAlias: OneOfAlias?,
  public val nullableAlias: NullableAlias?,
  public val allOfNullableAlias: AllOfNullableAlias?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalAnyOfAlias: Optional<AnyOfAlias> = Optional.Absent,
  public val anyOfAliases: List<AnyOfAlias?>,
  public val anyOfAliasMap: Map<String, AnyOfAlias?>,
)
