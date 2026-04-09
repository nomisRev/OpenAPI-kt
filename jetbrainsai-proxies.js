/**
 * Routes Pi providers through the OpenCode proxy defined in ~/.config/opencode/opencode.json.
 *
 * Proxied providers:
 *   anthropic     -> http://host.docker.internal:19516/wire/39b25b562368f306f9d5fcc886959c4d/opencode/anthropic
 *   google-vertex -> http://host.docker.internal:19516/wire/39b25b562368f306f9d5fcc886959c4d/opencode/vertex
 *   openai        -> http://host.docker.internal:19516/wire/39b25b562368f306f9d5fcc886959c4d/opencode/openai
 */
export default function (pi) {
  pi.registerProvider("anthropic", {
    baseUrl: "http://host.docker.internal:19516/wire/39b25b562368f306f9d5fcc886959c4d/pi/anthropic",
    apiKey: "proxy",
  });

  pi.registerProvider("google-vertex", {
    baseUrl: "http://host.docker.internal:19516/wire/39b25b562368f306f9d5fcc886959c4d/pi/vertex",
    apiKey: "proxy",
  });

  pi.registerProvider("openai", {
    baseUrl: "http://host.docker.internal:19516/wire/39b25b562368f306f9d5fcc886959c4d/pi/openai",
    apiKey: "proxy",
  });
}
