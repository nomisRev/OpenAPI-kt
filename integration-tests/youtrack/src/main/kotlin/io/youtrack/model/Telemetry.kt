package io.youtrack.model

import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Telemetry(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val installationFolder: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val databaseLocation: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logsLocation: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val availableProcessors: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val availableMemory: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allocatedMemory: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usedMemory: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val uptime: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val startedTime: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val databaseBackgroundThreads: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pendingAsyncJobs: Int? = null,
  @SerialName("cachedResultsCountInDBQueriesCache")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cachedResultsCountInDbQueriesCache: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val databaseQueriesCacheHitRate: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val blobStringsCacheHitRate: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val totalTransactions: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val transactionsPerSecond: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requestsPerSecond: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val databaseSize: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fullDatabaseSize: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textIndexSize: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val onlineUsers: OnlineUsers? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reportCalculatorThreads: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val notificationAnalyzerThreads: Int? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
