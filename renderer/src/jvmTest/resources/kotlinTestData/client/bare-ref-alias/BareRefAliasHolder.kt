package io.github.nomisrev.render.test.client.bare.ref.alias

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BareRefAliasHolder(
  public val bareOwner: BareOwner,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalBareOwner: BareOwner? = null,
  public val bareOwners: List<BareOwner>,
  public val bareOwnerMap: Map<String, BareOwner>,
  public val bareColor: BareColor,
  public val bareLabel: BareLabel,
  public val bareNames: BareNames,
  public val chainOwner: ChainOwner,
  public val nullableChainOwner: NullableChainOwner?,
  public val owner: Owner,
)
