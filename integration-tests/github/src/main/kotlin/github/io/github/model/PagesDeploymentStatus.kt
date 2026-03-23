package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class PagesDeploymentStatus(
  public val status: Status? = null,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("deployment_in_progress")
    DeploymentInProgress("deployment_in_progress"),
    @SerialName("syncing_files")
    SyncingFiles("syncing_files"),
    @SerialName("finished_file_sync")
    FinishedFileSync("finished_file_sync"),
    @SerialName("updating_pages")
    UpdatingPages("updating_pages"),
    @SerialName("purging_cdn")
    PurgingCdn("purging_cdn"),
    @SerialName("deployment_cancelled")
    DeploymentCancelled("deployment_cancelled"),
    @SerialName("deployment_failed")
    DeploymentFailed("deployment_failed"),
    @SerialName("deployment_content_failed")
    DeploymentContentFailed("deployment_content_failed"),
    @SerialName("deployment_attempt_error")
    DeploymentAttemptError("deployment_attempt_error"),
    @SerialName("deployment_lost")
    DeploymentLost("deployment_lost"),
    @SerialName("succeed")
    Succeed("succeed"),
    ;
  }
}
