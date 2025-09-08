package io.github.nomisrev.openapi

import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.jupiter.api.Test

class YouTrackIssuesIntegrationTest {
  @Language("yaml")
  val operation = $$"""{
  "openapi" : "3.0.1",
  "info" : {
    "title" : "YouTrack REST API",
    "description" : "YouTrack issue tracking and project management system",
    "license" : {
      "name" : "YouTrack license",
      "url" : "https://www.jetbrains.com/youtrack/buy/license.html"
    },
    "version" : "2025.3"
  },
  "servers" : [ {
    "url" : "https://youtrack.jetbrains.com:443/api"
  } ],
  "security" : [ {
    "permanentToken" : [ ]
  } ],
  "paths" : {
      "/issues/{id}/attachments" : {
        "description" : "This resource lets you work with attachments in the specific issue.",
        "post" : {
          "parameters" : [ {
             "name" : "muteUpdateNotifications",
            "in" : "query",
            "description" : "Set this parameter to `true` if no notifications should be sent on changes made by this request. This doesn't mute notifications sent by any workflow rules. Using this parameter requires <control>Apply Commands Silently</control> permission in all projects affected by the request. Available since 2021.3.",
            "schema" : {
              "type" : "boolean"
            }
          }, {
            "name" : "fields",
            "in" : "query",
            "schema" : {
              "type" : "string",
              "example" : "$type,author($type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url",
              "default" : "$type,author($type,id,login,ringId),charset,created,extension,id,metaData,mimeType,name,size,updated,url"
            }
          }, {
            "name" : "id",
            "in" : "path",
            "required" : true,
            "schema" : {
              "type" : "string"
            }
          } ],
          "requestBody" : {
            "content" : {
              "multipart/form-data" : {
                "schema" : {
                  "type" : "object",
                  "properties" : {
                    "files[0]" : {
                      "type" : "string",
                      "format" : "binary"
                    }
                  }
                }
              }
            }
          },
          "responses" : {
            "200" : {
              "description" : "collection of IssueAttachment",
              "content" : {
                "application/json" : {
                  "schema" : {
                    "type" : "array",
                    "items" : {
                      "type" : "string"
                    }
                  }
                 }
              }
            }
           }
        }
      }
    }
  }""".trimIndent()

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun test() {
    val openAPI = OpenAPI.fromYaml(operation)
    openAPI.root("YouTrack").endpoints.single().compiles()
  }
}