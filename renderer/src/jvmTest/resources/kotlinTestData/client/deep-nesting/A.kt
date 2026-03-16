package io.github.nomisrev.render.test.client.deep.nesting

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
