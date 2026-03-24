package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Confirmation payload returned after unassigning a role.
 */
@Serializable
public data class DeletedRoleAssignmentResource(
  public val `object`: String,
  public val deleted: Boolean,
)
