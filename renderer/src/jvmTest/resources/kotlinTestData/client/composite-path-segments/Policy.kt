package io.github.nomisrev.render.test.client.composite.path.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Policy internal constructor(
  private val client: HttpClient,
) {
  public val agentPermissions: AgentPermissions = AgentPermissions(client)

  public class AgentPermissions internal constructor(
    private val client: HttpClient,
  ) {
    public val revisionId: RevisionId = RevisionId(client)

    public class RevisionId internal constructor(
      private val client: HttpClient,
    ) {
      public val `get`: Get = Get(client)

      public class Get internal constructor(
        private val client: HttpClient,
      ) {
        public suspend operator fun invoke() {
          val response = client.get("policy/agentPermissions")
          if (response.status.value != 204) throw ResponseException(response, "")
        }
      }
    }
  }
}
