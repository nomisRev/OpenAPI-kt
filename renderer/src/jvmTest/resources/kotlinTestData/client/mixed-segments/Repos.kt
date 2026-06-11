package io.github.nomisrev.render.test.client.mixed.segments

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.String

public class Repos internal constructor(
  private val client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(client, owner)

  public class OwnerPath internal constructor(
    private val client: HttpClient,
    private val owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(client, owner, repo)

    public class RepoPath internal constructor(
      private val client: HttpClient,
      private val owner: String,
      private val repo: String,
    ) {
      public val collaborators: Collaborators = Collaborators(client, owner, repo)

      public class Collaborators internal constructor(
        private val client: HttpClient,
        private val owner: String,
        private val repo: String,
      ) {
        public val `get`: Get = Get(client, owner, repo)

        public class Get internal constructor(
          private val client: HttpClient,
          private val owner: String,
          private val repo: String,
        ) {
          public suspend operator fun invoke() {
            client.get("/repos/$owner/$repo/collaborators")
          }
        }
      }
    }
  }
}
