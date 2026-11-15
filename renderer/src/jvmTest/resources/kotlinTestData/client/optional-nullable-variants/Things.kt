package io.github.nomisrev.render.test.client.optional.nullable.variants

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Things internal constructor(
  private val __client: HttpClient,
) {
  public val extended: Extended = Extended(__client)

  public val pet: Pet = Pet(__client)

  public val withadditional: Withadditional = Withadditional(__client)

  public val account: Account = Account(__client)

  public class Extended internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(): io.github.nomisrev.render.test.client.optional.nullable.variants.Extended {
        val response = __client.get("things/extended")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Pet internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(): io.github.nomisrev.render.test.client.optional.nullable.variants.Pet {
        val response = __client.get("things/pet")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Withadditional internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(): WithAdditional {
        val response = __client.get("things/withadditional")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Account internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(): io.github.nomisrev.render.test.client.optional.nullable.variants.Account {
        val response = __client.get("things/account")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
