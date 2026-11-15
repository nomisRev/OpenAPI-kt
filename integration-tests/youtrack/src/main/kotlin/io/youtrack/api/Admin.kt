package io.youtrack.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.youtrack.model.AppearanceSettingsRead
import io.youtrack.model.AppearanceSettingsWrite
import io.youtrack.model.BackupFile
import io.youtrack.model.BaseArticleRead
import io.youtrack.model.BundleElementRead
import io.youtrack.model.BundleElementWrite
import io.youtrack.model.BundleRead
import io.youtrack.model.BundleWrite
import io.youtrack.model.CustomFieldDefaultsRead
import io.youtrack.model.CustomFieldDefaultsWrite
import io.youtrack.model.CustomFieldRead
import io.youtrack.model.CustomFieldWrite
import io.youtrack.model.DatabaseBackupSettingsRead
import io.youtrack.model.DatabaseBackupSettingsWrite
import io.youtrack.model.FieldTypeRead
import io.youtrack.model.GlobalSettingsRead
import io.youtrack.model.GlobalSettingsWrite
import io.youtrack.model.GlobalTimeTrackingSettings
import io.youtrack.model.IssueFolderRead
import io.youtrack.model.IssueFolderWrite
import io.youtrack.model.IssueRead
import io.youtrack.model.IssueWrite
import io.youtrack.model.LicenseRead
import io.youtrack.model.LicenseWrite
import io.youtrack.model.LocaleSettingsRead
import io.youtrack.model.LocaleSettingsWrite
import io.youtrack.model.NotificationSettingsRead
import io.youtrack.model.NotificationSettingsWrite
import io.youtrack.model.ProjectCustomFieldRead
import io.youtrack.model.ProjectCustomFieldWrite
import io.youtrack.model.ProjectTimeTrackingSettingsRead
import io.youtrack.model.ProjectTimeTrackingSettingsWrite
import io.youtrack.model.RestCorsSettingsRead
import io.youtrack.model.RestCorsSettingsWrite
import io.youtrack.model.SystemSettingsRead
import io.youtrack.model.SystemSettingsWrite
import io.youtrack.model.UserGroupRead
import io.youtrack.model.UserGroupWrite
import io.youtrack.model.UserRead
import io.youtrack.model.UserWrite
import io.youtrack.model.WorkItemTypeRead
import io.youtrack.model.WorkItemTypeWrite
import io.youtrack.model.WorkTimeSettingsRead
import io.youtrack.model.WorkTimeSettingsWrite
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class Admin internal constructor(
  private val __client: HttpClient,
) {
  public val customFieldSettings: CustomFieldSettings = CustomFieldSettings(__client)

  public val databaseBackup: DatabaseBackup = DatabaseBackup(__client)

  public val globalSettings: GlobalSettings = GlobalSettings(__client)

  public val projects: Projects = Projects(__client)

  public val telemetry: Telemetry = Telemetry(__client)

  public val timeTrackingSettings: TimeTrackingSettings = TimeTrackingSettings(__client)

  public class CustomFieldSettings internal constructor(
    private val __client: HttpClient,
  ) {
    public val bundles: Bundles = Bundles(__client)

    public val customFields: CustomFields = CustomFields(__client)

    public val types: Types = Types(__client)

    public class Bundles internal constructor(
      private val __client: HttpClient,
    ) {
      public val build: Build = Build(__client)

      public val `enum`: Enum = Enum(__client)

      public val ownedField: OwnedField = OwnedField(__client)

      public val state: State = State(__client)

      public val user: User = User(__client)

      public val version: Version = Version(__client)

      public class Build internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public val post: Post = Post(__client)

        public fun id(id: String): IdPath = IdPath(__client, id)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.BuildBundle> {
            val response = __client.get("admin/customFieldSettings/bundles/build") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.BuildBundle? = null): BundleRead.BuildBundle {
            val response = __client.post("admin/customFieldSettings/bundles/build") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val delete: Delete = Delete(__client, _id)

          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public val values: Values = Values(__client, _id)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.BuildBundle {
              val response = __client.get("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.BuildBundle? = null): BundleRead.BuildBundle {
              val response = __client.post("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Values internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun buildBundleElementId(buildBundleElementId: String): BuildBundleElementIdPath = BuildBundleElementIdPath(__client, _id, buildBundleElementId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.BuildBundleElement> {
                val response = __client.get("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal", body: BundleElementWrite.BuildBundleElement? = null): BundleElementRead.BuildBundleElement {
                val response = __client.post("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class BuildBundleElementIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _buildBundleElementId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _buildBundleElementId)

              public val `get`: Get = Get(__client, _id, _buildBundleElementId)

              public val post: Post = Post(__client, _id, _buildBundleElementId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _buildBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}/values/${this._buildBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _buildBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal"): BundleElementRead.BuildBundleElement {
                  val response = __client.get("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}/values/${this._buildBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _buildBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,assembleDate,color(${'$'}type,background,foreground,id),id,name,ordinal", body: BundleElementWrite.BuildBundleElement? = null): BundleElementRead.BuildBundleElement {
                  val response = __client.post("admin/customFieldSettings/bundles/build/${this._id.encodeURLParameter()}/values/${this._buildBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                    body?.let {
                      contentType(ContentType.Application.Json)
                      setBody(it)
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }

      public class Enum internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public val post: Post = Post(__client)

        public fun id(id: String): IdPath = IdPath(__client, id)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.EnumBundle> {
            val response = __client.get("admin/customFieldSettings/bundles/enum") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.EnumBundle? = null): BundleRead.EnumBundle {
            val response = __client.post("admin/customFieldSettings/bundles/enum") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val delete: Delete = Delete(__client, _id)

          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public val values: Values = Values(__client, _id)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.EnumBundle {
              val response = __client.get("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.EnumBundle? = null): BundleRead.EnumBundle {
              val response = __client.post("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Values internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun enumBundleElementId(enumBundleElementId: String): EnumBundleElementIdPath = EnumBundleElementIdPath(__client, _id, enumBundleElementId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.EnumBundleElement> {
                val response = __client.get("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal", body: BundleElementWrite.EnumBundleElement? = null): BundleElementRead.EnumBundleElement {
                val response = __client.post("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class EnumBundleElementIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _enumBundleElementId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _enumBundleElementId)

              public val `get`: Get = Get(__client, _id, _enumBundleElementId)

              public val post: Post = Post(__client, _id, _enumBundleElementId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _enumBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}/values/${this._enumBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _enumBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal"): BundleElementRead.EnumBundleElement {
                  val response = __client.get("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}/values/${this._enumBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _enumBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,localizedName,name,ordinal", body: BundleElementWrite.EnumBundleElement? = null): BundleElementRead.EnumBundleElement {
                  val response = __client.post("admin/customFieldSettings/bundles/enum/${this._id.encodeURLParameter()}/values/${this._enumBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                    body?.let {
                      contentType(ContentType.Application.Json)
                      setBody(it)
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }

      public class OwnedField internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public val post: Post = Post(__client)

        public fun id(id: String): IdPath = IdPath(__client, id)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.OwnedBundle> {
            val response = __client.get("admin/customFieldSettings/bundles/ownedField") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.OwnedBundle? = null): BundleRead.OwnedBundle {
            val response = __client.post("admin/customFieldSettings/bundles/ownedField") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val delete: Delete = Delete(__client, _id)

          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public val values: Values = Values(__client, _id)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.OwnedBundle {
              val response = __client.get("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.OwnedBundle? = null): BundleRead.OwnedBundle {
              val response = __client.post("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Values internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun ownedBundleElementId(ownedBundleElementId: String): OwnedBundleElementIdPath = OwnedBundleElementIdPath(__client, _id, ownedBundleElementId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.OwnedBundleElement> {
                val response = __client.get("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)", body: BundleElementWrite.OwnedBundleElement? = null): BundleElementRead.OwnedBundleElement {
                val response = __client.post("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class OwnedBundleElementIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _ownedBundleElementId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _ownedBundleElementId)

              public val `get`: Get = Get(__client, _id, _ownedBundleElementId)

              public val post: Post = Post(__client, _id, _ownedBundleElementId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}/values/${this._ownedBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)"): BundleElementRead.OwnedBundleElement {
                  val response = __client.get("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}/values/${this._ownedBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _ownedBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,name,ordinal,owner(${'$'}type,id,login,ringId)", body: BundleElementWrite.OwnedBundleElement? = null): BundleElementRead.OwnedBundleElement {
                  val response = __client.post("admin/customFieldSettings/bundles/ownedField/${this._id.encodeURLParameter()}/values/${this._ownedBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                    body?.let {
                      contentType(ContentType.Application.Json)
                      setBody(it)
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }

      public class State internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public val post: Post = Post(__client)

        public fun id(id: String): IdPath = IdPath(__client, id)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.StateBundle> {
            val response = __client.get("admin/customFieldSettings/bundles/state") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.StateBundle? = null): BundleRead.StateBundle {
            val response = __client.post("admin/customFieldSettings/bundles/state") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val delete: Delete = Delete(__client, _id)

          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public val values: Values = Values(__client, _id)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.StateBundle {
              val response = __client.get("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.StateBundle? = null): BundleRead.StateBundle {
              val response = __client.post("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Values internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun stateBundleElementId(stateBundleElementId: String): StateBundleElementIdPath = StateBundleElementIdPath(__client, _id, stateBundleElementId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.StateBundleElement> {
                val response = __client.get("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal", body: BundleElementWrite.StateBundleElement? = null): BundleElementRead.StateBundleElement {
                val response = __client.post("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class StateBundleElementIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _stateBundleElementId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _stateBundleElementId)

              public val `get`: Get = Get(__client, _id, _stateBundleElementId)

              public val post: Post = Post(__client, _id, _stateBundleElementId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _stateBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}/values/${this._stateBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _stateBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal"): BundleElementRead.StateBundleElement {
                  val response = __client.get("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}/values/${this._stateBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _stateBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,color(${'$'}type,background,foreground,id),id,isResolved,localizedName,name,ordinal", body: BundleElementWrite.StateBundleElement? = null): BundleElementRead.StateBundleElement {
                  val response = __client.post("admin/customFieldSettings/bundles/state/${this._id.encodeURLParameter()}/values/${this._stateBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                    body?.let {
                      contentType(ContentType.Application.Json)
                      setBody(it)
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }

      public class User internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public val post: Post = Post(__client)

        public fun id(id: String): IdPath = IdPath(__client, id)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.UserBundle> {
            val response = __client.get("admin/customFieldSettings/bundles/user") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.UserBundle? = null): BundleRead.UserBundle {
            val response = __client.post("admin/customFieldSettings/bundles/user") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val delete: Delete = Delete(__client, _id)

          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public val aggregatedUsers: AggregatedUsers = AggregatedUsers(__client, _id)

          public val groups: Groups = Groups(__client, _id)

          public val individuals: Individuals = Individuals(__client, _id)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.UserBundle {
              val response = __client.get("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.UserBundle? = null): BundleRead.UserBundle {
              val response = __client.post("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class AggregatedUsers internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId",
                skip: Int? = null,
                top: Int? = null,
              ): List<UserRead> {
                val response = __client.get("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/aggregatedUsers") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }

          public class Groups internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun userGroupId(userGroupId: String): UserGroupIdPath = UserGroupIdPath(__client, _id, userGroupId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,allUsersGroup,id,name,ringId",
                skip: Int? = null,
                top: Int? = null,
              ): List<UserGroupRead> {
                val response = __client.get("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/groups") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId", body: UserGroupWrite? = null): UserGroupRead {
                val response = __client.post("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/groups") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class UserGroupIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _userGroupId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _userGroupId)

              public val `get`: Get = Get(__client, _id, _userGroupId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _userGroupId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/groups/${this._userGroupId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _userGroupId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,allUsersGroup,id,name,ringId"): UserGroupRead {
                  val response = __client.get("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/groups/${this._userGroupId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }

          public class Individuals internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun userId(userId: String): UserIdPath = UserIdPath(__client, _id, userId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId",
                skip: Int? = null,
                top: Int? = null,
              ): List<UserRead> {
                val response = __client.get("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/individuals") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId", body: UserWrite? = null): UserRead {
                val response = __client.post("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/individuals") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class UserIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _userId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _userId)

              public val `get`: Get = Get(__client, _id, _userId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _userId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/individuals/${this._userId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _userId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,banned,email,fullName,guest,id,login,ringId"): UserRead {
                  val response = __client.get("admin/customFieldSettings/bundles/user/${this._id.encodeURLParameter()}/individuals/${this._userId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }

      public class Version internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public val post: Post = Post(__client)

        public fun id(id: String): IdPath = IdPath(__client, id)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,id",
            skip: Int? = null,
            top: Int? = null,
          ): List<BundleRead.VersionBundle> {
            val response = __client.get("admin/customFieldSettings/bundles/version") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.VersionBundle? = null): BundleRead.VersionBundle {
            val response = __client.post("admin/customFieldSettings/bundles/version") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val delete: Delete = Delete(__client, _id)

          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public val values: Values = Values(__client, _id)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id"): BundleRead.VersionBundle {
              val response = __client.get("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,id", body: BundleWrite.VersionBundle? = null): BundleRead.VersionBundle {
              val response = __client.post("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Values internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public val `get`: Get = Get(__client, _id)

            public val post: Post = Post(__client, _id)

            public fun versionBundleElementId(versionBundleElementId: String): VersionBundleElementIdPath = VersionBundleElementIdPath(__client, _id, versionBundleElementId)

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(
                fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released",
                skip: Int? = null,
                top: Int? = null,
              ): List<BundleElementRead.VersionBundleElement> {
                val response = __client.get("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  if (skip != null) {
                    this.url.encodedParameters.append("%24skip", skip.toString())
                  }
                  if (top != null) {
                    this.url.encodedParameters.append("%24top", top.toString())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class Post internal constructor(
              private val __client: HttpClient,
              private val _id: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released", body: BundleElementWrite.VersionBundleElement? = null): BundleElementRead.VersionBundleElement {
                val response = __client.post("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}/values") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                  body?.let {
                    contentType(ContentType.Application.Json)
                    setBody(it)
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }

            public class VersionBundleElementIdPath internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _versionBundleElementId: String,
            ) {
              public val delete: Delete = Delete(__client, _id, _versionBundleElementId)

              public val `get`: Get = Get(__client, _id, _versionBundleElementId)

              public val post: Post = Post(__client, _id, _versionBundleElementId)

              public class Delete internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _versionBundleElementId: String,
              ) {
                public suspend operator fun invoke() {
                  val response = __client.delete("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}/values/${this._versionBundleElementId.encodeURLParameter()}")
                  if (response.status.value != 200) throw ResponseException(response, "")
                }
              }

              public class Get internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _versionBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released"): BundleElementRead.VersionBundleElement {
                  val response = __client.get("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}/values/${this._versionBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }

              public class Post internal constructor(
                private val __client: HttpClient,
                private val _id: String,
                private val _versionBundleElementId: String,
              ) {
                public suspend operator fun invoke(fields: String? = "${'$'}type,archived,color(${'$'}type,background,foreground,id),id,name,ordinal,releaseDate,released", body: BundleElementWrite.VersionBundleElement? = null): BundleElementRead.VersionBundleElement {
                  val response = __client.post("admin/customFieldSettings/bundles/version/${this._id.encodeURLParameter()}/values/${this._versionBundleElementId.encodeURLParameter()}") {
                    if (fields != null) {
                      this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                    }
                    body?.let {
                      contentType(ContentType.Application.Json)
                      setBody(it)
                    }
                  }
                  if (response.status.value != 200) throw ResponseException(response, "")
                  return response.body()
                }
              }
            }
          }
        }
      }
    }

    public class CustomFields internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public fun id(id: String): IdPath = IdPath(__client, id)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal",
          skip: Int? = null,
          top: Int? = null,
        ): List<CustomFieldRead> {
          val response = __client.get("admin/customFieldSettings/customFields") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              this.url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              this.url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal", body: CustomFieldWrite? = null): CustomFieldRead {
          val response = __client.post("admin/customFieldSettings/customFields") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class IdPath internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val delete: Delete = Delete(__client, _id)

        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public val fieldDefaults: FieldDefaults = FieldDefaults(__client, _id)

        public val instances: Instances = Instances(__client, _id)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("admin/customFieldSettings/customFields/${this._id.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal"): CustomFieldRead {
            val response = __client.get("admin/customFieldSettings/customFields/${this._id.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,fieldType(${'$'}type,id),id,isAutoAttached,isUpdateable,localizedName,name,ordinal", body: CustomFieldWrite? = null): CustomFieldRead {
            val response = __client.post("admin/customFieldSettings/customFields/${this._id.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class FieldDefaults internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,id,isPublic"): CustomFieldDefaultsRead {
              val response = __client.get("admin/customFieldSettings/customFields/${this._id.encodeURLParameter()}/fieldDefaults") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,id,isPublic", body: CustomFieldDefaultsWrite? = null): CustomFieldDefaultsRead {
              val response = __client.post("admin/customFieldSettings/customFields/${this._id.encodeURLParameter()}/fieldDefaults") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }

        public class Instances internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val `get`: Get = Get(__client, _id)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal",
              skip: Int? = null,
              top: Int? = null,
            ): List<ProjectCustomFieldRead> {
              val response = __client.get("admin/customFieldSettings/customFields/${this._id.encodeURLParameter()}/instances") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                if (skip != null) {
                  this.url.encodedParameters.append("%24skip", skip.toString())
                }
                if (top != null) {
                  this.url.encodedParameters.append("%24top", top.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }
    }

    public class Types internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,id",
          skip: Int? = null,
          top: Int? = null,
        ): List<FieldTypeRead> {
          val response = __client.get("admin/customFieldSettings/types") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              this.url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              this.url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class DatabaseBackup internal constructor(
    private val __client: HttpClient,
  ) {
    public val backups: Backups = Backups(__client)

    public val settings: Settings = Settings(__client)

    public class Backups internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public fun id(id: String): IdPath = IdPath(__client, id)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,creationDate,id,link,name,size",
          skip: Int? = null,
          top: Int? = null,
        ): List<BackupFile> {
          val response = __client.get("admin/databaseBackup/backups") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              this.url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              this.url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class IdPath internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,creationDate,id,link,name,size"): BackupFile {
            val response = __client.get("admin/databaseBackup/backups/${this._id.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }

    public class Settings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public val backupStatus: BackupStatus = BackupStatus(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,availableDiskSpace,backupStatus(${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id),id,isOn,location"): DatabaseBackupSettingsRead {
          val response = __client.get("admin/databaseBackup/settings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,availableDiskSpace,backupStatus(${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id),id,isOn,location", body: DatabaseBackupSettingsWrite? = null): DatabaseBackupSettingsRead {
          val response = __client.post("admin/databaseBackup/settings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class BackupStatus internal constructor(
        private val __client: HttpClient,
      ) {
        public val `get`: Get = Get(__client)

        public class Get internal constructor(
          private val __client: HttpClient,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,backupCancelled,backupError(${'$'}type,date,errorMessage,id),backupInProgress,id"): io.youtrack.model.BackupStatus {
            val response = __client.get("admin/databaseBackup/settings/backupStatus") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }
  }

  public class GlobalSettings internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public val appearanceSettings: AppearanceSettings = AppearanceSettings(__client)

    public val license: License = License(__client)

    public val localeSettings: LocaleSettings = LocaleSettings(__client)

    public val notificationSettings: NotificationSettings = NotificationSettings(__client)

    public val restSettings: RestSettings = RestSettings(__client)

    public val systemSettings: SystemSettings = SystemSettings(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,appearanceSettings(${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)),id,license(${'$'}type,error,id,license,username),localeSettings(${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)),notificationSettings(${'$'}type,emailSettings(${'$'}type,id,isEnabled),id),restSettings(${'$'}type,allowAllOrigins,allowedOrigins,id),systemSettings(${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize)"): GlobalSettingsRead {
        val response = __client.get("admin/globalSettings") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,appearanceSettings(${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)),id,license(${'$'}type,error,id,license,username),localeSettings(${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)),notificationSettings(${'$'}type,emailSettings(${'$'}type,id,isEnabled),id),restSettings(${'$'}type,allowAllOrigins,allowedOrigins,id),systemSettings(${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize)", body: GlobalSettingsWrite? = null): GlobalSettingsRead {
        val response = __client.post("admin/globalSettings") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class AppearanceSettings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)"): AppearanceSettingsRead {
          val response = __client.get("admin/globalSettings/appearanceSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,dateFieldFormat(${'$'}type,datePattern,id,pattern,presentation),id,timeZone(${'$'}type,id,offset,presentation)", body: AppearanceSettingsWrite? = null): AppearanceSettingsRead {
          val response = __client.post("admin/globalSettings/appearanceSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class License internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,error,id,license,username"): LicenseRead {
          val response = __client.get("admin/globalSettings/license") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,error,id,license,username", body: LicenseWrite? = null): LicenseRead {
          val response = __client.post("admin/globalSettings/license") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class LocaleSettings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)"): LocaleSettingsRead {
          val response = __client.get("admin/globalSettings/localeSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,id,isRTL,locale(${'$'}type,community,id,language,locale,name)", body: LocaleSettingsWrite? = null): LocaleSettingsRead {
          val response = __client.post("admin/globalSettings/localeSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class NotificationSettings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,emailSettings(${'$'}type,id,isEnabled),id"): NotificationSettingsRead {
          val response = __client.get("admin/globalSettings/notificationSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,emailSettings(${'$'}type,id,isEnabled),id", body: NotificationSettingsWrite? = null): NotificationSettingsRead {
          val response = __client.post("admin/globalSettings/notificationSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class RestSettings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,allowAllOrigins,allowedOrigins,id"): RestCorsSettingsRead {
          val response = __client.get("admin/globalSettings/restSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,allowAllOrigins,allowedOrigins,id", body: RestCorsSettingsWrite? = null): RestCorsSettingsRead {
          val response = __client.post("admin/globalSettings/restSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }

    public class SystemSettings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize"): SystemSettingsRead {
          val response = __client.get("admin/globalSettings/systemSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,baseUrl,id,isApplicationReadOnly,maxExportItems,maxUploadFileSize", body: SystemSettingsWrite? = null): SystemSettingsRead {
          val response = __client.post("admin/globalSettings/systemSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }

  public class Projects internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val post: Post = Post(__client)

    public fun id(id: String): IdPath = IdPath(__client, id)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
        skip: Int? = null,
        top: Int? = null,
      ): List<IssueFolderRead.Project> {
        val response = __client.get("admin/projects") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
          if (skip != null) {
            this.url.encodedParameters.append("%24skip", skip.toString())
          }
          if (top != null) {
            this.url.encodedParameters.append("%24top", top.toString())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class Post internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(
        template: String? = null,
        fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName",
        body: IssueFolderWrite.Project? = null,
      ): IssueFolderRead.Project {
        val response = __client.post("admin/projects") {
          if (template != null) {
            this.url.encodedParameters.append("template", template.encodeURLParameter())
          }
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
          body?.let {
            contentType(ContentType.Application.Json)
            setBody(it)
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class IdPath internal constructor(
      private val __client: HttpClient,
      private val _id: String,
    ) {
      public val delete: Delete = Delete(__client, _id)

      public val `get`: Get = Get(__client, _id)

      public val post: Post = Post(__client, _id)

      public val articles: Articles = Articles(__client, _id)

      public val customFields: CustomFields = CustomFields(__client, _id)

      public val issues: Issues = Issues(__client, _id)

      public val timeTrackingSettings: TimeTrackingSettings = TimeTrackingSettings(__client, _id)

      public class Delete internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke() {
          val response = __client.delete("admin/projects/${this._id.encodeURLParameter()}")
          if (response.status.value != 200) throw ResponseException(response, "")
        }
      }

      public class Get internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName"): IssueFolderRead.Project {
          val response = __client.get("admin/projects/${this._id.encodeURLParameter()}") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,archived,customFields,id,leader(${'$'}type,id,login,ringId),name,shortName", body: IssueFolderWrite.Project? = null): IssueFolderRead.Project {
          val response = __client.post("admin/projects/${this._id.encodeURLParameter()}") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Articles internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,content,created,id,idReadable,parentArticle(${'$'}type,id,idReadable),project(${'$'}type,id,name,shortName),summary,updated,updatedBy(${'$'}type,id,login,ringId)",
            skip: Int? = null,
            top: Int? = null,
          ): List<BaseArticleRead.Article> {
            val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/articles") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }

      public class CustomFields internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public fun projectCustomFieldId(projectCustomFieldId: String): ProjectCustomFieldIdPath = ProjectCustomFieldIdPath(__client, _id, projectCustomFieldId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(
            fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal",
            skip: Int? = null,
            top: Int? = null,
          ): List<ProjectCustomFieldRead> {
            val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/customFields") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal", body: ProjectCustomFieldWrite? = null): ProjectCustomFieldRead {
            val response = __client.post("admin/projects/${this._id.encodeURLParameter()}/customFields") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class ProjectCustomFieldIdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _projectCustomFieldId: String,
        ) {
          public val delete: Delete = Delete(__client, _id, _projectCustomFieldId)

          public val `get`: Get = Get(__client, _id, _projectCustomFieldId)

          public val post: Post = Post(__client, _id, _projectCustomFieldId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/projects/${this._id.encodeURLParameter()}/customFields/${this._projectCustomFieldId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal"): ProjectCustomFieldRead {
              val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/customFields/${this._projectCustomFieldId.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _projectCustomFieldId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,bundle(${'$'}type,id),canBeEmpty,defaultValues(${'$'}type,id,name),emptyFieldText,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id,isPublic,ordinal", body: ProjectCustomFieldWrite? = null): ProjectCustomFieldRead {
              val response = __client.post("admin/projects/${this._id.encodeURLParameter()}/customFields/${this._projectCustomFieldId.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class Issues internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public fun issueId(issueId: String): IssueIdPath = IssueIdPath(__client, _id, issueId)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(
            customFields: String? = null,
            fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            skip: Int? = null,
            top: Int? = null,
          ): List<IssueRead> {
            val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/issues") {
              if (customFields != null) {
                this.url.encodedParameters.append("customFields", customFields.encodeURLParameter())
              }
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              if (skip != null) {
                this.url.encodedParameters.append("%24skip", skip.toString())
              }
              if (top != null) {
                this.url.encodedParameters.append("%24top", top.toString())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(
            muteUpdateNotifications: Boolean? = null,
            fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
            body: IssueWrite? = null,
          ): IssueRead {
            val response = __client.post("admin/projects/${this._id.encodeURLParameter()}/issues") {
              if (muteUpdateNotifications != null) {
                this.url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
              }
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class IssueIdPath internal constructor(
          private val __client: HttpClient,
          private val _id: String,
          private val _issueId: String,
        ) {
          public val delete: Delete = Delete(__client, _id, _issueId)

          public val `get`: Get = Get(__client, _id, _issueId)

          public val post: Post = Post(__client, _id, _issueId)

          public class Delete internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueId: String,
          ) {
            public suspend operator fun invoke() {
              val response = __client.delete("admin/projects/${this._id.encodeURLParameter()}/issues/${this._issueId.encodeURLParameter()}")
              if (response.status.value != 200) throw ResponseException(response, "")
            }
          }

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueId: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))"): IssueRead {
              val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/issues/${this._issueId.encodeURLParameter()}") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _issueId: String,
          ) {
            public suspend operator fun invoke(
              muteUpdateNotifications: Boolean? = null,
              fields: String? = "${'$'}type,created,customFields(${'$'}type,id,name,value(${'$'}type,id,name)),description,id,idReadable,links(${'$'}type,direction,id,linkType(${'$'}type,id,localizedName,name)),numberInProject,project(${'$'}type,id,name,shortName),reporter(${'$'}type,id,login,ringId),resolved,summary,updated,updater(${'$'}type,id,login,ringId),visibility(${'$'}type,id,permittedGroups(${'$'}type,id,name,ringId),permittedUsers(${'$'}type,id,login,ringId))",
              body: IssueWrite? = null,
            ): IssueRead {
              val response = __client.post("admin/projects/${this._id.encodeURLParameter()}/issues/${this._issueId.encodeURLParameter()}") {
                if (muteUpdateNotifications != null) {
                  this.url.encodedParameters.append("muteUpdateNotifications", muteUpdateNotifications.toString())
                }
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }
        }
      }

      public class TimeTrackingSettings internal constructor(
        private val __client: HttpClient,
        private val _id: String,
      ) {
        public val `get`: Get = Get(__client, _id)

        public val post: Post = Post(__client, _id)

        public val workItemTypes: WorkItemTypes = WorkItemTypes(__client, _id)

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,estimate(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,timeSpent(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id)"): ProjectTimeTrackingSettingsRead {
            val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/timeTrackingSettings") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,enabled,estimate(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id),id,timeSpent(${'$'}type,field(${'$'}type,fieldType(${'$'}type,id),id,localizedName,name),id)", body: ProjectTimeTrackingSettingsWrite? = null): ProjectTimeTrackingSettingsRead {
            val response = __client.post("admin/projects/${this._id.encodeURLParameter()}/timeTrackingSettings") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class WorkItemTypes internal constructor(
          private val __client: HttpClient,
          private val _id: String,
        ) {
          public val `get`: Get = Get(__client, _id)

          public val post: Post = Post(__client, _id)

          public fun workItemTypeId(workItemTypeId: String): WorkItemTypeIdPath = WorkItemTypeIdPath(__client, _id, workItemTypeId)

          public class Get internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(
              fields: String? = "${'$'}type,autoAttached,id,name",
              skip: Int? = null,
              top: Int? = null,
            ): List<WorkItemTypeRead> {
              val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/timeTrackingSettings/workItemTypes") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                if (skip != null) {
                  this.url.encodedParameters.append("%24skip", skip.toString())
                }
                if (top != null) {
                  this.url.encodedParameters.append("%24top", top.toString())
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class Post internal constructor(
            private val __client: HttpClient,
            private val _id: String,
          ) {
            public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead {
              val response = __client.post("admin/projects/${this._id.encodeURLParameter()}/timeTrackingSettings/workItemTypes") {
                if (fields != null) {
                  this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                }
                body?.let {
                  contentType(ContentType.Application.Json)
                  setBody(it)
                }
              }
              if (response.status.value != 200) throw ResponseException(response, "")
              return response.body()
            }
          }

          public class WorkItemTypeIdPath internal constructor(
            private val __client: HttpClient,
            private val _id: String,
            private val _workItemTypeId: String,
          ) {
            public val delete: Delete = Delete(__client, _id, _workItemTypeId)

            public val `get`: Get = Get(__client, _id, _workItemTypeId)

            public class Delete internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _workItemTypeId: String,
            ) {
              public suspend operator fun invoke() {
                val response = __client.delete("admin/projects/${this._id.encodeURLParameter()}/timeTrackingSettings/workItemTypes/${this._workItemTypeId.encodeURLParameter()}")
                if (response.status.value != 200) throw ResponseException(response, "")
              }
            }

            public class Get internal constructor(
              private val __client: HttpClient,
              private val _id: String,
              private val _workItemTypeId: String,
            ) {
              public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name"): WorkItemTypeRead {
                val response = __client.get("admin/projects/${this._id.encodeURLParameter()}/timeTrackingSettings/workItemTypes/${this._workItemTypeId.encodeURLParameter()}") {
                  if (fields != null) {
                    this.url.encodedParameters.append("fields", fields.encodeURLParameter())
                  }
                }
                if (response.status.value != 200) throw ResponseException(response, "")
                return response.body()
              }
            }
          }
        }
      }
    }
  }

  public class Telemetry internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,allocatedMemory,availableMemory,availableProcessors,blobStringsCacheHitRate,cachedResultsCountInDBQueriesCache,databaseBackgroundThreads,databaseLocation,databaseQueriesCacheHitRate,databaseSize,fullDatabaseSize,id,installationFolder,logsLocation,notificationAnalyzerThreads,onlineUsers(${'$'}type,id,users),pendingAsyncJobs,reportCalculatorThreads,requestsPerSecond,startedTime,textIndexSize,totalTransactions,transactionsPerSecond,uptime,usedMemory"): io.youtrack.model.Telemetry {
        val response = __client.get("admin/telemetry") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }
  }

  public class TimeTrackingSettings internal constructor(
    private val __client: HttpClient,
  ) {
    public val `get`: Get = Get(__client)

    public val workItemTypes: WorkItemTypes = WorkItemTypes(__client)

    public val workTimeSettings: WorkTimeSettings = WorkTimeSettings(__client)

    public class Get internal constructor(
      private val __client: HttpClient,
    ) {
      public suspend operator fun invoke(fields: String? = "${'$'}type,id,workItemTypes(${'$'}type,id,name),workTimeSettings(${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays)"): GlobalTimeTrackingSettings {
        val response = __client.get("admin/timeTrackingSettings") {
          if (fields != null) {
            this.url.encodedParameters.append("fields", fields.encodeURLParameter())
          }
        }
        if (response.status.value != 200) throw ResponseException(response, "")
        return response.body()
      }
    }

    public class WorkItemTypes internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public fun workItemTypeId(workItemTypeId: String): WorkItemTypeIdPath = WorkItemTypeIdPath(__client, workItemTypeId)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(
          fields: String? = "${'$'}type,autoAttached,id,name",
          skip: Int? = null,
          top: Int? = null,
        ): List<WorkItemTypeRead> {
          val response = __client.get("admin/timeTrackingSettings/workItemTypes") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            if (skip != null) {
              this.url.encodedParameters.append("%24skip", skip.toString())
            }
            if (top != null) {
              this.url.encodedParameters.append("%24top", top.toString())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead {
          val response = __client.post("admin/timeTrackingSettings/workItemTypes") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class WorkItemTypeIdPath internal constructor(
        private val __client: HttpClient,
        private val _workItemTypeId: String,
      ) {
        public val delete: Delete = Delete(__client, _workItemTypeId)

        public val `get`: Get = Get(__client, _workItemTypeId)

        public val post: Post = Post(__client, _workItemTypeId)

        public class Delete internal constructor(
          private val __client: HttpClient,
          private val _workItemTypeId: String,
        ) {
          public suspend operator fun invoke() {
            val response = __client.delete("admin/timeTrackingSettings/workItemTypes/${this._workItemTypeId.encodeURLParameter()}")
            if (response.status.value != 200) throw ResponseException(response, "")
          }
        }

        public class Get internal constructor(
          private val __client: HttpClient,
          private val _workItemTypeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name"): WorkItemTypeRead {
            val response = __client.get("admin/timeTrackingSettings/workItemTypes/${this._workItemTypeId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }

        public class Post internal constructor(
          private val __client: HttpClient,
          private val _workItemTypeId: String,
        ) {
          public suspend operator fun invoke(fields: String? = "${'$'}type,autoAttached,id,name", body: WorkItemTypeWrite? = null): WorkItemTypeRead {
            val response = __client.post("admin/timeTrackingSettings/workItemTypes/${this._workItemTypeId.encodeURLParameter()}") {
              if (fields != null) {
                this.url.encodedParameters.append("fields", fields.encodeURLParameter())
              }
              body?.let {
                contentType(ContentType.Application.Json)
                setBody(it)
              }
            }
            if (response.status.value != 200) throw ResponseException(response, "")
            return response.body()
          }
        }
      }
    }

    public class WorkTimeSettings internal constructor(
      private val __client: HttpClient,
    ) {
      public val `get`: Get = Get(__client)

      public val post: Post = Post(__client)

      public class Get internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays"): WorkTimeSettingsRead {
          val response = __client.get("admin/timeTrackingSettings/workTimeSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }

      public class Post internal constructor(
        private val __client: HttpClient,
      ) {
        public suspend operator fun invoke(fields: String? = "${'$'}type,daysAWeek,firstDayOfWeek,id,minutesADay,workDays", body: WorkTimeSettingsWrite? = null): WorkTimeSettingsRead {
          val response = __client.post("admin/timeTrackingSettings/workTimeSettings") {
            if (fields != null) {
              this.url.encodedParameters.append("fields", fields.encodeURLParameter())
            }
            body?.let {
              contentType(ContentType.Application.Json)
              setBody(it)
            }
          }
          if (response.status.value != 200) throw ResponseException(response, "")
          return response.body()
        }
      }
    }
  }
}
