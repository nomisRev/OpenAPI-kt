package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class YouTrackRESTAPI internal constructor(
  private val client: HttpClient,
) : AutoCloseable {
  public val activities: Activities = Activities(client)

  public val activitiesPage: ActivitiesPage = ActivitiesPage(client)

  public val admin: Admin = Admin(client)

  public val agiles: Agiles = Agiles(client)

  public val articles: Articles = Articles(client)

  public val commands: Commands = Commands(client)

  public val groups: Groups = Groups(client)

  public val issueLinkTypes: IssueLinkTypes = IssueLinkTypes(client)

  public val issues: Issues = Issues(client)

  public val issuesGetter: IssuesGetter = IssuesGetter(client)

  public val savedQueries: SavedQueries = SavedQueries(client)

  public val search: Search = Search(client)

  public val tags: Tags = Tags(client)

  public val users: Users = Users(client)

  public val workItems: WorkItems = WorkItems(client)

  public constructor(baseUrl: String, block: HttpClientConfig<*>.() -> Unit) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    block()
  }
  )

  public constructor(baseUrl: String) : this(HttpClient {
    defaultRequest { url(baseUrl) }
    install(ContentNegotiation) { json() }
  }
  )

  override fun close() {
    client.close()
  }
}
