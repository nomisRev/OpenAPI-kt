package io.github.api

import io.github.model.Root
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.`get`
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class GitHubV3RESTAPI internal constructor(
  private val client: HttpClient,
) : AutoCloseable {
  public val `get`: Get = Get(client)

  public val advisories: Advisories = Advisories(client)

  public val app: App = App(client)

  public val appManifests: AppManifests = AppManifests(client)

  public val applications: Applications = Applications(client)

  public val apps: Apps = Apps(client)

  public val assignments: Assignments = Assignments(client)

  public val classrooms: Classrooms = Classrooms(client)

  public val codesOfConduct: CodesOfConduct = CodesOfConduct(client)

  public val credentials: Credentials = Credentials(client)

  public val emojis: Emojis = Emojis(client)

  public val enterprises: Enterprises = Enterprises(client)

  public val events: Events = Events(client)

  public val feeds: Feeds = Feeds(client)

  public val gists: Gists = Gists(client)

  public val gitignore: Gitignore = Gitignore(client)

  public val installation: Installation = Installation(client)

  public val issues: Issues = Issues(client)

  public val licenses: Licenses = Licenses(client)

  public val markdown: Markdown = Markdown(client)

  public val marketplaceListing: MarketplaceListing = MarketplaceListing(client)

  public val meta: Meta = Meta(client)

  public val networks: Networks = Networks(client)

  public val notifications: Notifications = Notifications(client)

  public val octocat: Octocat = Octocat(client)

  public val organizations: Organizations = Organizations(client)

  public val orgs: Orgs = Orgs(client)

  public val rateLimit: RateLimit = RateLimit(client)

  public val repos: Repos = Repos(client)

  public val repositories: Repositories = Repositories(client)

  public val search: Search = Search(client)

  public val teams: Teams = Teams(client)

  public val user: User = User(client)

  public val users: Users = Users(client)

  public val versions: Versions = Versions(client)

  public val zen: Zen = Zen(client)

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

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(): Root = client.get("/").body()
  }
}
