package io.openai.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlin.AutoCloseable
import kotlin.String
import kotlin.Unit

public class OpenAIAPI internal constructor(
  private val client: HttpClient,
) : AutoCloseable {
  public val assistants: Assistants = Assistants(client)

  public val audio: Audio = Audio(client)

  public val batches: Batches = Batches(client)

  public val chat: Chat = Chat(client)

  public val completions: Completions = Completions(client)

  public val containers: Containers = Containers(client)

  public val conversations: Conversations = Conversations(client)

  public val embeddings: Embeddings = Embeddings(client)

  public val evals: Evals = Evals(client)

  public val files: Files = Files(client)

  public val fineTuning: FineTuning = FineTuning(client)

  public val images: Images = Images(client)

  public val models: Models = Models(client)

  public val moderations: Moderations = Moderations(client)

  public val organization: Organization = Organization(client)

  public val projects: Projects = Projects(client)

  public val realtime: Realtime = Realtime(client)

  public val responses: Responses = Responses(client)

  public val threads: Threads = Threads(client)

  public val uploads: Uploads = Uploads(client)

  public val vectorStores: VectorStores = VectorStores(client)

  public val videos: Videos = Videos(client)

  public val skills: Skills = Skills(client)

  public val chatkit: Chatkit = Chatkit(client)

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
    client.close()
  }
}
