package io.github.nomisrev.render.test.client.recursive.allof.ref

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Node(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val child: Node? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val children: NodeList? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val byName: NodeMap? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: Owner? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val annotated: Annotated? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Annotated(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val child: Node? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val children: NodeList? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val byName: NodeMap? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: Owner? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val annotated: Annotated? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val note: String? = null,
  )
}
