package io.youtrack

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.youtrack.api.YouTrackRESTAPI
import io.youtrack.model.ActivityCursorPage
import io.youtrack.model.ActivityItem
import io.youtrack.model.AgileRead
import io.youtrack.model.BaseArticleRead
import io.youtrack.model.BaseWorkItemRead
import io.youtrack.model.BundleRead
import io.youtrack.model.CommandListRead
import io.youtrack.model.CommandListWrite
import io.youtrack.model.CustomFieldRead
import io.youtrack.model.FieldTypeRead
import io.youtrack.model.GlobalSettingsRead
import io.youtrack.model.IssueCountResponseRead
import io.youtrack.model.IssueCountResponseWrite
import io.youtrack.model.IssueCustomFieldRead
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.IssueLink
import io.youtrack.model.IssueLinkTypeRead
import io.youtrack.model.IssueRead
import io.youtrack.model.ProjectCustomFieldRead
import io.youtrack.model.ProjectTimeTrackingSettingsRead
import io.youtrack.model.SearchSuggestionsRead
import io.youtrack.model.SearchSuggestionsWrite
import io.youtrack.model.Telemetry
import io.youtrack.model.UserRead
import io.youtrack.model.WorkItemTypeRead
import io.youtrack.model.WorkTimeSettingsRead
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

/**
 * Read-only smoke runner for a live YouTrack instance.
 *
 * Required env:
 * - YOUTRACK_BASE_URL, for example https://example.youtrack.cloud/api
 * - YOUTRACK_TOKEN
 *
 * Optional env:
 * - YOUTRACK_ISSUE_ID
 * - YOUTRACK_ISSUE_ID_READABLE
 * - YOUTRACK_ISSUE_QUERY
 * - YOUTRACK_SMOKE_TOP
 * - YOUTRACK_INCLUDE_ADMIN
 * - YOUTRACK_LOG_HTTP
 */
fun main(): Unit = runBlocking {
    val env = SmokeEnv.fromEnvironment()
    val api = YouTrackRESTAPI(env.baseUrl) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
        defaultRequest {
            headers.append(HttpHeaders.Authorization, "Bearer ${env.token}")
        }
        if (env.logHttp) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    try {
        SmokeSuite(api, env).run()
    } finally {
        api.close()
    }
}

private data class SmokeEnv(
    val baseUrl: String,
    val token: String,
    val issueId: String?,
    val issueIdReadable: String?,
    val issueQuery: String,
    val top: Int,
    val includeAdmin: Boolean,
    val logHttp: Boolean,
) {
    companion object {
        fun fromEnvironment(): SmokeEnv =
            SmokeEnv(
                baseUrl = requireEnv("YOUTRACK_BASE_URL").removeSuffix("/"),
                token = requireEnv("YOUTRACK_TOKEN"),
                issueId = optionalEnv("YOUTRACK_ISSUE_ID"),
                issueIdReadable = optionalEnv("YOUTRACK_ISSUE_ID_READABLE"),
                issueQuery = optionalEnv("YOUTRACK_ISSUE_QUERY") ?: "sort by: updated desc",
                top = optionalEnv("YOUTRACK_SMOKE_TOP")?.toIntOrNull()?.coerceIn(1, 10) ?: 3,
                includeAdmin = envFlag("YOUTRACK_INCLUDE_ADMIN"),
                logHttp = envFlag("YOUTRACK_LOG_HTTP"),
            )

        private fun requireEnv(name: String): String =
            optionalEnv(name) ?: error("Missing required env var: $name")

        private fun optionalEnv(name: String): String? =
            System.getenv(name)?.trim()?.takeIf { it.isNotEmpty() }

        private fun envFlag(name: String): Boolean =
            optionalEnv(name)?.lowercase()?.let { it in setOf("1", "true", "yes", "on") } ?: false
    }
}

