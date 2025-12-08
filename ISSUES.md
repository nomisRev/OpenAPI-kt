# Response

## Support multivalue responses

```json
{
  "responses": {
    "200": {
      "description": "An paged array of pets",
      "headers": {
        "x-next": {
          "description": "A link to the next page of responses",
          "schema": {
            "type": "string"
          }
        }
      },
      "content": {
        "application/json": {
          "schema": {
            "$ref": "#/components/schemas/Pets"
          }
        }
      }
    },
    "default": {
      "description": "unexpected error",
      "content": {
        "application/json": {
          "schema": {
            "$ref": "#/components/schemas/Error"
          }
        }
      }
    }
  }
}
```