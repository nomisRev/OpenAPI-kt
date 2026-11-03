package io.github.nomisrev.render.test.client.single.ref.alias

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SingleRefAliasHolder(
  public val anyOfOwner: AnyOfOwner,
  public val oneOfOwner: OneOfOwner,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalAnyOfOwner: AnyOfOwner? = null,
  public val anyOfOwners: List<AnyOfOwner>,
  public val oneOfOwnerMap: Map<String, OneOfOwner>,
  public val anyOfColor: AnyOfColor,
  public val oneOfLabel: OneOfLabel,
  public val anyOfNames: AnyOfNames,
  public val inlineAnyOfOwner: Owner,
  public val owner: Owner,
)
