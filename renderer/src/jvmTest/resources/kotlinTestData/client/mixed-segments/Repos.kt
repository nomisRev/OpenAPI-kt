package io.github.nomisrev.render.test.client.mixed.segments

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.http.encodeURLParameter
import kotlin.String

public class Repos internal constructor(
  private val __client: HttpClient,
) {
  public fun owner(owner: String): OwnerPath = OwnerPath(__client, owner)

  public class OwnerPath internal constructor(
    private val __client: HttpClient,
    private val _owner: String,
  ) {
    public fun repo(repo: String): RepoPath = RepoPath(__client, _owner, repo)

    public class RepoPath internal constructor(
      private val __client: HttpClient,
      private val _owner: String,
      private val _repo: String,
    ) {
      public val collaborators: Collaborators = Collaborators(__client, _owner, _repo)

      public class Collaborators internal constructor(
        private val __client: HttpClient,
        private val _owner: String,
        private val _repo: String,
      ) {
        public val `get`: Get = Get(__client, _owner, _repo)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _owner: String,
          private val _repo: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.get("repos/${this._owner.encodeURLParameter()}/${this._repo.encodeURLParameter()}/collaborators")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }
      }
    }
  }
}
