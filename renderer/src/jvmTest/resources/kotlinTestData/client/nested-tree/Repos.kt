package io.github.nomisrev.render.test.client.nested.tree

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.String

public interface Repos {
  public fun owner(owner: String): Owner

  public interface Owner {
    public fun repo(repo: String): Repo

    public interface Repo {
      public suspend fun `get`()
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
  override suspend fun `get`() {
    client.get("/repos/$owner/$repo")
  }
}
