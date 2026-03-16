package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val clientSpec by testSuite {
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/users": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/simple-tree"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}": {
              "get": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/nested-tree"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/collaborators": {
              "get": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/users": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/mixed-segments"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/health": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/root-operations"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/a/b/{c}/d/{e}/f": {
              "get": {
                "parameters": [
                  { "name": "c", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "e", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/deep-nesting"
    )

    // Phase 9 tests

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-basic"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/search": {
              "get": {
                "parameters": [
                  { "name": "query", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "X-Api-Key", "in": "header", "required": true, "schema": { "type": "string" } },
                  { "name": "session", "in": "cookie", "required": true, "schema": { "type": "string" } },
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-params"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  { "name": "query", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "X-Request-Id", "in": "header", "required": true, "schema": { "type": "string" } },
                  { "name": "limit", "in": "query", "required": false, "schema": { "type": "integer", "format": "int32" } },
                  { "name": "X-Trace-Id", "in": "header", "required": false, "schema": { "type": "string" } },
                  { "name": "preference", "in": "cookie", "required": false, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-optional"
    )

    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "$ref": "#/components/schemas/CreatePetRequest" }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/settings": {
              "patch": {
                "requestBody": {
                  "required": false,
                  "content": {
                    "application/json": {
                      "schema": { "$ref": "#/components/schemas/UpdateSettingsRequest" }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          },
          "components": {
            "schemas": {
              "CreatePetRequest": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" }
                },
                "required": ["name"]
              },
              "UpdateSettingsRequest": {
                "type": "object",
                "properties": {
                  "theme": { "type": "string" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-json"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/markdown": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "properties": {
                          "text": {
                            "description": "The Markdown text to render in HTML.",
                            "type": "string"
                          },
                          "mode": {
                            "description": "The rendering mode.",
                            "enum": [
                              "markdown",
                              "gfm"
                            ],
                            "default": "markdown",
                            "example": "markdown",
                            "type": "string"
                          },
                          "context": {
                            "description": "The repository context to use when creating references in `gfm` mode. For example, setting `context` to `octo-org/octo-repo` will change the text `#42` into an HTML link to issue 42 in the `octo-org/octo-repo` repository.",
                            "type": "string"
                          }
                        },
                        "required": [
                          "text"
                        ],
                        "type": "object"
                      }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "text/html": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-markdown-body"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/widgets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "string" },
                            "version": { "type": "integer", "format": "int32" }
                          },
                          "required": ["id"]
                        }
                      }
                    }
                  }
                }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "name": { "type": "string" }
                        },
                        "required": ["name"]
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "string" },
                            "name": { "type": "string" }
                          },
                          "required": ["id", "name"]
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-method-uniqueness"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/uploads/{uploadId}": {
              "post": {
                "parameters": [
                  { "name": "uploadId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "file": { "type": "string", "format": "binary" },
                          "checksum": { "type": "string" },
                          "retries": { "type": "integer", "format": "int32" },
                          "tags": {
                            "type": "array",
                            "items": { "type": "string" }
                          }
                        },
                        "required": ["file", "checksum", "tags"]
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "207": {
                    "description": "Multi-Status",
                    "content": {
                      "application/json": {
                        "schema": { "type": "array", "items": { "type": "string" } }
                      }
                    }
                  },
                  "422": {
                    "description": "Unprocessable Entity",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": { "type": "boolean" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-complex-body-response"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/files": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "file": { "type": "string", "format": "binary" },
                          "purpose": { "type": "string" }
                        },
                        "required": ["file", "purpose"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-multipart"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/oauth/token": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "grant_type": { "type": "string" },
                          "code": { "type": "string" },
                          "redirect_uri": { "type": "string" }
                        },
                        "required": ["grant_type", "code", "redirect_uri"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-form"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/legacy": {
              "get": {
                "deprecated": true,
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-deprecated"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  {
                    "name": "status",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "type": "string",
                      "enum": ["active", "archived", "all"],
                      "default": "all"
                    }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-enum"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  {
                    "name": "limit",
                    "in": "query",
                    "required": true,
                    "schema": { "type": "integer", "format": "int32", "default": 20 }
                  },
                  {
                    "name": "offset",
                    "in": "query",
                    "required": false,
                    "schema": { "type": "integer", "format": "int32", "default": 0 }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-defaults"
    )

    // Phase 10 tests — Response Handling

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "array", "items": { "type": "string" } }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-single"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "delete": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-unit"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "404": {
                    "description": "Not Found",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-multiple"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-default"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "204": {
                    "description": "No Content"
                  },
                  "404": {
                    "description": "Not Found",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-mixed"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "400": {
                    "description": "Bad Request",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-with-body"
    )

    // Phase 11 tests — Ktor Implementation + Server + Factory

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "servers": [
            {
              "url": "https://api.example.com",
              "description": "Production server"
            },
            {
              "url": "https://staging-api.example.com",
              "description": "Staging server"
            }
          ],
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/factory-servers"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "servers": [
            {
              "url": "https://{environment}.api.example.com/{version}",
              "description": "Configurable server",
              "variables": {
                "environment": {
                  "default": "prod",
                  "enum": ["prod", "staging", "dev"]
                },
                "version": {
                  "default": "v1"
                }
              }
            }
          ],
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/factory-server-variables"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  { "name": "limit", "in": "query", "required": false, "schema": { "type": "integer", "format": "int32" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "array", "items": { "type": "string" } }
                      }
                    }
                  }
                }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "400": {
                    "description": "Bad Request",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/impl-full"
    )
}
