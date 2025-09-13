package io.github.nomisrev.openapi

import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.jupiter.api.Test

class YouTrackIssuesIntegrationTest2 {
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
      "/admin/projects" : {
        "description" : "This resource provides access to projects.",
        "post" : {
          "parameters" : [ {
             "name" : "template",
            "in" : "query",
            "description" : "If the `template` is not specified, then the new project will use the default settings.\n<emphasis>Optional</emphasis>. Lets you specify the template to use for the new project.\nPossible values: `scrum`, `kanban`.",
            "schema" : {
              "type" : "string"
            }
          }, {
            "name" : "fields",
            "in" : "query",
            "schema" : {
              "type" : "string",
              "example" : "$type,archived,customFields,id,leader($type,id,login,ringId),name,shortName",
              "default" : "$type,archived,customFields,id,leader($type,id,login,ringId),name,shortName"
            }
          } ],
          "requestBody" : {
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "string"
                }
              }
            }
          },
          "responses" : {
            "200" : {
              "description" : "single Project",
              "content" : {
                "application/json" : {
                  "schema" : {
                    "type" : "string"
                  }
                }
              }
            }
          }
        }
      },
      "/admin/projects/{id}" : {
        "description" : "This resource provides access to projects.",
        "post" : {
          "parameters" : [ {
            "name" : "fields",
             "in" : "query",
            "schema" : {
              "type" : "string",
              "example" : "$type,archived,customFields,id,leader($type,id,login,ringId),name,shortName",
              "default" : "$type,archived,customFields,id,leader($type,id,login,ringId),name,shortName"
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
              "application/json" : {
                "schema" : {
                  "type" : "string"
                }
              }
            }
          },
          "responses" : {
            "200" : {
              "description" : "single Project",
              "content" : {
                "application/json" : {
                  "schema" : {
                    "type" : "string"
                  }
                }
              }
            }
          }
        },
        "parameters" : [ {
          "name" : "id",
           "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ]
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