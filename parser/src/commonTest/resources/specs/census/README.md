# Preferred-version census reductions

Each `.json` has an equivalent `.yaml`. These are intentionally unresolved
reproducers, exercised by `CensusRegressionTest` as rejection characterization
checks. When implementing a fix, replace those checks with successful parsing
and assertions that preserve the enum JSON values or the paths extension.
No production parser behavior was changed for this census.

Enum schemas were moved to `components.schemas.Value` to remove unrelated
operations, parameters and response wrappers. Extra enum members, defaults,
descriptions and object properties were removed; the offending value kind and
schema type remain. The paths fixture retains the original extension key/value.
The fixtures are small reproducers, not independently validated source specs.
YAML parity was tested locally; the remote census used JSON only.

The [Schema Object](https://spec.openapis.org/oas/v3.0.3.html#schema-object)
does not restrict enum members to strings. Docker and PandaScore demonstrate
object-valued enums consistent with their declared type. Apacta, Enode and BC
Job Posting have mismatched types and should be tracked separately as source
quality cases. The [Paths Object](https://spec.openapis.org/oas/v3.0.3.html#paths-object)
allows specification extensions; Apicurio demonstrates a separate parser gap.
These observations do not establish validity of the entire original documents.

## Provenance

Original documents and phase journals are cached under
`exploration/build/preferred-census/`; see `exploration/FINDINGS.md` for totals.
SHA-256 below identifies the unmodified downloaded document, not its reduction.

- `apacta-array-enum` — [apacta.com 0.0.42](https://api.apis.guru/v2/specs/apacta.com/0.0.42/openapi.json): string schema with an array enum member; type and value disagree.
  Source pointer: `/paths/~1expenses/get/parameters/11/schema`.
  Corpus key: `45032aa34518e6bdb425`.
  SHA-256: `b33e191699b0af460274782a4cef0ddc5dbea468840bae5ef7aa366cb05703fe`.

- `enode-object-enum` — [enode.io 1.3.10](https://api.apis.guru/v2/specs/enode.io/1.3.10/openapi.json): string schema with an object enum member; type and value disagree.
  Source pointer: `/paths/~1vehicles~1{vehicleId}~1charge-state/get/responses/200/content/application~1json/schema/properties/isChargingReasons/items`.
  Corpus key: `6b310704012b29b07d4b`.
  SHA-256: `b7e513e2be60e55178d56fd35b76f82ebda6eea1c4188bc1aada07cd5135883e`.

- `docker-object-enum` — [docker.com:engine 1.33](https://api.apis.guru/v2/specs/docker.com/engine/1.33/openapi.json): object schema with an empty object enum member.
  Source pointer: `/components/schemas/ContainerConfig/properties/ExposedPorts/additionalProperties`.
  Corpus key: `72153fdeeb7f32d0f9f5`.
  SHA-256: `8c997693ce1e507b72b001bcac1603f36d8542e16017e120fa847dfabded2acc`.

- `apicurio-paths-extension` — [apicurio.local:registry 2.4.x](https://api.apis.guru/v2/specs/apicurio.local/registry/2.4.x/openapi.json): string-valued Paths Object extension.
  Source pointer: `/paths/x-codegen-contextRoot`.
  Corpus key: `8fd2e8614757d07ad158`.
  SHA-256: `66b1813fe1ad392bb874532dfa880795ea768a8300416b5e8445009d6139349a`.

- `jobposting-array-enum` — [gov.bc.ca:jobposting 1.0.0](https://api.apis.guru/v2/specs/gov.bc.ca/jobposting/1.0.0/openapi.json): integer schema with array enum members; type and value disagree.
  Source pointer: `/paths/~1jobs/post/requestBody/content/application~1json/schema/properties/jobTypes`.
  Corpus key: `9f0fc369baad2493ee6a`.
  SHA-256: `f89a564a6457ba61992b9f942e28245dacecd0f08240afc7dd2ff5a5aa5a327a`.

- `pandascore-object-enum` — [pandascore.co 2.23.1](https://api.apis.guru/v2/specs/pandascore.co/2.23.1/openapi.json): object schema with object enum members.
  Source pointer: `/components/schemas/CurrentVideogame`.
  Corpus key: `a16b7327554a85f8e46f`.
  SHA-256: `d8fd0a99b69306865eb66df122c489f7758f7e9a2ff3b70ee8da6b3548aa6f34`.

The shared `codat-boolean-property` fixture preserves a property literally named
`additionalProperties` whose schema is `false`; this is not the object-level
`additionalProperties: false` keyword. Boolean schemas are supported by the
[OpenAPI 3.1 Schema Object](https://spec.openapis.org/oas/v3.1.0.html#schema-object).
It was reduced by moving the containing component to `Value` and removing all
other properties. Both source documents fail on this same shape.

- `codat-boolean-property` — [codat.io:sync-for-expenses prealpha](https://api.apis.guru/v2/specs/codat.io/sync-for-expenses/prealpha/openapi.json).
  Source pointer: `/components/schemas/DataConnection/properties/additionalProperties`.
  Corpus key: `de45456e96ccc209edac`.
  SHA-256: `80ac157144261e8d026ad749257104831fc7efe889d5ea7004e5c2787d6d0b1b`.

- `codat-boolean-property` — [codat.io:sync-for-commerce 1.1](https://api.apis.guru/v2/specs/codat.io/sync-for-commerce/1.1/openapi.json).
  Source pointer: `/components/schemas/Connection/properties/additionalProperties`.
  Corpus key: `f2241f987ecc68b32e34`.
  SHA-256: `fe6cec20562b75464be54155a469986d84dcfb9a053fbb3003477a8df080cfb8`.

