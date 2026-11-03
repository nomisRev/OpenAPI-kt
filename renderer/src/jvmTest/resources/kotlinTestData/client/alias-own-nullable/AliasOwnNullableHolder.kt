package io.github.nomisrev.render.test.client.alias.own.nullable

import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class AliasOwnNullableHolder(
  public val owner: NullableAllOfOwner?,
  public val owners: List<NullableAllOfOwner?>,
  public val ownerMap: Map<String, NullableAllOfOwner?>,
  public val color: NullableAnyOfColor?,
  public val colors: List<NullableAnyOfColor?>,
  public val colorMap: Map<String, NullableAnyOfColor?>,
  public val string: NullableOneOfString?,
  public val strings: List<NullableOneOfString?>,
  public val stringMap: Map<String, NullableOneOfString?>,
  public val names: NullableAllOfNames?,
  public val namesList: List<NullableAllOfNames?>,
  public val namesMap: Map<String, NullableAllOfNames?>,
  public val nullableTarget: AllOfNullableOwner?,
  public val nullableTargets: List<AllOfNullableOwner?>,
  public val optionalOwner: NullableAllOfOwner? = null,
)
