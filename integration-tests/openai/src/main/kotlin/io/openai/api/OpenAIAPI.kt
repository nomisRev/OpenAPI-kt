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
  private val __client: HttpClient,
) : AutoCloseable {
  public val assistants: Assistants = Assistants(__client)

  public val audio: Audio = Audio(__client)

  public val batches: Batches = Batches(__client)

  public val chat: Chat = Chat(__client)

  public val completions: Completions = Completions(__client)

  public val containers: Containers = Containers(__client)

  public val conversations: Conversations = Conversations(__client)

  public val embeddings: Embeddings = Embeddings(__client)

  public val evals: Evals = Evals(__client)

  public val files: Files = Files(__client)

  public val fineTuning: FineTuning = FineTuning(__client)

  public val images: Images = Images(__client)

  public val models: Models = Models(__client)

  public val moderations: Moderations = Moderations(__client)

  public val organization: Organization = Organization(__client)

  public val projects: Projects = Projects(__client)

  public val realtime: Realtime = Realtime(__client)

  public val responses: Responses = Responses(__client)

  public val threads: Threads = Threads(__client)

  public val uploads: Uploads = Uploads(__client)

  public val vectorStores: VectorStores = VectorStores(__client)

  public val videos: Videos = Videos(__client)

  public val skills: Skills = Skills(__client)

  public val chatkit: Chatkit = Chatkit(__client)

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
