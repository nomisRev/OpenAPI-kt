package io.github.nomisrev.render.test.client.deep.nesting

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Long
import kotlin.String

public interface A {
  public val b: B

  public interface B {
    public fun c(c: String): CPath

    public interface CPath {
      public val d: D

      public interface D {
        public fun e(e: Long): EPath

        public interface EPath {
          public val f: F

          public interface F {
            public val `get`: Get

            public interface Get {
              public suspend operator fun invoke()
            }
          }
        }
      }
    }
  }
}

internal class KtorA(
  private val client: HttpClient,
) : A {
  override val b: A.B = KtorAB(client)
}

internal class KtorAB(
  private val client: HttpClient,
) : A.B {
  override fun c(c: String): A.B.CPath = KtorABCPath(client, c)
}

internal class KtorABCPath(
  private val client: HttpClient,
  private val c: String,
) : A.B.CPath {
  override val d: A.B.CPath.D = KtorABCPathD(client, c)
}

internal class KtorABCPathD(
  private val client: HttpClient,
  private val c: String,
) : A.B.CPath.D {
  override fun e(e: Long): A.B.CPath.D.EPath = KtorABCPathDEPath(client, c, e)
}

internal class KtorABCPathDEPath(
  private val client: HttpClient,
  private val c: String,
  private val e: Long,
) : A.B.CPath.D.EPath {
  override val f: A.B.CPath.D.EPath.F = KtorABCPathDEPathF(client, c, e)
}

internal class KtorABCPathDEPathF(
  private val client: HttpClient,
  private val c: String,
  private val e: Long,
) : A.B.CPath.D.EPath.F {
  override val `get`: A.B.CPath.D.EPath.F.Get = object : A.B.CPath.D.EPath.F.Get {
    override suspend operator fun invoke() {
      client.get("/a/b/$c/d/$e/f")
    }
  }
}
