package io.github.api

import io.github.model.Root
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.`get`
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class GitHubV3RESTAPI internal constructor(
  private val __client: HttpClient,
) : AutoCloseable {
  public val `get`: Get = Get(__client)

  public val advisories: Advisories = Advisories(__client)

  public val app: App = App(__client)

  public val appManifests: AppManifests = AppManifests(__client)

  public val applications: Applications = Applications(__client)

  public val apps: Apps = Apps(__client)

  public val assignments: Assignments = Assignments(__client)

  public val classrooms: Classrooms = Classrooms(__client)

  public val codesOfConduct: CodesOfConduct = CodesOfConduct(__client)

  public val credentials: Credentials = Credentials(__client)

  public val emojis: Emojis = Emojis(__client)

  public val enterprises: Enterprises = Enterprises(__client)

  public val events: Events = Events(__client)

  public val feeds: Feeds = Feeds(__client)

  public val gists: Gists = Gists(__client)

  public val gitignore: Gitignore = Gitignore(__client)

  public val installation: Installation = Installation(__client)

  public val issues: Issues = Issues(__client)

  public val licenses: Licenses = Licenses(__client)

  public val markdown: Markdown = Markdown(__client)

  public val marketplaceListing: MarketplaceListing = MarketplaceListing(__client)

  public val meta: Meta = Meta(__client)

  public val networks: Networks = Networks(__client)

  public val notifications: Notifications = Notifications(__client)

  public val octocat: Octocat = Octocat(__client)

  public val organizations: Organizations = Organizations(__client)

  public val orgs: Orgs = Orgs(__client)

  public val rateLimit: RateLimit = RateLimit(__client)

  public val repos: Repos = Repos(__client)

  public val repositories: Repositories = Repositories(__client)

  public val search: Search = Search(__client)

  public val teams: Teams = Teams(__client)

  public val user: User = User(__client)

  public val users: Users = Users(__client)

  public val versions: Versions = Versions(__client)

  public val zen: Zen = Zen(__client)

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

  public class Get internal constructor(
    private val __client: HttpClient,
  ) {
    public suspend operator fun invoke(): Root {
      val response = __client.get("")
      if (response.status.value != 200) throw ResponseException(response, "")
      return response.body()
    }
  }
}
