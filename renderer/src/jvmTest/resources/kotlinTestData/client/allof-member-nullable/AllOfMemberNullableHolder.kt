package io.github.nomisrev.render.test.client.allof.member.nullable

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AllOfMemberNullableHolder(
  public val mixed: MixedOwner,
  public val mixedList: List<MixedOwner>,
  public val allNullable: AllNullableOwner?,
  public val allNullableList: List<AllNullableOwner?>,
  public val ownNullable: OwnNullableOwner?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalMixed: MixedOwner? = null,
)
