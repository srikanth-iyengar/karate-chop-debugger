---
marp: true
paginate: true
backgroundColor: #0d1117
color: #e6edf3
footer: 'Karate Chop Debugger — Internal Demo'
theme: dracula
style: |
  /* Local overrides in addition to theme */
  section { font-size: 26px; }
  section h1 { margin-bottom: 0.2em; }
  section ul { margin-top: 0.4em; }
---

<!-- _class: lead -->
# 🥋 Karate Chop Debugger

**Debug Karate tests — natively in IntelliJ**

Unofficial plugin to set breakpoints in `.feature` files, step through, and inspect variables.

---

## Features

- Run individual scenarios directly from editor (Guttern Icons)
- Breakpoints in `.feature` files; seamless IntelliJ integration
- Variables panel + expression evaluation (Karate / JS)
- Step Over (F8), Step Into (F7), Step Back
- Hot Reload current scenario during debug
- Improved syntax highlighting and newline handling for `karate-dsl`
- More precise Step Into navigation
- Target: IntelliJ IDEA 2025.1+ (sinceBuild 251)

---

## Internal: RuntimeHook Debugging (How it works)

- Hooking execution
  - `DebugHook` implements Karate `RuntimeHook`
  - `beforeStep(Step, ScenarioRuntime)` checks active breakpoints
- Pausing & stepping
  - Uses `CountDownLatch(1)` to pause at a hit step
  - IDE commands (Resume / Step) release or re-arm the latch
- IDE ↔ Agent bridge (TCP)
  - IDE server: `DebugServer` (listens, forwards via message bus)
  - Agent client: `DebugClient` (connects back, proxies calls)
- Bootstrapping
  - Agent `Main` registers `new DebugHook()` via `Runner.builder().hook(...)`
  - IDE launches Agent from `KarateExecutionService`

---

## Code pointers:

Code pointers:
- `debug-agent/.../agent/DebugHook.java` (RuntimeHook, `beforeStep`, latch)
- `debug-agent/.../agent/Main.java` (hook registration)
- `debug-agent/.../communication/DebugServer.java`, `DebugClient.java`
- `src/main/.../KarateExecutionService.kt`, actions & breakpoint type in `language/*`, `plugin.xml`

---

## Limitations & Future Scope

- Current limitations
  - Requires plugin-triggered debug run (not generic JVM attach)
  - Evaluation context limited to current step scope
  - Loopback TCP must be available in environment
- Future scope
  - Rich JSON rendering & pretty-printing in Variables
  - Conditional breakpoints / logpoints
  - Parallel execution awareness
