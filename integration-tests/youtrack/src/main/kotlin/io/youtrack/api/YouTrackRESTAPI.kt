package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class YouTrackRESTAPI internal constructor(
  private val __client: HttpClient,
) : AutoCloseable {
  public val activities: Activities = Activities(__client)

  public val activitiesPage: ActivitiesPage = ActivitiesPage(__client)

  public val admin: Admin = Admin(__client)

  public val agiles: Agiles = Agiles(__client)

  public val articles: Articles = Articles(__client)

  public val commands: Commands = Commands(__client)

  public val groups: Groups = Groups(__client)

  public val issueLinkTypes: IssueLinkTypes = IssueLinkTypes(__client)

  public val issues: Issues = Issues(__client)

  public val issuesGetter: IssuesGetter = IssuesGetter(__client)

  public val savedQueries: SavedQueries = SavedQueries(__client)

  public val search: Search = Search(__client)

  public val tags: Tags = Tags(__client)

  public val users: Users = Users(__client)

  public val workItems: WorkItems = WorkItems(__client)

  public constructor(baseUrl: String, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    block()
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  public constructor(baseUrl: String) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    install(ContentNegotiation) { json() }
    // Ktor appends a relative path to the base URL only when its path ends with `/`.
    defaultRequest { url { if (!encodedPath.endsWith("/")) encodedPath += "/" } }
  }
  )

  override fun close() {
    __client.close()
  }
}
