package io.github.nomisrev.render.test.client.discriminated.union.nullable

import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class PetHolder(
  public val pet: NullBranchPet?,
  public val optionalPet: NullBranchPet? = null,
  public val pets: List<NullBranchPet?>,
  public val petMap: Map<String, NullBranchPet?>,
  public val member: NullableMemberPet?,
  public val optionalMember: NullableMemberPet? = null,
  public val members: List<NullableMemberPet?>,
  public val memberMap: Map<String, NullableMemberPet?>,
  public val tagged: TaggedNullableMemberPet?,
  public val taggedList: List<TaggedNullableMemberPet?>,
)
