package io.github.nomisrev.render.test.client.nested.tree

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
