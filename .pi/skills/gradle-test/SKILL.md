---
name: gradle-test
description: Run Gradle tests and inspect failures for this Kotlin Multiplatform project. Use when asked to run tests, check test results, or investigate a specific failing test by name.
---

# gradle-test

A CLI tool at `.pi/gradle-test.ts` that runs Gradle tests, collects JUnit XML results, persists failures across sessions, and lets you inspect individual failures by name.

Failures are stored in `.pi/gradle-test-failures.json` and survive between sessions, so `inspect` works even without re-running tests.

Always run from the project root.

## Run all tests

```bash
.pi/gradle-test.ts
```

## Run tests for one module

Known modules: `parser`, `typed`, `renderer`, `gradle-plugin`

```bash
.pi/gradle-test.ts <module>
```

Examples:
```bash
.pi/gradle-test.ts parser
.pi/gradle-test.ts typed
```

## Inspect a stored failure

Look up a failure by name after tests have been run. The name is a case-insensitive substring match.

```bash
.pi/gradle-test.ts inspect <testName>
.pi/gradle-test.ts inspect <module> <testName>
```

Examples:
```bash
.pi/gradle-test.ts inspect "should parse"
.pi/gradle-test.ts inspect parser "should parse"
```

The inspect command prints the full failure message and stack trace.

## Output

- **All pass:** `All N tests passed.`
- **Failures:** lists each failing test name and its module, exits with code 1.
- **Inspect:** prints the failure header, message, and stack trace for every match.
