package io.github.nomisrev.render.test.collection.item.with.`inline`.`enum`

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A list of default code security configurations
 */
@JvmInline
@Serializable
public value class CodeSecurityDefaultConfigurations(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    @SerialName("default_for_new_repos")
    public val defaultForNewRepos: DefaultForNewRepos? = null,
    public val name: String? = null,
  ) {
    @Serializable
    public enum class DefaultForNewRepos(
      public val `value`: String,
    ) {
      @SerialName("public")
      Public("public"),
      @SerialName("private_and_internal")
      PrivateAndInternal("private_and_internal"),
      @SerialName("all")
      All("all"),
      ;
    }
  }
}
