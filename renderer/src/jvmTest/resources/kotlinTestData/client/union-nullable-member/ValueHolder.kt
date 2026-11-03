package io.github.nomisrev.render.test.client.union.nullable.member

import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class ValueHolder(
  public val ref: RefMember?,
  public val optionalRef: RefMember? = null,
  public val refs: List<RefMember?>,
  public val refMap: Map<String, RefMember?>,
  public val anyOf: AnyOfRefMember?,
  public val anyOfs: List<AnyOfRefMember?>,
  public val anyOfMap: Map<String, AnyOfRefMember?>,
  public val `inline`: InlineNullableMember?,
  public val inlines: List<InlineNullableMember?>,
  public val inlineMap: Map<String, InlineNullableMember?>,
  public val typeArray: TypeArrayMember?,
  public val typeArrays: List<TypeArrayMember?>,
  public val typeArrayMap: Map<String, TypeArrayMember?>,
  public val twoNullable: TwoNullableMembers?,
  public val twoNullables: List<TwoNullableMembers?>,
  public val `object`: ObjectMember?,
  public val objects: List<ObjectMember?>,
  public val objectMap: Map<String, ObjectMember?>,
  public val pet: InferredPet?,
  public val pets: List<InferredPet?>,
  public val petMap: Map<String, InferredPet?>,
)
