package io.github.nomisrev.render.test.client.deep.nesting

import io.ktor.client.HttpClient
import io.ktor.client.request.`get`
import kotlin.Long
import kotlin.String

public interface A {
  public val b: B

  public interface B {
    public fun c(c: String): C

    public interface C {
      public val d: D

      public interface D {
        public fun e(e: Long): E

        public interface E {
          public val f: F

          public interface F {
            public suspend fun `get`()
          }
        }
      }
    }
  }
}

internal class KtorA(
  private val client: HttpClient,
) : A {
  override val b: A.B = KtorB(client)
}

internal class KtorB(
  private val client: HttpClient,
) : A.B {
  override fun c(c: String): A.B.C = KtorC(client, c)
}

internal class KtorC(
  private val client: HttpClient,
  private val c: String,
) : A.B.C {
  override val d: A.B.C.D = KtorD(client, c)
}

internal class KtorD(
  private val client: HttpClient,
  private val c: String,
) : A.B.C.D {
  override fun e(e: Long): A.B.C.D.E = KtorE(client, c, e)
}

internal class KtorE(
  private val client: HttpClient,
  private val c: String,
  private val e: Long,
) : A.B.C.D.E {
  override val f: A.B.C.D.E.F = KtorF(client, c, e)
}

internal class KtorF(
  private val client: HttpClient,
  private val c: String,
  private val e: Long,
) : A.B.C.D.E.F {
  override suspend fun `get`() {
    client.get("/a/b/$c/d/$e/f")
  }
}
