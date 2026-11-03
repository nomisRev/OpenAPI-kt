package io.github.nomisrev.render.test.client.union.token.kind.dispatch

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class UnionHolder(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stringOrInteger: StringOrInteger? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val integerOrString: IntegerOrString? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stringOrBoolean: StringOrBoolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val numberOrInteger: NumberOrInteger? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stringOrEnumOrInteger: StringOrEnumOrInteger? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val stringOrDateTime: StringOrDateTime? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val plainStringOrDate: PlainStringOrDate? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val freeFormOrCat: FreeFormOrCat? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mapOrList: MapOrList? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val integerOrCat: IntegerOrCat? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val plainStringOrInteger: PlainStringOrInteger? = null,
)
