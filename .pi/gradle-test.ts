#!/usr/bin/env -S node --experimental-strip-types
/**
 * gradle-test – standalone CLI
 *
 * Usage:
 *   npx ts-node .pi/gradle-test.ts                          – run all tests
 *   npx ts-node .pi/gradle-test.ts <module>                 – run tests for one module
 *   npx ts-node .pi/gradle-test.ts inspect <testName>                – inspect a stored failure
 *   npx ts-node .pi/gradle-test.ts inspect <module> <name>           – inspect in a specific module
 *   npx ts-node .pi/gradle-test.ts inspect <name> --lines=N          – limit stack trace to N lines (0 = unlimited)
 *
 * Failures are persisted to .pi/gradle-test-failures.json.
 */

import { writeFile, readFile, readdir } from "node:fs/promises";
import { join, resolve } from "node:path";
import { existsSync } from "node:fs";
import { spawn } from "node:child_process";

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

interface Failure {
  module: string;
  /** Display name – platform suffix stripped */
  name: string;
  /** Full name as-is from XML, includes e.g. [jvm] */
  fullName: string;
  classname: string;
  platform: string;
  message: string;
  trace: string;
}

interface FailureStore {
  failures: Failure[];
}

interface ModuleInfo {
  name: string;
  type: "jvm" | "multiplatform";
}

// ---------------------------------------------------------------------------
// XML parsing
// ---------------------------------------------------------------------------

interface ParsedCase {
  name: string;
  classname: string;
  message: string;
  trace: string;
}

function parseJUnitXml(xml: string): { total: number; cases: ParsedCase[] } {
  const total = parseInt(xml.match(/\btests="(\d+)"/)?.[1] ?? "0", 10);
  const failureCount = parseInt(xml.match(/\bfailures="(\d+)"/)?.[1] ?? "0", 10);
  const errorCount = parseInt(xml.match(/\berrors="(\d+)"/)?.[1] ?? "0", 10);

  if (failureCount === 0 && errorCount === 0) {
    return { total, cases: [] };
  }

  const cases: ParsedCase[] = [];
  let pos = 0;

  while (pos < xml.length) {
    const tcStart = xml.indexOf("<testcase", pos);
    if (tcStart === -1) break;

    const tagEnd = xml.indexOf(">", tcStart);
    if (tagEnd === -1) break;

    if (xml[tagEnd - 1] === "/") {
      pos = tagEnd + 1;
      continue;
    }

    const tcEnd = xml.indexOf("</testcase>", tagEnd);
    if (tcEnd === -1) {
      pos = tagEnd + 1;
      continue;
    }

    const block = xml.substring(tcStart, tcEnd);
    pos = tcEnd + "</testcase>".length;

    if (!block.includes("<failure") && !block.includes("<error")) continue;

    const openTag = xml.substring(tcStart, tagEnd + 1);
    const name = openTag.match(/\bname="([^"]*)"/)?.[1] ?? "";
    const classname = openTag.match(/\bclassname="([^"]*)"/)?.[1] ?? "";

    const elemType = block.includes("<failure") ? "failure" : "error";
    const elemStart = block.indexOf(`<${elemType}`);
    const elemTagEnd = block.indexOf(">", elemStart);
    const elemClose = block.indexOf(`</${elemType}>`);

    let message = "";
    let trace = "";

    if (elemStart !== -1 && elemTagEnd !== -1) {
      const elemOpenTag = block.substring(elemStart, elemTagEnd + 1);
      message = elemOpenTag.match(/\bmessage="([^"]*)"/)?.[1] ?? "";
      if (elemClose !== -1) {
        trace = block
          .substring(elemTagEnd + 1, elemClose)
          .replace(/<!\[CDATA\[/g, "")
          .replace(/\]\]>/g, "")
          .trim();
      }
    }

    cases.push({ name, classname, message, trace });
  }

  return { total, cases };
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

function stripPlatformSuffix(name: string): string {
  return name.replace(/\s*\[[^\]]+\]$/, "").trim();
}

function extractPlatform(name: string): string {
  return name.match(/\[([^\]]+)\]$/)?.[1] ?? "";
}

