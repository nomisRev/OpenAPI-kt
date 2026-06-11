package io.github.nomisrev.render.test.object_.nested

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Container(
  public val child: Child,
  public val sort: Sort,
  public val children: List<Children>,
) {
  @JvmInline
  @Serializable
  public value class Child(
    public val name: String,
  )

  @JvmInline
  @Serializable
  public value class Children(
    public val id: String,
  )

  @Serializable
  public enum class Sort(
    public val `value`: String,
  ) {
    @SerialName("asc")
    Asc("asc"),
    @SerialName("desc")
    Desc("desc"),
    ;
  }
}
