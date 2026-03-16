package io.github.nomisrev.render.test.client.mixed.segments

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.String

public interface Repos {
  public fun owner(owner: String): Owner

  public interface Owner {
    public fun repo(repo: String): Repo

    public interface Repo {
      public val collaborators: Collaborators

      public interface Collaborators {
        public val `get`: Get

        public interface Get {
          public suspend operator fun invoke()
        }
      }
    }
  }
}

internal class KtorRepos(
  private val client: HttpClient,
) : Repos {
  override fun owner(owner: String): Repos.Owner = KtorOwner(client, owner)
}

internal class KtorOwner(
  private val client: HttpClient,
  private val owner: String,
) : Repos.Owner {
  override fun repo(repo: String): Repos.Owner.Repo = KtorRepo(client, owner, repo)
}

internal class KtorRepo(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.Owner.Repo {
  override val collaborators: Repos.Owner.Repo.Collaborators =
      KtorCollaborators(client, owner, repo)
}

internal class KtorCollaborators(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.Owner.Repo.Collaborators {
  override val `get`: Repos.Owner.Repo.Collaborators.Get =
      object : Repos.Owner.Repo.Collaborators.Get {
    override suspend operator fun invoke() {
      client.get("/repos/$owner/$repo/collaborators")
    }
  }
}
