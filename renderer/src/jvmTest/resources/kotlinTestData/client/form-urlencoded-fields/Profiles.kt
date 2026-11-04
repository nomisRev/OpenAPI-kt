package io.github.nomisrev.render.test.client.form.urlencoded.fields

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class Profiles internal constructor(
  private val client: HttpClient,
) {
  public val post: Post = Post(client)

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    @OptIn(ExperimentalUuidApi::class)
    public suspend operator fun invoke(
      name: String,
      nickname: String? = null,
      count: Int,
      age: Long? = null,
      ratio: Double,
      active: Boolean? = null,
      role: Body.Role,
      status: Status? = null,
      born: LocalDate? = null,
      seen: Instant? = null,
      id: Uuid? = null,
      code: Code? = null,
      tags: List<String>? = null,
      roles: List<Body.Roles>,
      ids: List<Long>? = null,
      words: List<String>? = null,
      path: List<String>? = null,
      flags: List<Boolean>? = null,
    ) {
      val response = client.post("/profiles") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(Parameters.build {
          append("name", name)
          if (nickname != null) {
            append("nickname", nickname)
          }
          append("count", count.toString())
          if (age != null) {
            append("age", age.toString())
          }
          append("ratio", ratio.toString())
          if (active != null) {
            append("active", active.toString())
          }
          append("role", role.value)
          if (status != null) {
            append("status", status.toString())
          }
          if (born != null) {
            append("born", born.toString())
          }
          if (seen != null) {
            append("seen", seen.toString())
          }
          if (id != null) {
            append("id", id.toString())
          }
          if (code != null) {
            append("code", code.value)
          }
          if (tags != null) {
            appendAll("tags", tags)
          }
          appendAll("roles", roles.map { it.value })
          if (!ids.isNullOrEmpty()) {
            append("ids", ids.joinToString(","))
          }
          if (!words.isNullOrEmpty()) {
            append("words", words.joinToString(" "))
          }
          if (!path.isNullOrEmpty()) {
            append("path", path.joinToString("|"))
          }
          if (flags != null) {
            appendAll("flags", flags.map { it.toString() })
          }
        }.formUrlEncode())
      }
      if (response.status.value != 204) throw ResponseException(response, "")
    }

    public object Body {
      @Serializable
      public enum class Role(
        public val `value`: String,
      ) {
        @SerialName("admin")
        Admin("admin"),
        @SerialName("read-only")
        ReadOnly("read-only"),
        ;
      }

      @Serializable
      public enum class Roles(
        public val `value`: String,
      ) {
        @SerialName("admin")
        Admin("admin"),
        @SerialName("read-only")
        ReadOnly("read-only"),
        ;
      }
    }
  }
}
