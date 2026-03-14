package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Import(
    val vcs: String?,
    @SerialName("use_lfs") val useLfs: Boolean? = null,
    @SerialName("vcs_url") val vcsUrl: String,
    @SerialName("svc_root") val svcRoot: String? = null,
    @SerialName("tfvc_project") val tfvcProject: String? = null,
    val status: Status,
    @SerialName("status_text") val statusText: String? = null,
    @SerialName("failed_step") val failedStep: String? = null,
    @SerialName("error_message") val errorMessage: String? = null,
    @SerialName("import_percent") val importPercent: Long? = null,
    @SerialName("commit_count") val commitCount: Long? = null,
    @SerialName("push_percent") val pushPercent: Long? = null,
    @SerialName("has_large_files") val hasLargeFiles: Boolean? = null,
    @SerialName("large_files_size") val largeFilesSize: Long? = null,
    @SerialName("large_files_count") val largeFilesCount: Long? = null,
    @SerialName("project_choices") val projectChoices: List<ProjectChoices>? = null,
    val message: String? = null,
    @SerialName("authors_count") val authorsCount: Long? = null,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("authors_url") val authorsUrl: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("svn_root") val svnRoot: String? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("auth")
        Auth,
        @SerialName("error")
        Error,
        @SerialName("none")
        None,
        @SerialName("detecting")
        Detecting,
        @SerialName("choose")
        Choose,
        @SerialName("auth_failed")
        AuthFailed,
        @SerialName("importing")
        Importing,
        @SerialName("mapping")
        Mapping,
        @SerialName("waiting_to_push")
        WaitingToPush,
        @SerialName("pushing")
        Pushing,
        @SerialName("complete")
        Complete,
        @SerialName("setup")
        Setup,
        @SerialName("unknown")
        Unknown,
        @SerialName("detection_found_multiple")
        DetectionFoundMultiple,
        @SerialName("detection_found_nothing")
        DetectionFoundNothing,
        @SerialName("detection_needs_auth")
        DetectionNeedsAuth;
    }

    @Serializable
    data class ProjectChoices(
        val vcs: String? = null,
        @SerialName("tfvc_project") val tfvcProject: String? = null,
        @SerialName("human_name") val humanName: String? = null,
    )
}
