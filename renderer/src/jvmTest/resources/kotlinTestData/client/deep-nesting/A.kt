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
  override fun c(c: String): A.B.C = KtorABC(client, c)
}

internal class KtorABC(
  private val client: HttpClient,
  private val c: String,
) : A.B.C {
  override val d: A.B.C.D = KtorABCD(client, c)
}

internal class KtorABCD(
  private val client: HttpClient,
  private val c: String,
) : A.B.C.D {
  override fun e(e: Long): A.B.C.D.E = KtorABCDE(client, c, e)
}

internal class KtorABCDE(
  private val client: HttpClient,
  private val c: String,
  private val e: Long,
) : A.B.C.D.E {
  override val f: A.B.C.D.E.F = KtorABCDEF(client, c, e)
}

internal class KtorABCDEF(
  private val client: HttpClient,
  private val c: String,
  private val e: Long,
) : A.B.C.D.E.F {
  override val `get`: A.B.C.D.E.F.Get = object : A.B.C.D.E.F.Get {
    override suspend operator fun invoke() {
      client.get("/a/b/$c/d/$e/f")
    }
  }
}
