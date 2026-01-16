package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TelemetryResponse(
    val id: String? = null,
    val installationFolder: String? = null,
    val databaseLocation: String? = null,
    val logsLocation: String? = null,
    val availableProcessors: Int? = null,
    val availableMemory: String? = null,
    val allocatedMemory: String? = null,
    val usedMemory: String? = null,
    val uptime: String? = null,
    val startedTime: Long? = null,
    val databaseBackgroundThreads: Int? = null,
    val pendingAsyncJobs: Int? = null,
    val cachedResultsCountInDBQueriesCache: Int? = null,
    val databaseQueriesCacheHitRate: String? = null,
    val blobStringsCacheHitRate: String? = null,
    val totalTransactions: Long? = null,
    val transactionsPerSecond: String? = null,
    val requestsPerSecond: String? = null,
    val databaseSize: String? = null,
    val fullDatabaseSize: String? = null,
    val textIndexSize: String? = null,
    val onlineUsers: OnlineUsersResponse? = null,
    val reportCalculatorThreads: Int? = null,
    val notificationAnalyzerThreads: Int? = null,
    @SerialName($$"$type") val type: String? = null,
)
