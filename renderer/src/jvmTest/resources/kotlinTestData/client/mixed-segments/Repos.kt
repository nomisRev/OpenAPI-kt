package io.github.nomisrev.render.test.client.mixed.segments

import kotlin.String

public interface Repos {
  public fun owner(owner: String): Owner

  public interface Owner {
    public fun repo(repo: String): Repo

    public interface Repo {
      public val collaborators: Collaborators

      public interface Collaborators {
        public suspend fun `get`()
      }
    }
  }
}