function stripSuitePrefix(name: string): string {
  const arrowIdx = name.lastIndexOf("↘");
  return arrowIdx !== -1 ? name.substring(arrowIdx + 1).trim() : name;
}

async function discoverModules(root: string): Promise<ModuleInfo[]> {
  const settingsPath = join(root, "settings.gradle.kts");
  const content = await readFile(settingsPath, "utf8");

  const names = [...content.matchAll(/include\("([^"]+)"\)/g)].map((m) => m[1]);
  const modules: ModuleInfo[] = [];

  for (const name of names) {
    const buildFile = join(root, name, "build.gradle.kts");
    if (!existsSync(buildFile)) continue;
    const buildContent = await readFile(buildFile, "utf8");
    const type = buildContent.includes("multiplatform") ? "multiplatform" : "jvm";
    modules.push({ name, type });
  }

  return modules;
}

function buildTasks(modules: ModuleInfo[]): string[] {
  return modules.map((m) =>
    m.type === "multiplatform" ? `:${m.name}:allTests` : `:${m.name}:test`,
  );
}

async function findXmlFiles(root: string, moduleName: string): Promise<string[]> {
  const testResultsDir = join(root, moduleName, "build", "test-results");
  if (!existsSync(testResultsDir)) return [];

  const files: string[] = [];
  try {
    const platforms = await readdir(testResultsDir);
    for (const platform of platforms) {
      const platformDir = join(testResultsDir, platform);
      try {
        const entries = await readdir(platformDir);
        for (const entry of entries) {
          if (entry.startsWith("TEST-") && entry.endsWith(".xml")) {
            files.push(join(platformDir, entry));
          }
        }
      } catch {
        // Not a directory or unreadable, skip
      }
    }
  } catch {
    // testResultsDir unreadable
  }

  return files;
}

async function collectFailures(
  root: string,
  modules: ModuleInfo[],
): Promise<{ total: number; failures: Failure[] }> {
  let total = 0;
  const failures: Failure[] = [];

  for (const mod of modules) {
    const xmlFiles = await findXmlFiles(root, mod.name);
    for (const xmlPath of xmlFiles) {
      try {
        const xml = await readFile(xmlPath, "utf8");
        const { total: fileTotal, cases } = parseJUnitXml(xml);
        total += fileTotal;

        for (const tc of cases) {
          failures.push({
            module: mod.name,
            name: stripSuitePrefix(stripPlatformSuffix(tc.name)),
            fullName: tc.name,
            classname: tc.classname,
            platform: extractPlatform(tc.name),
            message: tc.message,
            trace: tc.trace,
          });
        }
      } catch {
        // Unreadable XML, skip
      }
    }
  }

  return { total, failures };
}

function execGradle(gradlew: string, args: string[]): Promise<void> {
  return new Promise((resolve, reject) => {
    const child = spawn(gradlew, args, { stdio: "pipe" });
    child.on("close", (code) => {
      if (code === 0) resolve();
      else reject(new Error(`Gradle exited with code ${code}`));
    });
    child.on("error", reject);
  });
}

// ---------------------------------------------------------------------------
// Store helpers
// ---------------------------------------------------------------------------

function storePath(root: string): string {
  return join(root, ".pi", "gradle-test-failures.json");
}

async function loadStore(root: string): Promise<FailureStore> {
  const path = storePath(root);
  if (existsSync(path)) {
    try {
      return JSON.parse(await readFile(path, "utf8")) as FailureStore;
    } catch {
      // fall through
    }
  }
  return { failures: [] };
}

async function saveStore(root: string, store: FailureStore): Promise<void> {
  await writeFile(storePath(root), JSON.stringify(store, null, 2), "utf8");
}

// ---------------------------------------------------------------------------
// Commands
// ---------------------------------------------------------------------------

async function handleRun(root: string, targetModule: string | undefined): Promise<void> {
  let modules: ModuleInfo[];
  try {
    modules = await discoverModules(root);
  } catch (e) {
    console.error(`Failed to read settings.gradle.kts: ${String(e)}`);
    process.exit(1);
  }

  if (targetModule !== undefined) {
    const found = modules.find((m) => m.name === targetModule);
    if (!found) {
      const names = modules.map((m) => m.name).join(", ");
      console.error(`Unknown module "${targetModule}". Known modules: ${names}`);
      process.exit(1);
    }
    modules = [found];
  }

  const tasks = buildTasks(modules);
  const gradlew = join(root, "gradlew");
  console.log(`Running: ./gradlew ${tasks.join(" ")} --continue`);

  try {
    await execGradle(gradlew, [...tasks, "--continue"]);
  } catch {
    // Gradle exits non-zero when tests fail; expected with --continue.
  }

  const { total, failures } = await collectFailures(root, modules);

  let store = await loadStore(root);
  if (targetModule !== undefined) {
    store.failures = store.failures.filter((f) => f.module !== targetModule);
  } else {
    store.failures = [];
  }
  store.failures.push(...failures);
  await saveStore(root, store);

  // Deduplicate by stripped name + module
  const seen = new Set<string>();
  const unique: Failure[] = [];
  for (const f of failures) {
    const key = `${f.module}::${f.name}`;
    if (!seen.has(key)) {
      seen.add(key);
      unique.push(f);
    }
  }

  if (unique.length === 0) {
    console.log(`\nAll ${total} tests passed.`);
  } else {
    console.log(`\n${unique.length} out of ${total} tests failed:`);
    for (const f of unique) {
      console.log(`  - ${f.name}  (${f.module})`);
    }
    process.exitCode = 1;
  }
}

async function handleInspect(root: string, parts: string[]): Promise<void> {
  if (parts.length === 0) {
    console.error("Usage: gradle-test inspect [module] <testName> [--lines=N]");
    process.exit(1);
  }

  // Extract --lines=N flag before processing other args.
  let traceLines = 10;
  const args = parts.filter((p) => {
    const m = p.match(/^--lines=(\d+)$/);
    if (m) {
      traceLines = parseInt(m[1], 10);
      return false;
    }
    return true;
  });

  const store = await loadStore(root);

  let moduleName: string | undefined;
  let testName: string;

  const knownModules = [...new Set(store.failures.map((f) => f.module))];
  if (args.length >= 2 && knownModules.includes(args[0])) {
    moduleName = args[0];
    testName = args.slice(1).join(" ");
  } else {
    testName = args.join(" ");
  }

  const needle = testName.toLowerCase();
  const matches = store.failures.filter((f) => {
    const nameMatch =
      f.name.toLowerCase().includes(needle) || f.fullName.toLowerCase().includes(needle);
    return nameMatch && (moduleName === undefined || f.module === moduleName);
  });

  if (matches.length === 0) {
    console.error(`No stored failure matching "${testName}".`);
    process.exit(1);
  }

  // When the same test failed on multiple platforms, prefer jvm.
  const deduped: Failure[] = [];
  const byName = new Map<string, Failure[]>();
  for (const m of matches) {
    const key = `${m.module}::${m.name}`;
    if (!byName.has(key)) byName.set(key, []);
    byName.get(key)!.push(m);
  }
  for (const group of byName.values()) {
    const jvm = group.find((f) => f.platform === "jvm");
    deduped.push(jvm ?? group[0]);
  }

  for (const m of deduped) {
    if (m.trace) {
      const lines = m.trace.split("\n");
      const truncated = traceLines > 0 && lines.length > traceLines
        ? [...lines.slice(0, traceLines), `        ... (${lines.length - traceLines} more lines)`]
        : lines;
      console.log(truncated.join("\n"));
    }
    console.log();
  }
}

// ---------------------------------------------------------------------------
// Entry point
// ---------------------------------------------------------------------------

async function main(): Promise<void> {
  const root = resolve(process.cwd());
  const args = process.argv.slice(2);

  if (args[0] === "inspect") {
    await handleInspect(root, args.slice(1));
  } else if (args.length === 0) {
    await handleRun(root, undefined);
  } else if (args.length === 1) {
    await handleRun(root, args[0]);
  } else {
    console.error(
      "Usage: gradle-test [module] | gradle-test inspect [module] <testName>",
    );
    process.exit(1);
  }
}

main().catch((e) => {
  console.error(e);
  process.exit(1);
});
