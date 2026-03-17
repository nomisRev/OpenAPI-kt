package io.github.nomisrev.render.test.client.nested.tree

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.String

public interface Repos {
  public fun owner(owner: String): Owner

  public interface Owner {
    public fun repo(repo: String): Repo

    public interface Repo {
      public val `get`: Get

      public interface Get {
        public suspend operator fun invoke()
      }
    }
  }
}

internal class KtorRepos(
  private val client: HttpClient,
) : Repos {
  override fun owner(owner: String): Repos.Owner = KtorReposOwner(client, owner)
}

internal class KtorReposOwner(
  private val client: HttpClient,
  private val owner: String,
) : Repos.Owner {
  override fun repo(repo: String): Repos.Owner.Repo = KtorReposOwnerRepo(client, owner, repo)
}

internal class KtorReposOwnerRepo(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.Owner.Repo {
  override val `get`: Repos.Owner.Repo.Get = object : Repos.Owner.Repo.Get {
    override suspend operator fun invoke() {
      client.get("/repos/$owner/$repo")
    }
  }
}
