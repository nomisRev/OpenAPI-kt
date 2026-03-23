package io.youtrack.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Telemetry(
  public val id: String? = null,
  public val installationFolder: String? = null,
  public val databaseLocation: String? = null,
  public val logsLocation: String? = null,
  public val availableProcessors: Int? = null,
  public val availableMemory: String? = null,
  public val allocatedMemory: String? = null,
  public val usedMemory: String? = null,
  public val uptime: String? = null,
  public val startedTime: Long? = null,
  public val databaseBackgroundThreads: Int? = null,
  public val pendingAsyncJobs: Int? = null,
  @SerialName("cachedResultsCountInDBQueriesCache")
  public val cachedResultsCountInDbQueriesCache: Int? = null,
  public val databaseQueriesCacheHitRate: String? = null,
  public val blobStringsCacheHitRate: String? = null,
  public val totalTransactions: Long? = null,
  public val transactionsPerSecond: String? = null,
  public val requestsPerSecond: String? = null,
  public val databaseSize: String? = null,
  public val fullDatabaseSize: String? = null,
  public val textIndexSize: String? = null,
  public val onlineUsers: OnlineUsers? = null,
  public val reportCalculatorThreads: Int? = null,
  public val notificationAnalyzerThreads: Int? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
