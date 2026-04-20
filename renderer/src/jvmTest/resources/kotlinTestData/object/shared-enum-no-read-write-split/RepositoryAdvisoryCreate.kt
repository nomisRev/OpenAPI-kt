package io.github.nomisrev.render.test.object_.shared.enum.no.read.write.split

import kotlinx.serialization.Serializable

@Serializable
public data class RepositoryAdvisoryCreate(
  public val ecosystem: SecurityAdvisoryEcosystems,
)