private class SmokeSuite(
    private val api: YouTrackRESTAPI,
    private val env: SmokeEnv,
) {
    private val failures = mutableListOf<String>()
    private var passed = 0
    private var skipped = 0

    suspend fun run() {
        println(
            "YouTrack smoke baseUrl=${env.baseUrl} top=${env.top} " +
                "issueSelector=${env.issueId ?: env.issueIdReadable ?: env.issueQuery}"
        )

        section("Identity")
        val me = required("users.me.get") { api.users.me.get() }
        val meId = idOrSkip("users.me.id", me?.id) ?: run {
            finish()
            return
        }

        required("users.id($meId).get") { api.users.id(meId).get() }
        required("users.id($meId).profiles.general.get") { api.users.id(meId).profiles.general.get() }
        required("users.id($meId).profiles.notifications.get") { api.users.id(meId).profiles.notifications.get() }
        required("users.id($meId).profiles.timetracking.get") { api.users.id(meId).profiles.timetracking.get() }

        val userSavedQueries =
            required("users.id($meId).savedQueries.get") { api.users.id(meId).savedQueries.get(top = env.top) }.orEmpty()
        val userTags =
            required("users.id($meId).tags.get") { api.users.id(meId).tags.get(top = env.top) }.orEmpty()

        section("Top-Level")
        val groups = required("groups.get") { api.groups.get(top = env.top) }.orEmpty()
        groups.firstOrNull()?.id?.let { groupId ->
            required("groups.id($groupId).get") { api.groups.id(groupId).get() }
        } ?: skip("groups.id(<first>).get", "no group ids returned")

        val linkTypes = required("issueLinkTypes.get") { api.issueLinkTypes.get(top = env.top) }.orEmpty()
        linkTypes.firstOrNull()?.id?.let { linkTypeId ->
            required("issueLinkTypes.id($linkTypeId).get") { api.issueLinkTypes.id(linkTypeId).get() }
        } ?: skip("issueLinkTypes.id(<first>).get", "no link type ids returned")

        val savedQueries = required("savedQueries.get") { api.savedQueries.get(top = env.top) }.orEmpty()
        val tags = required("tags.get") { api.tags.get(top = env.top) }.orEmpty()

        val issue = required("seed issue") { discoverIssue() }
        val issueSearch = issueSearch(issue)

        required("issuesGetter.count.post") {
            api.issuesGetter.count.post(
                body = IssueCountResponseWrite(
                    query = issueSearch,
                    unresolvedOnly = false,
                )
            )
        }

        val assistQuery = assistQuery(issue)
        required("search.assist.post") {
            api.search.assist.post(
                body = SearchSuggestionsWrite(
                    query = assistQuery,
                    caret = assistQuery.length,
                    ignoreUnresolvedSetting = false,
                )
            )
        }

        optional("commands.assist.post") {
            api.commands.assist.post(
                body = CommandListWrite(
                    query = "State: ",
                    caret = "State: ".length,
                )
            )
        }

        val globalActivities = required("activities.get") {
            api.activities.get(
                issueQuery = issueSearch,
                reverse = true,
                top = env.top,
            )
        }.orEmpty()
        globalActivities.firstOrNull()?.id?.let { activityId ->
            required("activities.id($activityId).get") { api.activities.id(activityId).get() }
        } ?: skip("activities.id(<first>).get", "no activities returned")

        val globalActivityPage = required("activitiesPage.get") { api.activitiesPage.get(issueQuery = issueSearch) }
        globalActivityPage?.afterCursor?.let { cursor ->
            required("activitiesPage.get(cursor)") {
                api.activitiesPage.get(issueQuery = issueSearch, cursor = cursor)
            }
        } ?: skip("activitiesPage.get(cursor)", "no afterCursor returned")

        val workItems = required("workItems.get") { api.workItems.get(top = env.top) }.orEmpty()
        workItems.firstOrNull()?.id?.let { workItemId ->
            required("workItems.id($workItemId).get") { api.workItems.id(workItemId).get() }
        } ?: skip("workItems.id(<first>).get", "no work items returned")

        section("Folders")
        val savedQuery = savedQueries.firstOrNull() ?: userSavedQueries.firstOrNull()
        savedQuery?.id?.let { savedQueryId ->
            required("savedQueries.id($savedQueryId).get") { api.savedQueries.id(savedQueryId).get() }
        } ?: skip("savedQueries.id(<first>).get", "no saved queries returned")

        val tag = tags.firstOrNull() ?: userTags.firstOrNull()
        tag?.id?.let { tagId ->
            required("tags.id($tagId).get") { api.tags.id(tagId).get() }
            required("tags.id($tagId).issues.get") { api.tags.id(tagId).issues.get(top = env.top) }
        } ?: skip("tags.id(<first>).get", "no tags returned")

        issue?.let { probeIssue(it) } ?: skip("issue-scoped suite", "seed issue discovery failed")
        probeArticles()
        probeAgiles()

        if (env.includeAdmin) {
            probeAdmin()
        } else {
            skip("admin suite", "disabled; set YOUTRACK_INCLUDE_ADMIN=true to enable")
        }

        finish()
    }

    private suspend fun discoverIssue(): IssueRead {
        env.issueId?.let { issueId ->
            return api.issues.id(issueId).get()
        }

        val query = issueSearch(null)
        return api.issues.get(query = query, top = 1).firstOrNull()
            ?: error("No visible issue found for query: $query")
    }

    private suspend fun probeIssue(seed: IssueRead) {
        section("Issue")
        val issueId = idOrSkip("seed issue id", seed.id) ?: return

        required("issues.id($issueId).get") { api.issues.id(issueId).get() }

        val activities = required("issues.id($issueId).activities.get") {
            api.issues.id(issueId).activities.get(top = env.top, reverse = true)
        }.orEmpty()
        activities.firstOrNull()?.id?.let { activityId ->
            required("issues.id($issueId).activities.id($activityId).get") {
                api.issues.id(issueId).activities.activityItemId(activityId).get()
            }
        } ?: skip("issues.id($issueId).activities.id(<first>).get", "no issue activities returned")

        val activityPage = required("issues.id($issueId).activitiesPage.get") {
            api.issues.id(issueId).activitiesPage.get()
        }
        activityPage?.afterCursor?.let { cursor ->
            required("issues.id($issueId).activitiesPage.get(cursor)") {
                api.issues.id(issueId).activitiesPage.get(cursor = cursor)
            }
        } ?: skip("issues.id($issueId).activitiesPage.get(cursor)", "no issue activity cursor returned")

        val attachments = required("issues.id($issueId).attachments.get") {
            api.issues.id(issueId).attachments.get(top = env.top)
        }.orEmpty()
        attachments.firstOrNull()?.id?.let { attachmentId ->
            required("issues.id($issueId).attachments.id($attachmentId).get") {
                api.issues.id(issueId).attachments.issueAttachmentId(attachmentId).get()
            }
        } ?: skip("issues.id($issueId).attachments.id(<first>).get", "no attachments returned")

        val comments = required("issues.id($issueId).comments.get") {
            api.issues.id(issueId).comments.get(top = env.top)
        }.orEmpty()
        comments.firstOrNull()?.id?.let { commentId ->
            required("issues.id($issueId).comments.id($commentId).get") {
                api.issues.id(issueId).comments.issueCommentId(commentId).get()
            }

            val reactions = required("issues.id($issueId).comments.id($commentId).reactions.get") {
                api.issues.id(issueId).comments.issueCommentId(commentId).reactions.get(top = env.top)
            }.orEmpty()
            reactions.firstOrNull()?.id?.let { reactionId ->
                required("issues.id($issueId).comments.id($commentId).reactions.id($reactionId).get") {
                    api.issues.id(issueId).comments.issueCommentId(commentId).reactions.reactionId(reactionId).get()
                }
            } ?: skip(
                "issues.id($issueId).comments.id($commentId).reactions.id(<first>).get",
                "no reactions returned"
            )
        } ?: skip("issues.id($issueId).comments.id(<first>).get", "no comments returned")

        val customFields = required("issues.id($issueId).customFields.get") {
            api.issues.id(issueId).customFields.get(top = env.top)
        }.orEmpty()
        customFields.firstOrNull()?.id?.let { customFieldId ->
            required("issues.id($issueId).customFields.id($customFieldId).get") {
                api.issues.id(issueId).customFields.issueCustomFieldId(customFieldId).get()
            }
        } ?: skip("issues.id($issueId).customFields.id(<first>).get", "no custom fields returned")

        val links = required("issues.id($issueId).links.get") {
            api.issues.id(issueId).links.get(top = env.top)
        }.orEmpty()
        links.firstOrNull()?.id?.let { linkId ->
            required("issues.id($issueId).links.id($linkId).get") {
                api.issues.id(issueId).links.issueLinkId(linkId).get()
            }
            required("issues.id($issueId).links.id($linkId).issues.get") {
                api.issues.id(issueId).links.issueLinkId(linkId).issues.get(top = env.top)
            }
        } ?: skip("issues.id($issueId).links.id(<first>).get", "no issue links returned")

        required("issues.id($issueId).project.get") { api.issues.id(issueId).project.get() }
        required("issues.id($issueId).sprints.get") { api.issues.id(issueId).sprints.get(top = env.top) }

        val issueTags = required("issues.id($issueId).tags.get") {
            api.issues.id(issueId).tags.get(top = env.top)
        }.orEmpty()
        issueTags.firstOrNull()?.id?.let { tagId ->
            required("issues.id($issueId).tags.id($tagId).get") {
                api.issues.id(issueId).tags.tagId(tagId).get()
            }
        } ?: skip("issues.id($issueId).tags.id(<first>).get", "no issue tags returned")

        required("issues.id($issueId).timeTracking.get") { api.issues.id(issueId).timeTracking.get() }

        val issueWorkItems = required("issues.id($issueId).timeTracking.workItems.get") {
            api.issues.id(issueId).timeTracking.workItems.get(top = env.top)
        }.orEmpty()
        issueWorkItems.firstOrNull()?.id?.let { workItemId ->
            required("issues.id($issueId).timeTracking.workItems.id($workItemId).get") {
                api.issues.id(issueId).timeTracking.workItems.issueWorkItemId(workItemId).get()
            }
        } ?: skip("issues.id($issueId).timeTracking.workItems.id(<first>).get", "no issue work items returned")

        val vcsChanges = required("issues.id($issueId).vcsChanges.get") {
            api.issues.id(issueId).vcsChanges.get(top = env.top)
        }.orEmpty()
        vcsChanges.firstOrNull()?.id?.let { vcsChangeId ->
            required("issues.id($issueId).vcsChanges.id($vcsChangeId).get") {
                api.issues.id(issueId).vcsChanges.vcsChangeId(vcsChangeId).get()
            }
        } ?: skip("issues.id($issueId).vcsChanges.id(<first>).get", "no VCS changes returned")
    }

    private suspend fun probeArticles() {
        section("Articles")
        val articles = optional("articles.get") { api.articles.get(top = env.top) }.orEmpty()
        val article = articles.firstOrNull()
        val articleId = idOrSkip("articles.first.id", article?.id) ?: return

        required("articles.id($articleId).get") { api.articles.id(articleId).get() }

        val attachments = required("articles.id($articleId).attachments.get") {
            api.articles.id(articleId).attachments.get(top = env.top)
        }.orEmpty()
        attachments.firstOrNull()?.id?.let { attachmentId ->
            required("articles.id($articleId).attachments.id($attachmentId).get") {
                api.articles.id(articleId).attachments.articleAttachmentId(attachmentId).get()
            }
        } ?: skip("articles.id($articleId).attachments.id(<first>).get", "no article attachments returned")

        val childArticles = required("articles.id($articleId).childArticles.get") {
            api.articles.id(articleId).childArticles.get(top = env.top)
        }.orEmpty()
        childArticles.firstOrNull()?.id?.let { childArticleId ->
            required("articles.id($articleId).childArticles.id($childArticleId).get") {
                api.articles.id(articleId).childArticles.articleId(childArticleId).get()
            }
        } ?: skip("articles.id($articleId).childArticles.id(<first>).get", "no child articles returned")

        val comments = required("articles.id($articleId).comments.get") {
            api.articles.id(articleId).comments.get(top = env.top)
        }.orEmpty()
        comments.firstOrNull()?.id?.let { commentId ->
            required("articles.id($articleId).comments.id($commentId).get") {
                api.articles.id(articleId).comments.articleCommentId(commentId).get()
            }

            val reactions = required("articles.id($articleId).comments.id($commentId).reactions.get") {
                api.articles.id(articleId).comments.articleCommentId(commentId).reactions.get(top = env.top)
            }.orEmpty()
            reactions.firstOrNull()?.id?.let { reactionId ->
                required("articles.id($articleId).comments.id($commentId).reactions.id($reactionId).get") {
                    api.articles.id(articleId).comments.articleCommentId(commentId).reactions.reactionId(reactionId).get()
                }
            } ?: skip(
                "articles.id($articleId).comments.id($commentId).reactions.id(<first>).get",
                "no article comment reactions returned"
            )
        } ?: skip("articles.id($articleId).comments.id(<first>).get", "no article comments returned")

        if (article?.parentArticle?.id != null) {
            required("articles.id($articleId).parentArticle.get") { api.articles.id(articleId).parentArticle.get() }
        } else {
            skip("articles.id($articleId).parentArticle.get", "article has no parent article")
        }

        val tags = required("articles.id($articleId).tags.get") {
            api.articles.id(articleId).tags.get(top = env.top)
        }.orEmpty()
        tags.firstOrNull()?.id?.let { tagId ->
            required("articles.id($articleId).tags.id($tagId).get") {
                api.articles.id(articleId).tags.tagId(tagId).get()
            }
        } ?: skip("articles.id($articleId).tags.id(<first>).get", "no article tags returned")
    }

    private suspend fun probeAgiles() {
        section("Agiles")
        val agiles = optional("agiles.get") { api.agiles.get(top = env.top) }.orEmpty()
        val agile = agiles.firstOrNull()
        val agileId = idOrSkip("agiles.first.id", agile?.id) ?: return

        required("agiles.id($agileId).get") { api.agiles.id(agileId).get() }
        val sprints = required("agiles.id($agileId).sprints.get") { api.agiles.id(agileId).sprints.get(top = env.top) }.orEmpty()
        sprints.firstOrNull()?.id?.let { sprintId ->
            required("agiles.id($agileId).sprints.id($sprintId).get") {
                api.agiles.id(agileId).sprints.sprintId(sprintId).get()
            }
        } ?: skip("agiles.id($agileId).sprints.id(<first>).get", "no sprints returned")
    }

    private suspend fun probeAdmin() {
        section("Admin")
        required("admin.globalSettings.get") { api.admin.globalSettings.get() }
        required("admin.globalSettings.appearanceSettings.get") { api.admin.globalSettings.appearanceSettings.get() }
        required("admin.globalSettings.license.get") { api.admin.globalSettings.license.get() }
        required("admin.globalSettings.localeSettings.get") { api.admin.globalSettings.localeSettings.get() }
        required("admin.globalSettings.notificationSettings.get") { api.admin.globalSettings.notificationSettings.get() }
        required("admin.globalSettings.restSettings.get") { api.admin.globalSettings.restSettings.get() }
        required("admin.globalSettings.systemSettings.get") { api.admin.globalSettings.systemSettings.get() }

        required("admin.telemetry.get") { api.admin.telemetry.get() }

        required("admin.timeTrackingSettings.get") { api.admin.timeTrackingSettings.get() }
        val globalWorkItemTypes = required("admin.timeTrackingSettings.workItemTypes.get") {
            api.admin.timeTrackingSettings.workItemTypes.get(top = env.top)
        }.orEmpty()
        globalWorkItemTypes.firstOrNull()?.id?.let { workItemTypeId ->
            required("admin.timeTrackingSettings.workItemTypes.id($workItemTypeId).get") {
                api.admin.timeTrackingSettings.workItemTypes.workItemTypeId(workItemTypeId).get()
            }
        } ?: skip("admin.timeTrackingSettings.workItemTypes.id(<first>).get", "no global work item types returned")
        required("admin.timeTrackingSettings.workTimeSettings.get") { api.admin.timeTrackingSettings.workTimeSettings.get() }

        required("admin.customFieldSettings.types.get") { api.admin.customFieldSettings.types.get(top = env.top) }

        val customFields = required("admin.customFieldSettings.customFields.get") {
            api.admin.customFieldSettings.customFields.get(top = env.top)
        }.orEmpty()
        customFields.firstOrNull()?.id?.let { customFieldId ->
            required("admin.customFieldSettings.customFields.id($customFieldId).get") {
                api.admin.customFieldSettings.customFields.id(customFieldId).get()
            }
            required("admin.customFieldSettings.customFields.id($customFieldId).fieldDefaults.get") {
                api.admin.customFieldSettings.customFields.id(customFieldId).fieldDefaults.get()
            }
            required("admin.customFieldSettings.customFields.id($customFieldId).instances.get") {
                api.admin.customFieldSettings.customFields.id(customFieldId).instances.get(top = env.top)
            }
        } ?: skip("admin.customFieldSettings.customFields.id(<first>).get", "no admin custom fields returned")

        val enumBundles = required("admin.customFieldSettings.bundles.enum.get") {
            api.admin.customFieldSettings.bundles.`enum`.get(top = env.top)
        }.orEmpty()
        enumBundles.firstOrNull()?.id?.let { bundleId ->
            required("admin.customFieldSettings.bundles.enum.id($bundleId).get") {
                api.admin.customFieldSettings.bundles.`enum`.id(bundleId).get()
            }
            val values = required("admin.customFieldSettings.bundles.enum.id($bundleId).values.get") {
                api.admin.customFieldSettings.bundles.`enum`.id(bundleId).values.get(top = env.top)
            }.orEmpty()
            values.firstOrNull()?.id?.let { valueId ->
                required("admin.customFieldSettings.bundles.enum.id($bundleId).values.id($valueId).get") {
                    api.admin.customFieldSettings.bundles.`enum`.id(bundleId).values.enumBundleElementId(valueId).get()
                }
            } ?: skip(
                "admin.customFieldSettings.bundles.enum.id($bundleId).values.id(<first>).get",
                "no enum bundle values returned"
            )
        } ?: skip("admin.customFieldSettings.bundles.enum.id(<first>).get", "no enum bundles returned")

        val stateBundles = required("admin.customFieldSettings.bundles.state.get") {
            api.admin.customFieldSettings.bundles.state.get(top = env.top)
        }.orEmpty()
        stateBundles.firstOrNull()?.id?.let { bundleId ->
            required("admin.customFieldSettings.bundles.state.id($bundleId).get") {
                api.admin.customFieldSettings.bundles.state.id(bundleId).get()
            }
            val values = required("admin.customFieldSettings.bundles.state.id($bundleId).values.get") {
                api.admin.customFieldSettings.bundles.state.id(bundleId).values.get(top = env.top)
            }.orEmpty()
            values.firstOrNull()?.id?.let { valueId ->
                required("admin.customFieldSettings.bundles.state.id($bundleId).values.id($valueId).get") {
                    api.admin.customFieldSettings.bundles.state.id(bundleId).values.stateBundleElementId(valueId).get()
                }
            } ?: skip(
                "admin.customFieldSettings.bundles.state.id($bundleId).values.id(<first>).get",
                "no state bundle values returned"
            )
        } ?: skip("admin.customFieldSettings.bundles.state.id(<first>).get", "no state bundles returned")

        required("admin.databaseBackup.settings.get") { api.admin.databaseBackup.settings.get() }
        required("admin.databaseBackup.settings.backupStatus.get") { api.admin.databaseBackup.settings.backupStatus.get() }
        val backups = required("admin.databaseBackup.backups.get") {
            api.admin.databaseBackup.backups.get(top = env.top)
        }.orEmpty()
        backups.firstOrNull()?.id?.let { backupId ->
            required("admin.databaseBackup.backups.id($backupId).get") {
                api.admin.databaseBackup.backups.id(backupId).get()
            }
        } ?: skip("admin.databaseBackup.backups.id(<first>).get", "no database backups returned")

        val projects = required("admin.projects.get") { api.admin.projects.get(top = env.top) }.orEmpty()
        val projectId = idOrSkip("admin.projects.first.id", projects.firstOrNull()?.id) ?: return

        required("admin.projects.id($projectId).get") { api.admin.projects.id(projectId).get() }
        required("admin.projects.id($projectId).articles.get") { api.admin.projects.id(projectId).articles.get(top = env.top) }

        val projectCustomFields = required("admin.projects.id($projectId).customFields.get") {
            api.admin.projects.id(projectId).customFields.get(top = env.top)
        }.orEmpty()
        projectCustomFields.firstOrNull()?.id?.let { projectCustomFieldId ->
            required("admin.projects.id($projectId).customFields.id($projectCustomFieldId).get") {
                api.admin.projects.id(projectId).customFields.projectCustomFieldId(projectCustomFieldId).get()
            }
        } ?: skip(
            "admin.projects.id($projectId).customFields.id(<first>).get",
            "no project custom fields returned"
        )

        val projectIssues = required("admin.projects.id($projectId).issues.get") {
            api.admin.projects.id(projectId).issues.get(top = env.top)
        }.orEmpty()
        projectIssues.firstOrNull()?.id?.let { projectIssueId ->
            required("admin.projects.id($projectId).issues.id($projectIssueId).get") {
                api.admin.projects.id(projectId).issues.issueId(projectIssueId).get()
            }
        } ?: skip("admin.projects.id($projectId).issues.id(<first>).get", "no project issues returned")

        required("admin.projects.id($projectId).timeTrackingSettings.get") {
            api.admin.projects.id(projectId).timeTrackingSettings.get()
        }
        val projectWorkItemTypes = required("admin.projects.id($projectId).timeTrackingSettings.workItemTypes.get") {
            api.admin.projects.id(projectId).timeTrackingSettings.workItemTypes.get(top = env.top)
        }.orEmpty()
        projectWorkItemTypes.firstOrNull()?.id?.let { workItemTypeId ->
            required("admin.projects.id($projectId).timeTrackingSettings.workItemTypes.id($workItemTypeId).get") {
                api.admin.projects.id(projectId).timeTrackingSettings.workItemTypes.workItemTypeId(workItemTypeId).get()
            }
        } ?: skip(
            "admin.projects.id($projectId).timeTrackingSettings.workItemTypes.id(<first>).get",
            "no project work item types returned"
        )
    }

    private fun issueSearch(issue: IssueRead?): String =
        when {
            issue?.idReadable != null -> "id: ${issue.idReadable}"
            env.issueIdReadable != null -> "id: ${env.issueIdReadable}"
            else -> env.issueQuery
        }

    private fun assistQuery(issue: IssueRead?): String =
        issue?.project?.shortName?.let { "project: $it " } ?: "for: me "

    private suspend fun <T> required(name: String, block: suspend () -> T): T? {
        println("RUN  $name")
        return try {
            val value = block()
            passed += 1
            println("OK   $name -> ${describe(value)}")
            value
        } catch (t: Throwable) {
            val message = failureMessage(t)
            failures += "$name -> $message"
            println("FAIL $name -> $message")
            null
        }
    }

    private suspend fun <T> optional(name: String, block: suspend () -> T): T? {
        println("RUN  $name")
        return try {
            val value = block()
            passed += 1
            println("OK   $name -> ${describe(value)}")
            value
        } catch (t: Throwable) {
            skipped += 1
            println("SKIP $name -> ${failureMessage(t)}")
            null
        }
    }

    private fun section(name: String) {
        println()
        println("== $name ==")
    }

    private fun skip(name: String, reason: String) {
        skipped += 1
        println("SKIP $name -> $reason")
    }

    private fun idOrSkip(name: String, id: String?): String? {
        if (id == null) {
            skip(name, "resource id missing")
        }
        return id
    }

    private fun finish() {
        println()
        println("Smoke summary: passed=$passed skipped=$skipped failed=${failures.size}")
        if (failures.isNotEmpty()) {
            error(
                buildString {
                    appendLine("Smoke test failed:")
                    failures.forEach { appendLine(" - $it") }
                }
            )
        }
    }

    private fun describe(value: Any?): String =
        when (value) {
            null -> "null"
            is List<*> -> "size=${value.size}" + value.firstOrNull()?.let { ", first=${describe(it)}" }.orEmpty()
            is UserRead -> value.login ?: value.fullName ?: value.id ?: "UserRead"
            is IssueRead -> "${value.idReadable ?: value.id ?: "issue"} ${value.summary.short()}"
            is IssueFolderRead.Project -> value.shortName ?: value.name ?: value.id ?: "Project"
            is IssueFolderRead.Tag -> value.name ?: value.id ?: "Tag"
            is IssueFolderRead.SavedQuery -> value.name ?: value.id ?: "SavedQuery"
            is ActivityItem -> "${value.javaClass.simpleName}#${value.id ?: "?"}"
            is ActivityCursorPage -> "activities=${value.activities?.size ?: 0} after=${value.afterCursor != null} before=${value.beforeCursor != null}"
            is BaseWorkItemRead.IssueWorkItem -> "${value.id ?: "workItem"} ${value.type?.name.short()}"
            is AgileRead -> value.name ?: value.id ?: "Agile"
            is BaseArticleRead.Article -> "${value.idReadable ?: value.id ?: "article"} ${value.summary.short()}"
            is ProjectCustomFieldRead -> "${value.javaClass.simpleName}#${value.id ?: "?"}"
            is IssueCustomFieldRead -> "${value.javaClass.simpleName}#${value.id ?: "?"}"
            is CustomFieldRead -> value.name ?: value.id ?: "CustomFieldRead"
            is FieldTypeRead -> value.id ?: "FieldTypeRead"
            is IssueLink -> "${value.linkType?.name ?: "link"}#${value.id ?: "?"}"
            is IssueLinkTypeRead -> value.name ?: value.id ?: "IssueLinkTypeRead"
            is BundleRead -> "${value.javaClass.simpleName}#${value.id ?: "?"}"
            is GlobalSettingsRead -> value.systemSettings?.baseUrl ?: value.id ?: "GlobalSettingsRead"
            is IssueCountResponseRead -> "count=${value.count ?: 0}"
            is SearchSuggestionsRead -> "suggestions=${value.suggestions?.size ?: 0} query=${value.query.short()}"
            is CommandListRead -> "commands=${value.commands?.size ?: 0} suggestions=${value.suggestions?.size ?: 0}"
            is ProjectTimeTrackingSettingsRead -> "enabled=${value.enabled} workItemTypes=${value.workItemTypes?.size ?: 0}"
            is WorkItemTypeRead -> value.name ?: value.id ?: "WorkItemTypeRead"
            is WorkTimeSettingsRead -> "minutesADay=${value.minutesADay} days=${value.daysAWeek}"
            is Telemetry -> "processors=${value.availableProcessors} uptime=${value.uptime}"
            else -> value.javaClass.simpleName
        }

    private fun failureMessage(t: Throwable): String {
        val chain = generateSequence(t) { it.cause }.toList()
        return chain.joinToString(" -> ") { current ->
            current.javaClass.simpleName + (current.message?.let { ": ${it.short(120)}" } ?: "")
        }
    }
}

private fun String?.short(maxLength: Int = 60): String =
    when {
        this == null -> ""
        length <= maxLength -> this
        else -> take(maxLength - 3) + "..."
    }
