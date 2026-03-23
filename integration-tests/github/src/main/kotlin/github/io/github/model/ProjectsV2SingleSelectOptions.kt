package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An option for a single select field
 */
@Serializable
public data class ProjectsV2SingleSelectOptions(
  public val id: String,
  public val name: Name,
  public val description: Description,
  public val color: String,
) {
  /**
   * The description of the option, in raw text and HTML formats.
   */
  @Serializable
  public data class Description(
    public val raw: String,
    public val html: String,
  )

  /**
   * The display name of the option, in raw text and HTML formats.
   */
  @Serializable
  public data class Name(
    public val raw: String,
    public val html: String,
  )
}
