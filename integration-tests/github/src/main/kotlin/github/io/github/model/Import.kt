package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A repository import from an external source.
 */
@Serializable
public data class Import(
  public val vcs: String?,
  @SerialName("use_lfs")
  public val useLfs: Boolean? = null,
  @SerialName("vcs_url")
  public val vcsUrl: String,
  @SerialName("svc_root")
  public val svcRoot: String? = null,
  @SerialName("tfvc_project")
  public val tfvcProject: String? = null,
  public val status: Status,
  @SerialName("status_text")
  public val statusText: String? = null,
  @SerialName("failed_step")
  public val failedStep: String? = null,
  @SerialName("error_message")
  public val errorMessage: String? = null,
  @SerialName("import_percent")
  public val importPercent: Long? = null,
  @SerialName("commit_count")
  public val commitCount: Long? = null,
  @SerialName("push_percent")
  public val pushPercent: Long? = null,
  @SerialName("has_large_files")
  public val hasLargeFiles: Boolean? = null,
  @SerialName("large_files_size")
  public val largeFilesSize: Long? = null,
  @SerialName("large_files_count")
  public val largeFilesCount: Long? = null,
  @SerialName("project_choices")
  public val projectChoices: List<ProjectChoices>? = null,
  public val message: String? = null,
  @SerialName("authors_count")
  public val authorsCount: Long? = null,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("authors_url")
  public val authorsUrl: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("svn_root")
  public val svnRoot: String? = null,
) {
  @Serializable
  public data class ProjectChoices(
    public val vcs: String? = null,
    @SerialName("tfvc_project")
    public val tfvcProject: String? = null,
    @SerialName("human_name")
    public val humanName: String? = null,
  )

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("auth")
    Auth("auth"),
    @SerialName("error")
    Error("error"),
    @SerialName("none")
    None("none"),
    @SerialName("detecting")
    Detecting("detecting"),
    @SerialName("choose")
    Choose("choose"),
    @SerialName("auth_failed")
    AuthFailed("auth_failed"),
    @SerialName("importing")
    Importing("importing"),
    @SerialName("mapping")
    Mapping("mapping"),
    @SerialName("waiting_to_push")
    WaitingToPush("waiting_to_push"),
    @SerialName("pushing")
    Pushing("pushing"),
    @SerialName("complete")
    Complete("complete"),
    @SerialName("setup")
    Setup("setup"),
    @SerialName("unknown")
    Unknown("unknown"),
    @SerialName("detection_found_multiple")
    DetectionFoundMultiple("detection_found_multiple"),
    @SerialName("detection_found_nothing")
    DetectionFoundNothing("detection_found_nothing"),
    @SerialName("detection_needs_auth")
    DetectionNeedsAuth("detection_needs_auth"),
    ;
  }
}
