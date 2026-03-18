package io.github.nomisrev.render.test.client.mixed.segments

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.String

public interface Repos {
  public fun owner(owner: String): OwnerPath

  public interface OwnerPath {
    public fun repo(repo: String): RepoPath

    public interface RepoPath {
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
  override fun owner(owner: String): Repos.OwnerPath = KtorReposOwnerPath(client, owner)
}

internal class KtorReposOwnerPath(
  private val client: HttpClient,
  private val owner: String,
) : Repos.OwnerPath {
  override fun repo(repo: String): Repos.OwnerPath.RepoPath = KtorReposOwnerPathRepoPath(client, owner, repo)
}

internal class KtorReposOwnerPathRepoPath(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.OwnerPath.RepoPath {
  override val collaborators: Repos.OwnerPath.RepoPath.Collaborators =
      KtorReposOwnerPathRepoPathCollaborators(client, owner, repo)
}

internal class KtorReposOwnerPathRepoPathCollaborators(
  private val client: HttpClient,
  private val owner: String,
  private val repo: String,
) : Repos.OwnerPath.RepoPath.Collaborators {
  override val `get`: Repos.OwnerPath.RepoPath.Collaborators.Get =
      object : Repos.OwnerPath.RepoPath.Collaborators.Get {
    override suspend operator fun invoke() {
      client.get("/repos/$owner/$repo/collaborators")
    }
  }
}
