package io.github.nomisrev.render.test.client.optional.nullable.variants

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`

public class Things internal constructor(
  private val client: HttpClient,
) {
  public val extended: Extended = Extended(client)

  public val pet: Pet = Pet(client)

  public val withadditional: Withadditional = Withadditional(client)

  public val account: Account = Account(client)

  public class Extended internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): io.github.nomisrev.render.test.client.optional.nullable.variants.Extended {
        val response = client.get("things/extended")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Pet internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): io.github.nomisrev.render.test.client.optional.nullable.variants.Pet {
        val response = client.get("things/pet")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Withadditional internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): WithAdditional {
        val response = client.get("things/withadditional")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class Account internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(): io.github.nomisrev.render.test.client.optional.nullable.variants.Account {
        val response = client.get("things/account")
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }
}
