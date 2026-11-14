package io.github.nomisrev.render.test.client.discriminated.union.nullable

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PetHolder(
  public val pet: NullBranchPet?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalPet: Optional<NullBranchPet> = Optional.Absent,
  public val pets: List<NullBranchPet?>,
  public val petMap: Map<String, NullBranchPet?>,
  public val member: NullableMemberPet?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalMember: Optional<NullableMemberPet> = Optional.Absent,
  public val members: List<NullableMemberPet?>,
  public val memberMap: Map<String, NullableMemberPet?>,
  public val tagged: TaggedNullableMemberPet?,
  public val taggedList: List<TaggedNullableMemberPet?>,
)
