package io.github.nomisrev.render.test.client.allof.ref.alias

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AllOfRefAliasHolder(
  public val primAllOf: PrimAllOf,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalPrimAllOf: PrimAllOf? = null,
  public val primAllOfs: List<PrimAllOf>,
  public val primAllOfMap: Map<String, PrimAllOf>,
  public val listAllOf: ListAllOf,
  public val listAllOfs: List<ListAllOf>,
  public val nullablePrimAllOf: NullablePrimAllOf?,
  public val nullableListAllOf: NullableListAllOf?,
  public val inlinePrimAllOf: String,
  public val inlineListAllOf: List<String>,
)
