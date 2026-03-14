package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class PagesDeploymentStatus(val status: Status? = null) {
    @Serializable
    enum class Status {
        @SerialName("deployment_in_progress")
        DeploymentInProgress,
        @SerialName("syncing_files")
        SyncingFiles,
        @SerialName("finished_file_sync")
        FinishedFileSync,
        @SerialName("updating_pages")
        UpdatingPages,
        @SerialName("purging_cdn")
        PurgingCdn,
        @SerialName("deployment_cancelled")
        DeploymentCancelled,
        @SerialName("deployment_failed")
        DeploymentFailed,
        @SerialName("deployment_content_failed")
        DeploymentContentFailed,
        @SerialName("deployment_attempt_error")
        DeploymentAttemptError,
        @SerialName("deployment_lost")
        DeploymentLost,
        @SerialName("succeed")
        Succeed;
    }
}
