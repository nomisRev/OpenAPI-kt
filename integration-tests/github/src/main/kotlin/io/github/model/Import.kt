package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A repository import from an external source.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Import(
  public val vcs: String?,
  @SerialName("use_lfs")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val useLfs: Boolean? = null,
  @SerialName("vcs_url")
  public val vcsUrl: String,
  @SerialName("svc_root")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val svcRoot: String? = null,
  @SerialName("tfvc_project")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasLargeFiles: Boolean? = null,
  @SerialName("large_files_size")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val largeFilesSize: Long? = null,
  @SerialName("large_files_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val largeFilesCount: Long? = null,
  @SerialName("project_choices")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val projectChoices: List<ProjectChoices>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val svnRoot: String? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectChoices(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val vcs: String? = null,
    @SerialName("tfvc_project")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tfvcProject: String? = null,
    @SerialName("human_name")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
